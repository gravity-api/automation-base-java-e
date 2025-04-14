package io.automation.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import java.util.stream.Collectors;

/**
 * Utility class providing helper methods for common operations.
 * This class is not meant to be instantiated.
 */
@SuppressWarnings("unchecked")
public class Utilities {
    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    // Private constructor to prevent instantiation
    private Utilities() {
    }

    /**
     * Converts an object to a map.
     * This method performs an unchecked cast of the given object to a map with the specified key and value types.
     *
     * @param <K>    the type of keys in the map
     * @param <V>    the type of values in the map
     * @param object the object to be cast to a map
     *
     * @return the object cast as a map of type <K, V>
     *
     * @throws ClassCastException if the object cannot be cast to a map
     */
    public static <K, V> Map<K, V> convertToMap(Object object) {
        // Perform an unchecked cast of the object to a map.
        return (Map<K, V>) object;
    }

    /**
     * Loads environment variables from a resource file (.env) and sets them as system properties.
     *
     * @param cls          the class used to load the resource file
     * @param resourcePath the path to the .env file relative to the classpath (e.g., ".env")
     *
     * @return A map containing the loaded environment variables as key-value pairs.
     */
    public static Map<String, Object> publishEnvironmentProperties(Class<?> cls, String resourcePath) {
        Map<String, Object> environmentProperties = new HashMap<>();

        // Load the resource file as an InputStream using the class loader.
        try (InputStream inputStream = cls.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                log.error("Resource not found: {}", resourcePath);
                return environmentProperties;
            }

            // Read the resource file line by line.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Trim the line and skip empty lines or lines starting with a comment (#).
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }

                    // Split the line into key and value by the first '=' character.
                    int delimitedPosition = line.indexOf('=');
                    if (delimitedPosition != -1) {
                        String key = line.substring(0, delimitedPosition).trim();
                        String value = line.substring(delimitedPosition + 1).trim();

                        // Add the key-value pair to the map and set it as a system property.
                        environmentProperties.put(key, value);
                        System.setProperty(key, value);
                    }
                }
            }
        } catch (IOException e) {
            // Log any errors that occur while reading the resource file.
            log.error("Error reading resource file: {}", resourcePath, e);
        }

        // Return the map of environment properties.
        return environmentProperties;
    }

    /**
     * Retrieves the current environment variables as a map.
     * This method fetches all environment variables available to the JVM
     * and logs them for debugging purposes.
     *
     * @return A map containing the environment variables as key-value pairs.
     */
    public static Map<String, Object> getEnvironmentProperties() {
        // Initialize an empty map to store environment properties.
        Map<String, Object> environmentProperties = new HashMap<>();

        try {
            // Fetch environment variables and convert them to a map.
            Map<String, Object> environmentVariables = System
                    .getenv()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // Fetch system properties and convert them to a map.
            Map<String, Object> systemProperties = System.getProperties()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> (String) e.getKey(), Map.Entry::getValue));

            // Combine environment variables and system properties into a single map.
            environmentProperties.putAll(environmentVariables);
            environmentProperties.putAll(systemProperties);

            // Log each environment variable for debugging purposes.
            environmentProperties.forEach((key, value) -> log.debug("{} = {}", key, value));
        } catch (Exception e) {
            // Log any exceptions that occur while retrieving environment properties.
            log.error("Error retrieving environment properties: ", e);
        }

        // Return the map of environment properties.
        return environmentProperties;
    }
}
