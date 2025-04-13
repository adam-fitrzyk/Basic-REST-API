package org.example.searchfacade.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileHandler {

    public static byte[] getFileAsBytes(String file_name) {
        try (var input_stream = FileHandler.class.getResourceAsStream("/" + file_name)) {
            var buffered_input_stream = new BufferedInputStream(input_stream);
            var output_stream = new ByteArrayOutputStream();

            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = buffered_input_stream.read(buffer)) >= 0) {
                output_stream.write(buffer, 0, count);
            }

            return output_stream.toByteArray();
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

}
