package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.thrive.fs.ui.pages.fs.ui.CommissionsPage;
import io.thrive.fs.ui.pages.fs.ui.HomePage;
import io.thrive.fs.ui.pages.fs.ui.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest extends BaseUISelenideTest{
    @Test
    void runBrowser(){
        // открываем браузер
        open("auth");
        // создаем "ждалку"
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(10));
        // объект с методами страницы авторизации
        LoginPage loginPage = new LoginPage();
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl+LoginPage.endpoint));
        // авторизуемся
        loginPage.setUsername("vladimir.pavlyukov@thrive.io");
        loginPage.setPassword("Bb@45678");
        loginPage.btnLoginClick();
        // главная страница
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
        HomePage homePage = new HomePage();

        // переходим на страницу комиссий
        homePage.hrfCommissionsClick();
        // страница коммиссий
        CommissionsPage commissionsPage = new CommissionsPage();
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + CommissionsPage.endpoint));
        // переключаемся на вкладку снятий заработанного
        commissionsPage.tapWithdrawal();
    }
}
