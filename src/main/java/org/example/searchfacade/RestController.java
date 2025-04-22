package org.example.searchfacade;

import org.example.searchfacade.http.HttpResponse;
import org.example.searchfacade.http.HttpStatus;
import org.example.searchfacade.model.Event;
import org.example.searchfacade.model.EventRepository;
import org.example.searchfacade.model.User;
import org.example.searchfacade.model.UserRepository;

import org.example.searchfacade.utilities.FileHandler;
import org.example.searchfacade.utilities.FilterSchemaManager;
import org.example.searchfacade.utilities.HtmlCrafter;
import org.example.searchfacade.http.HttpHandler;

import java.util.List;
import java.util.ArrayList;

import org.bson.Document;

public class RestController {

    private final UserRepository user_repository;
    private final EventRepository event_repository;
    private final FilterSchemaManager schema_manager;

    public RestController() {
        this.user_repository = UserRepository.getInstance();
        this.event_repository = EventRepository.getInstance();
        this.schema_manager = FilterSchemaManager.getInstance();
    }
    
    public HttpResponse getResponse(String resource_path, String parameters) {
        // TODO add default and headers that are supposed to be there

        // Build response
        var response = new HttpResponse();
        response.setProtocol("HTTP/1.1");

        // Set default response as successful
        response.setStatus(HttpStatus.SUCCESS_200_OK);

        ArrayList<Document> filters;
        byte[] resource;

        try {
            switch (resource_path) {
                case "/", "/index.html":
                    resource = this.getIndex();

                    response.addHeader("Content-Type", "text/html");
                    response.setBody(resource);

                    System.out.println("Serving index html page...");
                    break;

                case "/img/koala-icon.ico":
                    resource = this.getFavicon();

                    response.addHeader("Content-Type", "image/x-icon");
                    response.setBody(resource);

                    System.out.println("Serving favicon image...");
                    break;

                case "/css/search-facade.css":
                    resource = this.getCSS();

                    response.addHeader("Content-Type", "text/css");
                    response.setBody(resource);

                    System.out.println("Serving css file...");
                    break;

                case "/users", "/users/":
                    resource = this.getUsers();

                    response.addHeader("Content-Type", "text/html");
                    response.setBody(resource);

                    System.out.println("Serving all user entries...");
                    break;

                case "/users/search", "/users/search/":
                    response.addHeader("Content-Type", "text/html");

                    if (parameters == null) {
                        resource = this.getUsers();

                        response.setBody(resource);

                        System.out.println("No filters found, serving all user entries...");
                        break;
                    }

                    filters = HttpHandler.parseParameters(parameters);

                    if (schema_manager.validateFilters(filters)) {
                        resource = this.getUsersBySearch(filters);
                    } else {
                        resource = this.getUsers();
                    }

                    response.setBody(resource);

                    System.out.println("Serving user entries by filter " + filters + "...");
                    break;

                case "/events", "/events/":
                    resource = this.getEvents();

                    response.addHeader("Content-Type", "text/html");
                    response.setBody(resource);

                    System.out.println("Serving all event entries...");
                    break;

                case "/events/search", "/events/search/":
                    response.addHeader("Content-Type", "text/html");

                    if (parameters == null) {
                        resource = this.getEvents();

                        response.setBody(resource);

                        System.out.println("No filters found, serving all event entries...");
                        break;
                    }

                    filters = HttpHandler.parseParameters(parameters);

                    if (schema_manager.validateFilters(filters)) {
                        resource = this.getEventsBySearch(filters);
                    } else {
                        resource = this.getEvents();
                    }

                    response.setBody(resource);

                    System.out.println("Serving event entries by filter " + filters + "...");
                    break;
            }

            if (response.getBody() == null) {
                if (resource_path.matches("/users/.+")) {
                    String id = resource_path.split("/")[2];

                    resource = this.getUsersById(id);

                    response.addHeader("Content-Type", "text/html");
                    response.setBody(resource);

                    System.out.println("Serving user entries by id...");
                }
                else if (resource_path.matches("/events/.+")) {
                    String id = resource_path.split("/")[2];

                    resource = this.getEventsById(id);

                    response.addHeader("Content-Type", "text/html");
                    response.setBody(resource);

                    System.out.println("Serving event entries by id...");
                }
                else {
                    resource = "Requested Page Not Found".getBytes();

                    response.setStatus(HttpStatus.CLIENT_ERROR_404_PAGE_NOT_FOUND);
                    response.addHeader("Content-Type", "text/plain");
                    response.setBody(resource);

                    System.out.println("Unknown request, serving 404 Error...");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return response;
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
        var users = this.user_repository.findAll();
        var resource = User.usersToJson(users);
        var response = HtmlCrafter.insertToTemplate(resource);

        return response.getBytes();
    }

    public byte[] getUsersById(String id) {
        var resource = this.user_repository.findById(id);

        if (resource != null) {
            var response = HtmlCrafter.insertToTemplate(resource.toString());

            return response.getBytes();
        }

        return "".getBytes();
    }
    
    public byte[] getUsersBySearch(List<Document> filters) {
        var users = this.user_repository.findByFilters(filters);
        var resource = User.usersToJson(users);
        var response = HtmlCrafter.insertToTemplate(resource);

        return response.getBytes();
    }

    public byte[] getEvents() {
        var events = this.event_repository.findAll();var resource = Event.eventsToJson(events);
        var response = HtmlCrafter.insertToTemplate(resource);

        return response.getBytes();
    }

    public byte[] getEventsById(String id) {
        var resource = this.event_repository.findById(id);

        if (resource != null) {
            var response = HtmlCrafter.insertToTemplate(resource.toString());

            return response.getBytes();
        }

        return "".getBytes();
    }

    public byte[] getEventsBySearch(List<Document> filters) {
        var events = this.event_repository.findByFilters(filters);
        var resource = Event.eventsToJson(events);
        var response = HtmlCrafter.insertToTemplate(resource);

        return response.getBytes();
    }

}
