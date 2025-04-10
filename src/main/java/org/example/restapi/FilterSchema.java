package org.example.restapi;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;

import org.bson.Document;

public class FilterSchema {

    private Schema schema;

    public FilterSchema() {
        var raw_schema = this.readJSONFile("FilterSchema.json");
        this.schema = SchemaLoader.load(raw_schema);
    }

    public JSONObject readJSONFile(String file_name) {
        try (var input_stream = FilterSchema.class.getResourceAsStream("/" + file_name);) {
            var reader = new BufferedReader(new InputStreamReader(input_stream));
            
            var content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            var raw_schema = new JSONObject(content.toString());

            return raw_schema;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public boolean validateFilter(Document filter) {
        try {
            this.schema.validate(new JSONObject(filter.toJson()));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
