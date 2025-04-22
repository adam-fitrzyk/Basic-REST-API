package org.example.searchfacade.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;

import static java.net.URLDecoder.decode;

import org.bson.Document;
import org.bson.json.JsonParseException;

public class HttpHandler {

    public static HttpRequest parseRequest(InputStream in) {
        try {
            var request = new HttpRequest();

            // Read request line by line
            var reader = new BufferedReader(new InputStreamReader(in));
            var request_arr = new ArrayList<String>();
            String request_line = reader.readLine();
            if (request_line == null) {
                return null;
            }
            while (!request_line.isEmpty()) {
                request_arr.add(request_line);
                request_line = reader.readLine();
            }

            var components = request_arr.getFirst().split(" ");

            request.setMethod(components[0]);
            var URL = components[1].split("\\?");
            request.setTarget(URL[0]);
            if (URL.length > 1) {
                request.setParameters(URL[1]);
            }
            request.setProtocol(components[2]);

            return request;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<Document> parseParameters(String parameters) {
        /* Parameters are JSON formatted filters and should follow the following structure (specified in filter schema):
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
                filters.add(Document.parse(decode(filter_string, UTF_8)));
            }
        }
        catch (JsonParseException e) {
            assert true;
        }

        return filters;
    }

    public static HttpStatus validateRequest(HttpRequest request) {
        // TODO implement HEAD request
        if (!request.getMethod().equals("GET")) {
            return HttpStatus.CLIENT_ERROR_401_METHOD_NOT_ALLOWED;
        }
        if (!request.getProtocol().equals("HTTP/1.1")) {
            return HttpStatus.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED;
        }
        if (request.getTarget().contains("..")) {
            return HttpStatus.CLIENT_ERROR_403_FORBIDDEN;
        }
        return HttpStatus.SUCCESS_200_OK;
    }

}
