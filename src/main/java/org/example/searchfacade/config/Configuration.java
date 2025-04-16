package org.example.searchfacade.config;

import org.json.JSONObject;

public record Configuration (
        int port,
        String webroot,
        String filter_schema_name
) {

    public static Configuration fromJson(JSONObject json) {
        var port = json.getInt("port");
        var webroot = json.getString("webroot");
        var schema_name = json.getString("filter_schema_name");

        return new Configuration(port, webroot, schema_name);
    }

}
