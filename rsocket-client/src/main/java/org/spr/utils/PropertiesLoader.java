package org.spr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static Properties props = null;
    public static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
          .getClassLoader()
          .getResourceAsStream("application.properties");
        configuration.load(inputStream);
        inputStream.close();
        props = configuration;
        return configuration;
    }

    public static String getProperty(String name) {
        if (props == null) {
            try {
                loadProperties();
            } catch (IOException e) {
                props = new Properties();
            }
        }

        return props.getProperty(name);
    }
}