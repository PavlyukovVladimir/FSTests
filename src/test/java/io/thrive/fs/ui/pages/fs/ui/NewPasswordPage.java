package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class NewPasswordPage {
    private static String endpoint = "auth/new-password";
    private static String token;

    public NewPasswordPage(String registrationToken){
         token = registrationToken;
    }

    @Step("Перехожу на страницу регистрации нового пароля.")
    public void openSetPasswordPage(){
        Selenide.open(Configuration.baseUrl + this.getEndpoint());
    }

    public String getEndpoint(){
        return endpoint + "?token=" + token;
    }
    private SelenideElement fldPassword = $("#password");
    private SelenideElement fldConfirmationPassword = $("#passwordConfirmation");
    private SelenideElement btnSubmit = $("button");

    @Step("Ввожу пароль: {password}")
    public void setPassword(String password){
        fldPassword.setValue(password);
    }

    @Step("Ввожу подтверждение пароля: {ConfirmationPassword}")
    public void setConfirmationPassword(String ConfirmationPassword){
        fldConfirmationPassword.setValue(ConfirmationPassword);
    }

    @Step("Нажимаю кнопку \"submit\"")
    public void btnSubmitClick(){
        btnSubmit.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
    }
}
