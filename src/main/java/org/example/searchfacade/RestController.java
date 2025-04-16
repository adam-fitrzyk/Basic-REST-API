package org.example.searchfacade;

import org.example.searchfacade.model.Event;
import org.example.searchfacade.model.EventRepository;
import org.example.searchfacade.model.User;
import org.example.searchfacade.model.UserRepository;

import org.example.searchfacade.utilities.FileHandler;
import org.example.searchfacade.utilities.HtmlCrafter;
import org.example.searchfacade.utilities.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;

import org.bson.Document;

public class RestController {

    private UserRepository user_repository;
    private EventRepository event_repository;

    public RestController() {
        this.user_repository = UserRepository.getInstance();
        this.event_repository = EventRepository.getInstance();
    }
    
    public byte[] getResource(String resource_path, String parameters) {
        // Build response
        ArrayList<Document> filters;
        String header;
        byte[] resource;
        var response = new ByteArrayOutputStream();

        try {
            switch (resource_path) {
                case "/", "/index.html":
                    resource = this.getIndex();
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving index html page...");
                    break;

                case "/img/koala-icon.ico":
                    resource = this.getFavicon();
                    header = "HTTP/1.1 200 OK\r\nContent-Type: image/x-icon\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving favicon image...");
                    break;

                case "/css/search-facade.css":
                    resource = this.getCSS();
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/css\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving css file...");
                    break;

                case "/users", "/users/":
                    resource = this.getUsers();
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving all user entries...");
                    break;

                case "/users/search", "/users/search/":
                    if (parameters == null) {
                        header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
                        response.write(header.getBytes());
                        break;
                    }
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    filters = RequestHandler.parseParameters(parameters);
//                    for (var filter : filters) {
//                        System.out.println(filter.toJson());
//                    }

                    if (RequestHandler.validateParameters(filters)) {
                        resource = this.getUsersBySearch(filters);
                    } else {
                        resource = this.getUsers();
                    }

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving user entries by filter " + filters.toString() + "...");
                    break;

                case "/events", "/events/":
                    resource = this.getEvents();
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving all event entries...");
                    break;

                case "/events/search", "/events/search/":
                    if (parameters == null) {
                        header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
                        response.write(header.getBytes());
                        break;
                    }
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    filters = RequestHandler.parseParameters(parameters);
//                    for (var filter : filters) {
//                        System.out.println(filter.toJson());
//                    }

                    if (RequestHandler.validateParameters(filters)) {
                        resource = this.getEventsBySearch(filters);
                    } else {
                        resource = this.getEvents();
                    }

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving event entries by filter " + filters.toString() + "...");
                    break;
            }

            if (response.size() == 0) {
                if (resource_path.matches("/users/.+")) {
                    String id = resource_path.split("/")[2];

                    resource = this.getUsersById(id);
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving user entries by id...");
                } else if (resource_path.matches("/events/.+")) {
                    String id = resource_path.split("/")[2];

                    resource = this.getEventsById(id);
                    header = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Serving event entries by id...");
                } else {
                    header = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\n";
                    resource = "Error 404, Not Found".getBytes();

                    response.write(header.getBytes());
                    response.write(resource);

                    System.out.println("Unknown request, serving 404 Error...");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response.toByteArray();
    }

    public byte[] getIndex() {
        return FileHandler.getFileAsBytes("index.html");
    }

    public byte[] getFavicon() {
        return FileHandler.getFileAsBytes("img/koala-icon.ico");
    }

    public byte[] getCSS() {
        return FileHandler.getFileAsBytes("css/search-facade.css");
    }

    public byte[] getUsers() {
        var resource = new StringBuilder();
        var users = this.user_repository.findAll();

        for (User user : users) {
            resource.append(user.toString().strip())
                    .append(",\n");
        }
        resource.delete(resource.length() - 2, resource.length());
        var response = HtmlCrafter.insertToHTML(resource.toString());

        return response.getBytes();
    }

    public byte[] getUsersById(String id) {
        var resource = this.user_repository.findById(id);

        if (resource != null) {
            var response = HtmlCrafter.insertToHTML(resource.toString());

            return response.getBytes();
        }

        return "".getBytes();
    }
    
    public byte[] getUsersBySearch(List<Document> filters) {
        var users = this.user_repository.findByFilters(filters);
        var resource = new StringBuilder();

        if (!users.isEmpty()) {
            for (var user : users) {
                resource.append(user.toString().strip())
                        .append(",\n");
            }
            resource.delete(resource.length() - 2, resource.length());
        }
        var response = HtmlCrafter.insertToHTML(resource.toString());

        return response.getBytes();
    }

    public byte[] getEvents() {
        var resource = new StringBuilder();
        var events = this.event_repository.findAll();

        for (Event event : events) {
            resource.append(event.toString().strip())
                    .append(",\n");
        }
        resource.delete(resource.length() - 2, resource.length() - 1);
        var response = HtmlCrafter.insertToHTML(resource.toString());

        return response.getBytes();
    }

    public byte[] getEventsById(String id) {
        var resource = this.event_repository.findById(id);

        if (resource != null) {
            var response = HtmlCrafter.insertToHTML(resource.toString());

            return response.getBytes();
        }

        return "".getBytes();
    }

    public byte[] getEventsBySearch(List<Document> filters) {
        var resource = new StringBuilder();
        var events = this.event_repository.findByFilters(filters);
        
        if (!events.isEmpty()) {
            for (var event : events) {
                resource.append(event.toString().strip())
                        .append(",\n");
            }
            resource.delete(resource.length() - 2, resource.length() - 1);
        }
        var response = HtmlCrafter.insertToHTML(resource.toString());

        return response.getBytes();
    }

}
