package pl.put.poznan.checker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Scenario Quality Checker Spring Boot application.
 * It initializes the Spring application context and scans for REST controllers
 * within the specified base packages.
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.checker.rest"})
public class ScenarioQualityCheckerApplication {

    /**
     * The main method that bootstraps and launches the Spring Boot application.
     * @param args Command line arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(ScenarioQualityCheckerApplication.class, args);
    }
}
