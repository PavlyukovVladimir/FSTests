package io.thrive.fs.ui.pages.stripe;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class StripeSelectPayoutsAccountPage {

    private String baseUrl;

    public StripeSelectPayoutsAccountPage(String baseUrl) {
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

    public void clickUseTestAccount(){
        $("button[data-test=test-mode-fill-button]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10)).click();
    }
}
