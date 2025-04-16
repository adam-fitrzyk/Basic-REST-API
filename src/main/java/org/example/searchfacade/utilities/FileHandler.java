package org.example.searchfacade.utilities;

import org.example.searchfacade.config.ConfigurationManager;
import org.example.searchfacade.config.Configuration;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileHandler {

    private static final Configuration conf = ConfigurationManager.getInstance().getConfiguration();

    public static byte[] getFileAsBytes(String file_name) {
        try (var in = FileHandler.class.getResourceAsStream(conf.getWebroot() + "/" + file_name)) {
            if (in == null) {
                throw new IOException("Inputstream of given file is null");
            }
            var out = new ByteArrayOutputStream();

            final byte[] buffer = new byte[0x10000];
            int n = 0;
            while ((n = in.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            return out.toByteArray();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
