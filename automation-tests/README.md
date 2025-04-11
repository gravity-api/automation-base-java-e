# JUnit Console API

## **Step-by-Step Guide**

To create the `jar` open the terminal and run:

```bash
mvn clean package -DskipTests
```

### **1. Modify the Execution Command**

Instead of using the `-jar` option with `--scan-classpath`, use the `-cp` option to set the classpath and specify the `ConsoleLauncher` as the main class. This allows you to use selectors like `--select-method` without conflicts.

**Command Structure:**

```bash
java -cp <path-to-jar> <main-class> <options>
```

**For Your Project:**

Assuming your shaded JAR is located at `target/junit-cmd-tests-1.0-SNAPSHOT-all.jar` and you want to run the `testAdd` method in the `CalculatorTest` class, the command would be:

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --details verbose
```

### **2. Example Commands**

#### **a. Run a Single Test Method**

To run a specific test method (`testAdd`):

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --details verbose
```

#### **b. Run Multiple Test Methods**

To run multiple specific test methods (`testAdd` and `testSubtract`):

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --select-method com.automation.CalculatorTest#testSubtract --details verbose
```

#### **c. Run All Tests in a Specific Class**

To run all tests within the `CalculatorTest` class:

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-class com.automation.CalculatorTest --details verbose
```

### **3. Explanation of Command Components**

- **`java -cp target/junit-cmd-tests-1.0-SNAPSHOT-all.jar`:**
  - **`-cp`**: Sets the classpath to include your shaded JAR, ensuring all classes and dependencies are accessible.
  
- **`org.junit.platform.console.ConsoleLauncher`:**
  - Specifies the main class to execute, which is JUnit's Console Launcher.

- **`--select-method com.automation.CalculatorTest#testAdd`:**
  - **`--select-method`**: Directs JUnit to run the specified test method.
  - **`com.automation.CalculatorTest#testAdd`**: Fully qualified name of the test class and method.

- **`--details verbose`:**
  - Provides detailed output of the test execution.

### **4. Running the Command**

Open your terminal or command prompt, navigate to your project directory, and execute the desired command. For example:

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --details verbose
```

### **5. Additional Tips**

- **Ensure Correct Class and Method Names:**
  - Verify that the class and method names are **fully qualified** and **exactly match** those in your code.
  
- **Handling Special Characters:**
  - If your method names or class names contain special characters, consider enclosing the selector in quotes:
  
    ```bash
    --select-method "com.automation.CalculatorTest#testAdd"
    ```

- **Listing Available Tests:**
  - Before running specific tests, you can list all discovered tests to ensure they are correctly recognized:
  
    ```bash
    java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --scan-classpath target/junit-cmd-cases-1.0-SNAPSHOT-all.jar --list-cases --details verbose
    ```
  
  - **Note:** Avoid using `--scan-classpath` with explicit selectors to prevent the aforementioned error.

### **6. Alternative Approach: Using a Custom Runner Class**

If you prefer not to specify the classpath each time or want a more streamlined command, you can create a custom runner class that encapsulates the Console Launcher invocation.

**a. Create `TestRunner.java`:**

```java
package com.automation;

import org.junit.platform.console.ConsoleLauncher;

public class TestRunner {
    public static void main(String[] args) {
        ConsoleLauncher.main(new String[]{
            "--select-method", "com.automation.CalculatorTest#testAdd",
            "--details", "verbose"
        });
    }
}
```

**b. Update the Maven Shade Plugin's `mainClass`:**

In your `pom.xml`, set the `mainClass` to your custom runner:

```xml
<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
    <mainClass>com.automation.TestRunner</mainClass>
</transformer>
```

**c. Rebuild the Project:**

```bash
mvn clean package
```

**d. Run the JAR Without Additional Classpath Specification:**

```bash
java -jar target/junit-cmd-cases-1.0-SNAPSHOT-all.jar
```

**Benefits:**

- **Simplified Execution:** No need to specify the classpath or test selectors each time.
- **Flexibility:** You can modify `TestRunner.java` to accept dynamic arguments or configure different test selectors as needed.

### **7. Summary of Commands**

Here are the primary commands you can use based on your requirements:

#### **a. Run a Specific Test Method:**

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --details verbose
```

#### **b. Run Multiple Specific Test Methods:**

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-method com.automation.CalculatorTest#testAdd --select-method com.automation.CalculatorTest#testSubtract --details verbose
```

#### **c. Run All Tests in a Specific Class:**

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --select-class com.automation.CalculatorTest --details verbose
```

#### **d. List All Discovered Tests:**

```bash
java -cp target/junit-cmd-cases-1.0-SNAPSHOT-all.jar org.junit.platform.console.ConsoleLauncher --scan-classpath target/junit-cmd-cases-1.0-SNAPSHOT-all.jar --list-cases --details verbose
```

*(Ensure not to combine `--scan-classpath` with selectors like `--select-method` to avoid errors.)*