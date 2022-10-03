package io.thrive.fs.ui.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UiLoginTest {
    private String baseUrl = "https://fluency-strikers.dev.thrive.io/";
    private static WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        //Set implicit wait:
        //wait for WebElement
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));
        //wait for loading page
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(10));
        //wait for an asynchronous script to finish execution
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void test() {
        driver.get(baseUrl + "auth");
        driver.findElement(By.id("username")).sendKeys("vladimir.pavlyukov@thrive.io");
        driver.findElement(By.id("password")).sendKeys("Aa@45678");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement btn = driver.findElement(By.className("btn"));
        wait.until(ExpectedConditions.elementToBeClickable(btn));
        btn.click();
        String expectedUrl = "https://fluency-strikers.dev.thrive.io/";
        
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

    }
}
