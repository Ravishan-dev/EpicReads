package com.ravishandev.epicreads.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Env {

    private static final Properties APP_PROPERTIES = new Properties();

    static {
        try {
            InputStream inputStream = Env.class.getClassLoader().getResourceAsStream("app.properties");
            APP_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Application Properties Loading Failed: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return APP_PROPERTIES.getProperty(key);
    }

    public static Properties getAppProperties() {
        return APP_PROPERTIES;
    }
}
