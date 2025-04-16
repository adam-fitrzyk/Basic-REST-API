package org.example.searchfacade;

public class Application {

    private static final String CONFIG_FILE_NAME = "config.json";

    public static void main(String[] args) {
        System.out.println("* Welcome to -<{ Search Facade }>- !");

        var server = new HttpServer(CONFIG_FILE_NAME);
        server.run();
    }

}
