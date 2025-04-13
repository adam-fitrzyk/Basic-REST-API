package org.example.searchfacade.utilities;

import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;

import java.io.UnsupportedEncodingException;

import org.bson.Document;
import org.bson.json.JsonParseException;

public class RequestHandler {

    private static FilterSchemaManager schema_manager = FilterSchemaManager.getInstance();

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
                String filter_string = pair.split("=")[1];
                filters.add(Document.parse(decode(filter_string, "UTF-8")));
            }
        } 
        catch (JsonParseException | UnsupportedEncodingException e) {
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
            if (!schema_manager.validateFilter(filter)) {
                return false;
            }
        }
        return true;
    }

}
