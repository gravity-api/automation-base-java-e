package io.automation.examples.suites;

import io.automation.engine.core.TestSuiteBase;
import io.automation.examples.cases.C0005;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@SuppressWarnings("squid:S5960")
@DisplayName("Login Tests for User Authentication")
@Feature("User Authentication")
public class PracticeTests extends TestSuiteBase {
    @Test
    @Tag("practice")
    @DisplayName("Login Test with predefined credentials")
    public void t0005(){
        new C0005().invoke();
    }
}
