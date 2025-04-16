package org.example.searchfacade;

import org.example.searchfacade.config.ConfigurationManager;
import org.example.searchfacade.core.HttpConnectionWorkerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private String config_file_name;

    public HttpServer(String config_file_name) {
        this.config_file_name = config_file_name;
    }

    public void run() {
        var conf = ConfigurationManager.getInstance();
        conf.loadConfigurationFile(config_file_name);

        int port = conf.getConfiguration().getPort();

        System.out.println("> Using port: " + port);
        System.out.println("> Using schema: " + conf.getConfiguration().getFilterSchemaName());

        try {
            var serverSocket = new ServerSocket(port);
            System.out.println("> Listening on port " + conf.getConfiguration().getPort());

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                // Begin listening on port 9999 and wait for any requests
                Socket socket = serverSocket.accept();

                // Program will run once request has been detected
                System.out.println("> New client connected");

                var workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
            // serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
