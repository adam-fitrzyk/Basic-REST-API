package org.example.searchfacade;

import org.slf4j.LoggerFactory;

public class Application {

    private static final String CONFIG_FILE_NAME = "config.json";

    public static void main(String[] args) {
        var logger = LoggerFactory.getLogger(Application.class);
        logger.info("* Welcome to -<{ Search Facade }>- !");

        var server = new HttpServer(CONFIG_FILE_NAME);
        server.run();
    }

}
