package org.example.searchfacade;

import org.example.searchfacade.config.ConfigurationManager;

public class Application {

    private static final String config_file_name = "config.json";

    public static void main(String[] args) {
        System.out.println("Welcome to -<{ Search Facade }>- !");

        var conf = ConfigurationManager.getInstance();
        conf.loadConfigurationFile(config_file_name);

        System.out.println("Using port: " + conf.getConfiguration().getPort());
        System.out.println("Using schema: " + conf.getConfiguration().getFilterSchemaName());

        var server = new HTTPServer(conf.getConfiguration().getPort());
        server.run();
    }

}
