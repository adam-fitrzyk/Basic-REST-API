package org.example.searchfacade.config;

import org.json.JSONObject;

public record Configuration (
        int port,
        String webroot,
        String filter_schema_name,
        String mongodb_connection_string,
        String mongodb_name
) {

    public static Configuration fromJson(JSONObject json) {
        var port = json.getInt("port");
        var webroot = json.getString("webroot");
        var schema_name = json.getString("filter_schema_name");
        var mongodb_connection_string = json.getString("mongodb_connection_string");
        var mongodb_name = json.getString("mongodb_name");

        return new Configuration(port, webroot, schema_name, mongodb_connection_string, mongodb_name);
    }

}
