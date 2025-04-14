package org.example.searchfacade.config;

import org.example.searchfacade.utilities.JsonHandler;

public class ConfigurationManager {

    private static ConfigurationManager instance;
    private static Configuration configuration;

    private ConfigurationManager() { };

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void loadConfigurationFile(String file_name) {
        var conf = JsonHandler.parseJSONFile(file_name);
        configuration = Configuration.fromJson(conf);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
