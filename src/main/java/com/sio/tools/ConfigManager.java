package com.sio.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;


public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    public ConfigManager() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (FileNotFoundException e) {
            System.out.println("Config File not found: " + CONFIG_FILE);
            System.out.println(e.getMessage());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(getClass().getClassLoader().getResource(CONFIG_FILE).getPath())) {
            properties.store(output, "Updating application configuration");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
