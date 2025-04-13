package org.example.searchfacade.utilities;

import org.example.searchfacade.config.ConfigurationManager;

import org.bson.Document;
import org.json.JSONObject;

public class FilterSchemaManager {

    private static FilterSchemaManager INSTANCE;

    private FilterSchema filter_schema;

    private FilterSchemaManager() {
        var schema_file_name = ConfigurationManager.getInstance().getConfiguration().getFilterSchemaName();
        this.loadSchema(schema_file_name);
    }

    public static FilterSchemaManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FilterSchemaManager();
        }
        return INSTANCE;
    }

    public void loadSchema(String schema_file_name) {
        var raw_schema = JsonHandler.parseJSONFile(schema_file_name);
        this.filter_schema = FilterSchema.fromJson(raw_schema);
    }

    public boolean validateFilter(Document filter) {
        try {
            filter_schema.getSchema().validate(new JSONObject(filter.toJson()));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
