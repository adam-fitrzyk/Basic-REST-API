package org.example.restapi;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.net.URLDecoder.decode;

import org.bson.Document;
import org.bson.json.JsonParseException;

public class RESTController {

    private static FilterSchema filterSchema = new FilterSchema();
    
    public static String[] parseRequest(ArrayList<String> request) {
        String method;
        String resource_path;
        String parameters;
        String protocol;
        var components = request.getFirst().split(" ");

        method = components[0];

        var resource_request = components[1].split("\\?");
        resource_path = resource_request[0];
        if (resource_request.length > 1) {
            parameters = resource_request[1];
        } else {
            parameters = null;
        }
        
        protocol = components[2];

        return new String[] {method, resource_path, parameters, protocol};
    }

    public static ArrayList<Document> parseParameters(String parameters) {
        /* Parameters are JSON formatted filters and should follow the following structure:
         * {
         *      "attribute": text,   // required
         *      "operator": text,    // required
         *      "value": text,       // optional if "range" is provided
         *      "range": {           // optional if "value" is provided
         *          "from": text,
         *          "to": text
         *      }
         *  }
         */

        var filters = new ArrayList<Document>();
        var pairs = parameters.split("&");

        try {
            for (var pair : pairs) {
                String filterString = pair.split("=")[1];
                filters.add(Document.parse(decode(filterString, "UTF-8")));
            }
        } catch (JsonParseException | UnsupportedEncodingException e) {
            assert true;
        }

        return filters;
    }

    public static int validateRequest(String[] components) {
        if (!components[0].equals("GET")) {
            return 405; // Method Not Allowed
        }
        if (!components[3].equals("HTTP/1.1")) {
            return 505; // HTTP Version Not Supported
        }
        if (components[1].contains("..")) {
            return 403; // Forbidden
        }
        return 200; // OK
    }

    public static boolean validateParameters(List<Document> filters) {
        for (var filter : filters) {
            if (!filterSchema.validateFilter(filter)) {
                return false;
            }
        }
        return true;
    }

    public static String getResource(String resource_path, String parameters) {
        // Build response
        ArrayList<Document> filters;
        String resource;
        var response = new StringBuilder();

        System.out.println(parameters);

        switch (resource_path) {
            case "/":
                resource = RESTController.getIndex();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/users", "/users/":
                resource = RESTController.getUsers();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/users/search", "/users/search/":
                if (parameters == null) {
                    response.append("HTTP/1.1 200 OK\r\n\r\n");
                    break;
                }

                filters = RESTController.parseParameters(parameters);
                for (var filter : filters) {
                    System.out.println(filter.toJson());
                }

                if (RESTController.validateParameters(filters)) {
                    resource = RESTController.getUsersBySearch(filters);
                } else {
                    resource = RESTController.getUsers();
                }

                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/events", "/events/":
                resource = RESTController.getEvents();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/events/search", "/events/search/":
                if (parameters == null) {
                    response.append("HTTP/1.1 200 OK\r\n\r\n");
                    break;
                }

                filters = RESTController.parseParameters(parameters);
                for (var filter : filters) {
                    System.out.println(filter.toJson());
                } 
                
                if (RESTController.validateParameters(filters)) {
                    resource = RESTController.getEventsBySearch(filters);
                } else {
                    resource = RESTController.getEvents();
                }

                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;
        }

        if (response.isEmpty()) {
            if (resource_path.matches("/users/.+")) {
                String id = resource_path.split("/")[2];
                System.out.println("> We've got an id! " + id);

                resource = RESTController.getUsersById(id);
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);             
            } 
            else if (resource_path.matches("/events/.+")) {
                String id = resource_path.split("/")[2];
                System.out.println("> We've got an id! " + id);

                resource = RESTController.getEventsById(id);
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
            } 
            else {
                response.append("HTTP/1.1 404 Not Found\r\n\r\n")
                        .append("Error 404, Not Found");
            }
        }

        return response.toString();
    }

    public static String getIndex() {
        try (var input_stream = RESTController.class.getResourceAsStream("/index.html")) {
            var reader = new BufferedReader(new InputStreamReader(input_stream));

            var content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                System.out.println(line);
            }

            return content.toString();
        }
        catch (IOException e) {
            System.out.println(e);
            return e.getMessage();
        }
    }

    public static String getUsers() {
        var resource = new StringBuilder();
        var users = UserRepository.getInstance().findAll();

        for (User user : users) {
            resource.append(user.toString())
                    .append("\r\n");
        }

        return resource.toString();
    }

    public static String getUsersById(String id) {
        var resource = UserRepository.getInstance().findById(id);

        if (resource != null) {
            return resource.toString();
        }

        return "";
    }
    
    public static String getUsersBySearch(List<Document> filters) {
        var users = UserRepository.getInstance().findByFilters(filters);
        var resource = new StringBuilder();

        if (!users.isEmpty()) {
            for (var user : users) {
                resource.append(user.toString())
                        .append("\r\n");
            }
        }

        return resource.toString();
    }

    public static String getEvents() {
        var resource = new StringBuilder();
        var events = EventRepository.getInstance().findAll();

        for (Event event : events) {
            resource.append(event.toString())
                    .append("\r\n");
        }

        return resource.toString();
    }

    public static String getEventsById(String id) {
        var resource = EventRepository.getInstance().findById(id);

        if (resource != null) {
            return resource.toString();
        }

        return "";
    }

    public static String getEventsBySearch(List<Document> filters) {
        var resource = new StringBuilder();
        var events = EventRepository.getInstance().findByFilters(filters);
        
        if (!events.isEmpty()) {
            for (var event : events) {
                resource.append(event.toString())
                        .append("\r\n");
            }
        }

        return resource.toString();
    }

    public static Set<String> findUncommonElements(Set<String> set1, Set<String> set2) {
        var symmetricDifference = new HashSet<>(set1);
        symmetricDifference.addAll(set2);

        var intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        symmetricDifference.removeAll(intersection);
        return symmetricDifference;
    }

}
/*
{"attribute": "", "operator": "eq", "value": "", "range": {}}
 */