package org.example.restapi;

import org.example.restapi.model.Event;
import org.example.restapi.model.EventRepository;
import org.example.restapi.model.User;
import org.example.restapi.model.UserRepository;

import org.example.restapi.utilities.RequestHandler;

import java.util.List;
import java.util.ArrayList;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.bson.Document;

public class RESTController {

    private UserRepository user_repository;
    private EventRepository event_repository;

    public RESTController() {
        this.user_repository = UserRepository.getInstance();
        this.event_repository = EventRepository.getInstance();
    }
    
    public String getResource(String resource_path, String parameters) {
        // Build response
        ArrayList<Document> filters;
        String resource;
        var response = new StringBuilder();

        System.out.println(parameters);

        switch (resource_path) {
            case "/":
                resource = this.getIndex();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/users", "/users/":
                resource = this.getUsers();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/users/search", "/users/search/":
                if (parameters == null) {
                    response.append("HTTP/1.1 200 OK\r\n\r\n");
                    break;
                }

                filters = RequestHandler.parseParameters(parameters);
                for (var filter : filters) {
                    System.out.println(filter.toJson());
                }

                if (RequestHandler.validateParameters(filters)) {
                    resource = this.getUsersBySearch(filters);
                } else {
                    resource = this.getUsers();
                }

                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/events", "/events/":
                resource = this.getEvents();
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;

            case "/events/search", "/events/search/":
                if (parameters == null) {
                    response.append("HTTP/1.1 200 OK\r\n\r\n");
                    break;
                }

                filters = RequestHandler.parseParameters(parameters);
                for (var filter : filters) {
                    System.out.println(filter.toJson());
                } 
                
                if (RequestHandler.validateParameters(filters)) {
                    resource = this.getEventsBySearch(filters);
                } else {
                    resource = this.getEvents();
                }

                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);
                break;
        }

        if (response.isEmpty()) {
            if (resource_path.matches("/users/.+")) {
                String id = resource_path.split("/")[2];
                System.out.println("> We've got an id! " + id);

                resource = this.getUsersById(id);
                response.append("HTTP/1.1 200 OK\r\n\r\n")
                        .append(resource);             
            } 
            else if (resource_path.matches("/events/.+")) {
                String id = resource_path.split("/")[2];
                System.out.println("> We've got an id! " + id);

                resource = this.getEventsById(id);
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

    public String getIndex() {
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

    public String getUsers() {
        var resource = new StringBuilder();
        var users = this.user_repository.findAll();

        for (User user : users) {
            resource.append(user.toString())
                    .append("\r\n");
        }

        return resource.toString();
    }

    public String getUsersById(String id) {
        var resource = this.user_repository.findById(id);

        if (resource != null) {
            return resource.toString();
        }

        return "";
    }
    
    public String getUsersBySearch(List<Document> filters) {
        var users = this.user_repository.findByFilters(filters);
        var resource = new StringBuilder();

        if (!users.isEmpty()) {
            for (var user : users) {
                resource.append(user.toString())
                        .append("\r\n");
            }
        }

        return resource.toString();
    }

    public String getEvents() {
        var resource = new StringBuilder();
        var events = this.event_repository.findAll();

        for (Event event : events) {
            resource.append(event.toString())
                    .append("\r\n");
        }

        return resource.toString();
    }

    public String getEventsById(String id) {
        var resource = this.event_repository.findById(id);

        if (resource != null) {
            return resource.toString();
        }

        return "";
    }

    public String getEventsBySearch(List<Document> filters) {
        var resource = new StringBuilder();
        var events = this.event_repository.findByFilters(filters);
        
        if (!events.isEmpty()) {
            for (var event : events) {
                resource.append(event.toString())
                        .append("\r\n");
            }
        }

        return resource.toString();
    }

}
