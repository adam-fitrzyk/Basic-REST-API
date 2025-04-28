package org.example.searchfacade;

import org.example.searchfacade.config.ConfigurationManager;
import org.example.searchfacade.core.HttpConnectionWorkerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private final String config_file_name;

    private final Logger logger;

    public HttpServer(String config_file_name) {
        this.config_file_name = config_file_name;
        this.logger = LoggerFactory.getLogger(HttpServer.class);
    }

    public void run() {
        var conf = ConfigurationManager.getInstance();
        conf.loadConfigurationFile(config_file_name);

        int port = conf.getConfiguration().port();

        logger.info("> Using port: {}", port);
        logger.info("> Using schema: {}", conf.getConfiguration().filter_schema_name());

        try {
            var serverSocket = new ServerSocket(port);
            logger.info("> Listening on port {}", conf.getConfiguration().port());

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                // Begin listening and wait for any requests
                Socket socket = serverSocket.accept();

                // Program will run once request has been detected
                logger.info("> New client connected");

                var workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
        } 
        catch (IOException e) {
            logger.warn("Encountered error whilst running Sockets", e);
        }
    }

}
