package io.automation.engine.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents an automation environment for testing purposes.
 */
public class AutomationEnvironment {
    private final Map<String, Object> contextProperties;
    private final Map<String, Object> testData;
    private final Map<String, Object> testParameters;

    /**
     * Initializes a new instance of the AutomationEnvironment class.
     */
    public AutomationEnvironment() {
        // Use a TreeMap with case-insensitive ordering for keys to simulate C#'s StringComparer.OrdinalIgnoreCase.
        contextProperties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        testData = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        testParameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Gets the dictionary of context properties.
     *
     * @return the context properties map
     */
    public Map<String, Object> getContextProperties() {
        return contextProperties;
    }

    /**
     * Gets the dictionary of test data.
     *
     * @return the test data map
     */
    public Map<String, Object> getTestData() {
        return testData;
    }

    /**
     * Gets the dictionary of test parameters.
     *
     * @return the test parameters map
     */
    public Map<String, Object> getTestParameters() {
        return testParameters;
    }

    /**
     * Adds a test parameter to the environment's TestParameters map.
     *
     * @param key   the key of the test parameter.
     * @param value the value of the test parameter.
     * @return the updated {@link AutomationEnvironment} instance.
     */
    public AutomationEnvironment addTestParameter(String key, Object value) {
        // Add or update the key-value pair in the testParameters map.
        testParameters.put(key, value);

        // Return the current instance of AutomationEnvironment for method chaining.
        return this;
    }

    /**
     * Adds test parameters to the environment's TestParameters map from a JSON string.
     *
     * @param json        the JSON string containing the test parameters.
     * @return the updated {@link AutomationEnvironment} instance.
     */
    public AutomationEnvironment addTestParameter(String json) {
        // Create a Gson instance.
        Gson gson = new Gson();

        // Define the type for deserialization.
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        try {
            // Deserialize the JSON string into a Map.
            Map<String, Object> deserialized = gson.fromJson(json, mapType);

            // Add each key-value pair to the TestParameters map.
            if (deserialized != null) {
                testParameters.putAll(deserialized);
            }
        } catch (Exception e) {
            // Silently catch any exceptions that occur during deserialization.
        }

        // Return the updated AutomationEnvironment instance.
        return this;
    }

    /**
     * Adds or updates a context property in the environment's ContextProperties map.
     *
     * @param key   the key of the context property to add or update.
     * @param value the value of the context property to add or update.
     * @return the updated {@link AutomationEnvironment} instance for method chaining.
     */
    public AutomationEnvironment addContextProperty(String key, Object value) {
        // Add or update the key-value pair in the contextProperties map.
        contextProperties.put(key, value);

        // Return the current instance of AutomationEnvironment for method chaining.
        return this;
    }

    /**
     * Adds or updates context properties in the environment's ContextProperties map from a JSON string.
     *
     * @param json the JSON string containing the context properties to add or update.
     * @return the updated {@link AutomationEnvironment} instance for method chaining.
     */
    public AutomationEnvironment addContextProperty(String json) {
        // Create a Gson instance.
        Gson gson = new Gson();

        // Define the type for deserialization.
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        try {
            // Deserialize the JSON string into a Map.
            Map<String, Object> deserialized = gson.fromJson(json, mapType);

            // Add each key-value pair to the ContextProperties map.
            if (deserialized != null) {
                contextProperties.putAll(deserialized);
            }
        } catch (Exception e) {
            // Silently catch any exceptions that occur during deserialization.
        }

        // Return the updated AutomationEnvironment instance.
        return this;
    }
}
