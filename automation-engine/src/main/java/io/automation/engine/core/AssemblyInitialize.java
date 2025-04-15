package io.automation.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;

/**
 * The `AssemblyInitialize` class is responsible for initializing and managing
 * global environment properties and configurations for the automation framework.
 * It provides utility methods to retrieve and set up environment properties,
 * including integration with AllureExtensions reporting.
 */
public class AssemblyInitialize {
    // This constant holds the epoch time in milliseconds as a String, set once when the class is loaded.
    public static final String TEST_RUN_ID = Long.toString(Instant.now().toEpochMilli());

    // A global, mutable map for storing String keys and Object values.
    // Each reference to AssemblyInitialize.GLOBAL_MAP will point to the same map.
    private static Map<String, Object> globalEnvironment;

    // Logger for the AssemblyInitialize class, used for logging messages at the class level.
    private static final Logger logger = LoggerFactory.getLogger(AssemblyInitialize.class);

    // Private constructor prevents instantiation
    private AssemblyInitialize() {
        // This constructor is intentionally empty to prevent instantiation.
    }

    /**
     * Retrieves the global environment properties for the application.
     * This method initializes the `GLOBAL_ENVIRONMENT` map if it is null,
     * loads environment properties from the `.env` file, and adds the `TEST_RUN_ID`.
     *
     * @return A map containing the global environment properties.
     */
    public static Map<String, Object> getGlobalEnvironment() {
        // Check if the global environment map is not initialized.
        // Load environment properties from the `.env` file and assign them to the global map.
        if (globalEnvironment == null) {
            globalEnvironment = Utilities.publishEnvironmentProperties(AssemblyInitialize.class, ".env");
        }

        // Add the `TEST_RUN_ID` to the global environment map.
        globalEnvironment.put("TEST_RUN_ID", TEST_RUN_ID);

        // Set the `TEST_RUN_ID` in the system properties for AllureExtensions reporting.
        setAllureEnvironment();

        // Return the global environment map.
        return globalEnvironment;
    }

    /**
     * Sets the AllureExtensions environment properties by creating an `environment.properties` file
     * in the AllureExtensions results directory. This method ensures the directory exists, retrieves
     * global environment properties, and writes them to the file.
     */
    private static void setAllureEnvironment() {
        // Load AllureExtensions-specific properties from the "allure.properties" file.
        Map<String, Object> allureProperties =
                Utilities.publishEnvironmentProperties(AssemblyInitialize.class, "allure.properties");

        // Retrieve the AllureExtensions results directory path from the properties, defaulting to an empty string if not specified.
        String allurePath = allureProperties.getOrDefault("allure.results.directory", "").toString();

        // Ensure the AllureExtensions results directory exists by creating it if it does not already exist.
        Path allureDirPath = Path.of(allurePath);
        try {
            Files.createDirectories(allureDirPath);
        } catch (IOException e) {
            // Log an error if the directory creation fails.
            logger.error("Failed to create AllureExtensions results directory: {}", allurePath, e);
        }

        // Create a `Properties` object to store the global environment properties.
        Properties properties = new Properties();

        // Populate the `Properties` object with key-value pairs from the global environment map.
        for (Map.Entry<String, Object> entry : globalEnvironment.entrySet()) {
            if (entry.getValue() != null) {
                // Convert non-null values to strings and add them to the properties.
                properties.setProperty(entry.getKey(), entry.getValue().toString());
            }
        }

        // Build the file path for the `environment.properties` file in the AllureExtensions results directory.
        String filePath = allurePath.endsWith(File.separator)
                ? allurePath + "environment.properties"
                : allurePath + File.separator + "environment.properties";

        // Write the properties to the `environment.properties` file.
        try (OutputStream out = new FileOutputStream(filePath)) {
            properties.store(out, "AllureExtensions Environment Properties");
        } catch (IOException e) {
            // Log an error if writing to the file fails.
            logger.error("Failed to write AllureExtensions environment properties to file: {}", filePath, e);
        }
    }
}
