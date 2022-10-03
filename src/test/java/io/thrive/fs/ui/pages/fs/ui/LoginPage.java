package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public static String endpoint = "auth";
    private SelenideElement fldUsername = $(new By.ById("username"));
    private SelenideElement fldPassword = $(new By.ById("password"));
    private SelenideElement btnLogin = $("button");

    @Step("Заполним имя пользователя: \"{username}\"")
    public void setUsername(String username){
        fldUsername.setValue(username);
    }

    @Step("Заполним пароль: \"{password}\"")
    public void setPassword(String password){
        fldPassword.setValue(password);
    }

    @Step("Нажмем кнопку")
    public void btnLoginClick(){
        btnLogin.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
    }
}
