package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@DisplayName("Страница регистрации")
public class RegistrationPage {
    public static String endpoint = "registration";

    private SelenideElement fldFullName = $("#fullName");
    private SelenideElement fldEmail = $("#email");
    private SelenideElement fldPhone = $("#phoneNumber");

    private SelenideElement cbxCountry = $("#countryId");
    private SelenideElement cbxState = $("#stateId");

    private SelenideElement fldCity = $("#city");

    private SelenideElement btnRegistration = $("button");

    @Step("Ввожу ФИО: {fullName}")
    public void setFullName(String fullName){
        fldFullName.setValue(fullName);
    }

    @Step("Ввожу email: {email}")
    public void setEmail(String email){
        fldEmail.setValue(email);
    }

    @Step("Ввожу телефон: {phone}")
    public void setPhone(String phone){
        fldPhone.setValue(phone);
    }
    @Step("Заполняю поле Country: {countryElementNumber} элементом выпадающего списка")
    public void setCountry(int countryElementNumber){
        cbxCountry.click();
        List<SelenideElement> lstCountryElements = $$("#countryId_list + * .ant-select-item-option-content");
        lstCountryElements.get(countryElementNumber).click();

    }

    @Step("Заполняю поле Country случайным образом")
    public void setCountryRandomly(){
        cbxCountry.click();
        List<SelenideElement> lstCountryElements = $$("#countryId_list + * .ant-select-item-option-content");
        int countryCount = lstCountryElements.size();
        lstCountryElements.get((int)(Math.random() * countryCount)).click();
    }

    @Step("Заполняю поле State: {stateElementNumber} элементом выпадающего списка")
    public void setState(int stateElementNumber){
        cbxState.click();
        List<SelenideElement> lstStateElements = $$("#stateId_list + * .ant-select-item-option-content");
        lstStateElements.get(stateElementNumber).click();

    }

    @Step("Заполняю поле State случайным образом")
    public void setStateRandomly(){
        cbxState.click();
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#stateId_list + * .ant-select-item-option-content")));
        List<SelenideElement> lstStateElements = $$("#stateId_list + * .ant-select-item-option-content");
        int stateCount = lstStateElements.size() - 2;
        lstStateElements.get((int)(Math.random() * stateCount)).click();

    }

    @Step("Заполняю поле City: {city}")
    public void setCity(String city){
        fldCity.setValue(city);
    }

    @Step("Нажимаю кнопку \"I WANT TO BECOME A STRIKER\"")
    public void registrationClick(){
        btnRegistration.shouldBe(Condition.enabled, Duration.ofSeconds(4)).click();
    }
}
