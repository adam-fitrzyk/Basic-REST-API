package org.example.searchfacade.core;

import org.example.searchfacade.RestController;
import org.example.searchfacade.utilities.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.ArrayList;

import java.lang.Thread;
import java.lang.Override;

public class HttpConnectionWorkerThread extends Thread {

    private Socket socket;
    private RestController controller;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
        this.controller = new RestController();
    }

    @Override
    public void run() {
        try {
            // Get input stream to read request
            var input = socket.getInputStream();
            var reader = new BufferedReader(new InputStreamReader(input));

            // Get output stream to send back a response
            var output = socket.getOutputStream();
            var writer = new PrintWriter(output, true);

            // Read request line by line and save in variable request
            var request = new ArrayList<String>();
            String request_line = reader.readLine();

            if (request_line == null) {
                reader.close();
                writer.close();
                socket.close();
            } else {
                while (!request_line.isEmpty()) {
                    request.add(request_line);
                    request_line = reader.readLine();
                }

                var components = RequestHandler.parseRequest(request);
                var status = RequestHandler.validateRequest(components);

                if (status != 200) {
                    writer.println("HTTP/1.1 " + status + " Error\r\n\r\n");
                    writer.println("Error: " + status);
                } else {
                    var resource_path = components[1];
                    var parameters = components[2];
                    var response = this.controller.getResource(resource_path, parameters);
                    output.write(response);
                    output.flush();
                }

                // Send out response and close connection
                reader.close();
                writer.close();
                socket.close();
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}
