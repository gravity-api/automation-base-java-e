package io.automation.engine.tests;

import io.automation.engine.core.DriverFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

class DriverFactoryTests {
    private static final String REMOTE_SERVER_ADDRESS = "http://host.k8s.internal:4444/wd/hub";
    private static final String AUT = "https://www.example.com";
    private static final String EXPECTED_TITLE = "Example Domain";

    @Test
    void InitializeRemoteEdgeDriverTest() {
        WebDriver driver = null;
        try {
            driver = DriverFactory.start(DriverFactory.Drivers.EDGE, REMOTE_SERVER_ADDRESS);
            driver.get(AUT);
            String title = driver.getTitle();
            Assertions.assertEquals(EXPECTED_TITLE, title);
        } catch (Exception e) {
            Assertions.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    void InitializeRemoteChromeDriverTest() {
        WebDriver driver = null;
        try {
            driver = DriverFactory.start(DriverFactory.Drivers.CHROME, REMOTE_SERVER_ADDRESS);
            driver.get(AUT);
            String title = driver.getTitle();
            Assertions.assertEquals(EXPECTED_TITLE, title);
        } catch (Exception e) {
            Assertions.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    void InitializeLocalChromeDriverTest() {
        WebDriver driver = null;
        try {
            driver = DriverFactory.start(DriverFactory.Drivers.CHROME);
            driver.get("https://www.example.com");
            String title = driver.getTitle();
            Assertions.assertEquals(EXPECTED_TITLE, title);
        } catch (Exception e) {
            Assertions.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    void InitializeLocalDefaultDriverTest() {
        WebDriver driver = null;
        try {
            driver = DriverFactory.start();
            driver.get("https://www.example.com");
            String title = driver.getTitle();
            Assertions.assertEquals(EXPECTED_TITLE, title);
        } catch (Exception e) {
            Assertions.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
