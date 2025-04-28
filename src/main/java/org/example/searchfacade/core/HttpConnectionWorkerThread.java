package org.example.searchfacade.core;

import org.example.searchfacade.RestController;
import org.example.searchfacade.http.HttpResponse;
import org.example.searchfacade.http.HttpHandler;
import org.example.searchfacade.http.HttpStatus;

import java.io.IOException;

import java.net.Socket;

import java.lang.Thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectionWorkerThread extends Thread {

    private final Socket socket;
    private final RestController controller;

    private final Logger logger;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
        this.controller = new RestController();
        this.logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    }

    @Override
    public void run() {
        try {
            // Get input stream to read request
            var input = socket.getInputStream();

            // Get output stream to send back a response
            var output = socket.getOutputStream();

            try {
                var request = HttpHandler.parseRequest(input);
                var response = new HttpResponse();

                if (request == null) {
                    response.setProtocol("HTTP/1.1");
                    response.setStatus(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST);

                    output.write(response.getBytes());
                    return;
                }

                var status = HttpHandler.validateRequest(request);

                if (status.STATUS_CODE != 200) {
                    response.setProtocol("HTTP/1.1");
                    response.setStatus(status);
                    response.setBody((status.STATUS_CODE + " Error: " + status.REASON_PHRASE).getBytes());
                } else {
                    response = this.controller.getResponse(request.getTarget(), request.getParameters());
                }

                output.write(response.getBytes());
                output.flush();
            }
            catch (Exception e) {
                var response = new HttpResponse();
                response.setProtocol("HTTP/1.1");
                response.setStatus(HttpStatus.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);

                output.write(response.getBytes());
                output.flush();

                logger.error("Encountered error whilst handling HttpRequest and sending HttpResponse", e);
            }

            input.close();
            output.close();
            socket.close();
        }
        catch (IOException e) {
            logger.error("Encountered error whilst fetching input and output streams of connection", e);
        }
    }

}
