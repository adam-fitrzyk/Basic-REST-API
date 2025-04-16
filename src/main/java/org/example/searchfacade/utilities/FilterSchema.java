package org.example.searchfacade.utilities;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;

public record FilterSchema (Schema schema) {

    public static FilterSchema fromJson(JSONObject json) {
        var schema = SchemaLoader.load(json);

        return new FilterSchema(schema);
    }

}
