package org.example.searchfacade.config;

import org.json.JSONObject;

public class Configuration {

    private int port;
    private String webroot;
    private String filter_schema_name;

    public Configuration(int port, String webroot, String schema_name) {
        this.port = port;
        this.webroot = webroot;
        this.filter_schema_name = schema_name;
    }

    public int getPort() {
        return port;
    } public String getWebroot() {
        return webroot;
    } public String getFilterSchemaName() {
        return filter_schema_name;
    }

    public static Configuration fromJson(JSONObject json) {
        var port = json.getInt("port");
        var webroot = json.getString("webroot");
        var schema_name = json.getString("filter_schema_name");

        return new Configuration(port, webroot, schema_name);
    }

}
