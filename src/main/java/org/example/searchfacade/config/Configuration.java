package org.example.searchfacade.config;

import org.json.JSONObject;

public class Configuration {

    private int port;
    private String filter_schema_name;

    public Configuration(int port, String schema_name) {
        this.port = port;
        this.filter_schema_name = schema_name;
    }

    public int getPort() {
        return port;
    } public String getFilterSchemaName() {
        return filter_schema_name;
    }

    public static Configuration fromJson(JSONObject json) {
        var port = json.getInt("port");
        var schema_name = json.getString("filter_schema_name");

        return new Configuration(port, schema_name);
    }

}
