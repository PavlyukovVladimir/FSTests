package io.thrive.fs.ui.pages.stripe;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class StripeSetVerificationCodePage {

    private String baseUrl;

    public StripeSetVerificationCodePage(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private WebDriverWait wait = null;

    private WebDriverWait getWait() {
        if(wait == null){
            return new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(10));
        }
        return wait;
    }

    private SelenideElement btnFillVerificationCode = $("button[data-test=test-mode-fill-button]");

    public void tapUseTestModeButton(){
        getWait().until(ExpectedConditions.elementToBeClickable(btnFillVerificationCode));
        btnFillVerificationCode.click();
    }
}
