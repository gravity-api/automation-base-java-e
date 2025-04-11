package io.automation.examples.suites;

import io.automation.engine.models.TestResultModel;
import io.automation.examples.cases.CGPP123456;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Story("The user story name from Jira")
@DisplayName("Expressive name of the test suite")
class UserStoryTests {
    @Test
    @Tag("UserStory")
    @Tag("Sanity")
    @Tag("System")
    @DisplayName("Some expression name")
    void TGPP123456Test() {
        CGPP123456 testCase = new CGPP123456();
        TestResultModel result = testCase.invoke();
        Assertions.assertTrue(result.isPassed());
    }
}
