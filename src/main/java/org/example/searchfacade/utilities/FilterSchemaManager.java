package org.example.searchfacade.utilities;

import org.example.searchfacade.config.ConfigurationManager;

import org.bson.Document;
import org.json.JSONObject;

public class FilterSchemaManager {

    private static FilterSchemaManager instance;

    private FilterSchema filter_schema;

    private FilterSchemaManager() {
        var schema_file_name = ConfigurationManager.getInstance().getConfiguration().filter_schema_name();
        this.loadSchema(schema_file_name);
    }

    public static FilterSchemaManager getInstance() {
        if (instance == null) {
            instance = new FilterSchemaManager();
        }
        return instance;
    }

    public void loadSchema(String schema_file_name) {
        var raw_schema = JsonHandler.parseJSONFile(schema_file_name);
        this.filter_schema = FilterSchema.fromJson(raw_schema);
    }

    public boolean validateFilter(Document filter) {
        try {
            filter_schema.schema().validate(new JSONObject(filter.toJson()));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
