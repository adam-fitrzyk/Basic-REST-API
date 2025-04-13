package org.example.searchfacade.utilities;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;

public class FilterSchema {

    private Schema schema;

    public FilterSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    public static FilterSchema fromJson(JSONObject json) {
        var schema = SchemaLoader.load(json);

        return new FilterSchema(schema);
    }

}
