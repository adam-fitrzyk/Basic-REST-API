package org.example.searchfacade.utilities;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHandler {

    private static final Logger logger = LoggerFactory.getLogger(JsonHandler.class);

    public static JSONObject parseJSONFile(String file_name) {
        try (var input_stream = JsonHandler.class.getResourceAsStream("/" + file_name)) {
            if (input_stream == null) {
                throw new NullPointerException("Input stream is null, json file not found");
            }
            var reader = new BufferedReader(new InputStreamReader(input_stream));

            var content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return new JSONObject(content.toString());
        }
        catch (IOException e) {
            logger.error("Encountered error whilst parsing json file", e);
            return null;
        }
        catch (NullPointerException e) {
            logger.error("Json file provided not found", e);
            return null;
        }
    }

}
