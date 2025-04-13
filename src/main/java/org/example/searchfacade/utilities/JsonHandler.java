package org.example.searchfacade.utilities;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonHandler {

    public static JSONObject parseJSONFile(String file_name) {
        try (var input_stream = JsonHandler.class.getResourceAsStream("/" + file_name);) {
            var reader = new BufferedReader(new InputStreamReader(input_stream));

            var content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return new JSONObject(content.toString());
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

}
