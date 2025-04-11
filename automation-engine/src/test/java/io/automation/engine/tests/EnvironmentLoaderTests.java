package io.automation.engine.tests;

import io.automation.engine.core.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnvironmentLoaderTests {

    @Test
    void PublishEnvTest() {
        // Publish global environment properties from the .env file.
        Utilities.publishEnvironmentProperties(EnvironmentLoaderTests.class, ".env");

        // Retrieve the AUT (Application Under Test) identifier from system properties.
        String aut = System.getProperty("AUT");

        // Publish environment properties specific to the AUT.
        Utilities.publishEnvironmentProperties(EnvironmentLoaderTests.class, "aut/" + aut + ".env");

        // Retrieve the URL property for the AUT and verify it matches the expected value.
        String autUrl = System.getProperty("URL");
        Assertions.assertEquals("http://example.com", autUrl);
    }
}
