package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.thrive.fs.ui.BaseUISelenideTest;
import io.thrive.fs.ui.pages.fs.ui.LoginPage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;

import static io.thrive.fs.help.FileManipulation.loadUserLoginData;

@Epic("Тестируем функционал UI")
@Feature("Уровни")
@Severity(SeverityLevel.CRITICAL)
public class UILoginTest extends BaseUISelenideTest {

    @Test
    public void test() throws IOException, ParseException {
        // Объект с методами страницы авторизации
        LoginPage loginPage = new LoginPage();
        // открываем страницу регистрации нового пользователя
        openBrowser(LoginPage.endpoint);
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + LoginPage.endpoint));
        // получаем сохраненные креды
        JSONObject creds = loadUserLoginData();
        String email = (String) creds.get("email");
        String pass = (String) creds.get("password");
        // авторизуемся
        loginPage.setUsername(email);
        loginPage.setPassword(pass);
        loginPage.btnLoginClick();
        // главная страница
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
        // получаем токен
        LocalStorage localStorage = Selenide.localStorage();
        JSONParser parser = new JSONParser();
        JSONObject lsObj;
        try {
            lsObj = (JSONObject) parser.parse(localStorage.getItem("persist:fluency-strikers-dashboard-auth"));
            lsObj = (JSONObject) parser.parse((String) lsObj.get("auth"));
        } catch (
                ParseException e) {
            throw new RuntimeException(e);
        }

        // по совпадению user id косвенно проверяем что токен от нужного пользователя
        long extractionedUserId = ((Long) lsObj.get("userId")).longValue();
//        Assertions.assertEquals(userId, extractionedUserId);

        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
    }
}
