package org.example.searchfacade.utilities;

import org.example.searchfacade.config.ConfigurationManager;
import org.example.searchfacade.config.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHandler {

    private static final Configuration conf = ConfigurationManager.getInstance().getConfiguration();

    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    public static byte[] getFileAsBytes(String relative_path) {
        try (var in = FileHandler.class.getResourceAsStream(conf.webroot() + "/" + relative_path)) {
            if (in == null) {
                throw new IOException("InputStream of given file is null");
            }
            var out = new ByteArrayOutputStream();

            final byte[] buffer = new byte[0x10000];
            int n;
            while ((n = in.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            return out.toByteArray();
        }
        catch (IOException e) {
            logger.error("Encountered error whilst fetching file data as bytes", e);
            return null;
        }
    }

}
