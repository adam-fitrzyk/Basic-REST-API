package org.example.restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public int port;

    public Server(int port) {
        this.port = port;
    }
    
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("> Listening on port " + this.port);
            
            while (true) {
                // Begin listening on port 9999 and wait for any requests
                Socket socket = serverSocket.accept();
                // Program will run once request has been detected
                System.out.println("> New client connected");

                // Get input stream to read request
                var input = socket.getInputStream();
                var reader = new BufferedReader(new InputStreamReader(input));

                // Get output stream to send back a response
                var output = socket.getOutputStream();
                var writer = new PrintWriter(output, true);

                // Read request line by line and save in variable request
                var request = new ArrayList<String> ();
                String request_line = reader.readLine();

                if (request_line == null) {
                    reader.close();
                    writer.close();
                    socket.close();
                } else {
                    while (!request_line.isEmpty()) {
                        System.out.println(request_line);
                        request.add(request_line);
                        request_line = reader.readLine();
                    }
                    System.out.println();

                    var components = RESTController.parseRequest(request);
                    var resource_path = components[1];
                    var parameters = components[2];
                    var status = RESTController.validateRequest(components);

                    if (status != 200) {
                        writer.println("HTTP/1.1 " + status + " Error\r\n\r\n");
                        writer.println("Error: " + status);
                    } else {
                        var response = RESTController.getResource(resource_path, parameters);
                        writer.println(response);
                    }

                    // Send out response and close connection
                    reader.close();
                    writer.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
