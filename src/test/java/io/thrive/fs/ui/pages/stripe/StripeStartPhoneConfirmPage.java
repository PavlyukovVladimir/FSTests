package io.thrive.fs.ui.pages.stripe;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.thrive.fs.api.common.StripeMethods;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class StripeStartPhoneConfirmPage {

    private String baseUrl;

    public StripeStartPhoneConfirmPage(String accessToken, String baseAPIUrl) {
        StripeMethods stripeMethods = new StripeMethods(baseAPIUrl);
        String status = stripeMethods.getStripeConnectStatus(accessToken);
        if(status.equals("Unregistered")){
            baseUrl = stripeMethods.postStripeConnectRegister(accessToken);
        }else{
            baseUrl = stripeMethods.getStripeConnectAuthLink(accessToken);
        }
    }

    public StripeStartPhoneConfirmPage(String baseUrl) {
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

    private SelenideElement btnFillTestPhone = $("button[data-test=phone-help-text-test-mode]");
    private SelenideElement btnContinue = $("button[data-test=phone-entry-submit]");

    public void setTestZeroPhone(){
        btnFillTestPhone.click();
    }
    public void clickContinue(){
        getWait().until(ExpectedConditions.elementToBeClickable(btnContinue));
        btnContinue.click();
    }



}
