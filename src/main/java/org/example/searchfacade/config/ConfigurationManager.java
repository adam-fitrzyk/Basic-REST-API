package org.example.searchfacade.config;

import org.example.searchfacade.utilities.JsonHandler;

public class ConfigurationManager {

    private static ConfigurationManager INSTANCE;
    private static Configuration configuration;

    private ConfigurationManager() { };

    public static ConfigurationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigurationManager();
        }
        return INSTANCE;
    }

    public void loadConfigurationFile(String file_name) {
        var conf = JsonHandler.parseJSONFile(file_name);
        configuration = Configuration.fromJson(conf);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
