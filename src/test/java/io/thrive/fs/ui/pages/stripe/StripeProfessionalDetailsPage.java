package io.thrive.fs.ui.pages.stripe;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StripeProfessionalDetailsPage {

    private String baseUrl;

    public StripeProfessionalDetailsPage(String baseUrl) {
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

    public void checkProfession(){
        $("button[name=industry]").shouldHave(Condition.visible, Duration.ofSeconds(5)).click();
        $$("ul[role] li").shouldHave(
                CollectionCondition.sizeGreaterThan(1), Duration.ofSeconds(5)).get(1).click();
        $("button[data-test=company-submit-button]")
                .shouldHave(Condition.visible, Duration.ofSeconds(5)).click();
    }
}
