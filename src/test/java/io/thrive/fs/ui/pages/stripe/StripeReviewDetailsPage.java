package io.thrive.fs.ui.pages.stripe;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class StripeReviewDetailsPage {

    private String baseUrl;

    public StripeReviewDetailsPage(String baseUrl) {
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
        SelenideElement btnSubmit = $("button[data-test=requirements-index-done-button]");
        btnSubmit.shouldBe(
                Condition.and("Submit is displayed", Condition.exist, Condition.visible), Duration.ofSeconds(10));

        SelenideElement btnUpdate = $("button[data-db-analytics-name=\"expressUnified_update_person_button\"]");

        if(btnUpdate.isDisplayed()){
            btnUpdate.click();
            btnUpdate.shouldNotBe(Condition.visible, Duration.ofSeconds(10));
        }

        SelenideElement btnUseTestDocument = $("button[data-test=\"test-mode-fill-button\"");
        if(btnUseTestDocument.isDisplayed()){
            btnUseTestDocument.click();
            btnUseTestDocument.shouldNotBe(Condition.visible, Duration.ofSeconds(10));
        }

        $("button[data-test=requirements-index-done-button]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10)).click();
    }
}
