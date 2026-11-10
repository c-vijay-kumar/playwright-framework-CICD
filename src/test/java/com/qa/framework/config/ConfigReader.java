package com.qa.framework.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        String environment = System.getProperty("env", "qa");
        String filePath = "config/" + environment + ".properties";

        try (InputStream input = ConfigReader.class
                .getClassLoader().getResourceAsStream(filePath)) {

            if (input == null) {
                throw new RuntimeException("Configuration file not found: " + filePath);
            }
            props.load(input);
            System.out.println("Running on environment: " + environment);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public static String get(String key) {
        // -D system property wins over the file, so CI can override anything
        return System.getProperty(key, props.getProperty(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}