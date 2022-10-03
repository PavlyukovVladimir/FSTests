package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.thrive.fs.api.common.StripeMethods;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class CommissionsPage {

    public static String endpoint = "commissions";
    // Commissions tabs elements
    private SelenideElement tabStripe = $("#rc-tabs-0-tab-1");
    private SelenideElement txtTabStripeAccountBalance = $x("//div[@class=\"commissions-tab__container\"]//span[contains(text(),\"BRL\")]");
    private SelenideElement btnMoneyManage = $("#rc-tabs-0-panel-1 button");

    private SelenideElement tabWithdrawal = $("#rc-tabs-0-tab-2");
    private SelenideElement txtTabWithdrawalAccountBalance = $$x(".commissions-tab__container.withdrawal-page span").get(0);
    private SelenideElement txtTabWithdrawalMinAmount = $$x(".commissions-tab__container.withdrawal-page span").get(1);
    private SelenideElement fldInputWithdrawal = $x(".commissions-tab__container.withdrawal-page input");
    private SelenideElement btnInputWithdrawal = $x(".commissions-tab__container.withdrawal-page button");

    private SelenideElement tabHistory = $("#rc-tabs-0-tab-3");

    @Step("Клик по вкладке \"Stripe\"")
    public void tapStripe(){
        tabStripe.click();
        tabStripe.shouldBe(Condition.visible, Duration.ofSeconds(4));
    }

    @Step("Клик по вкладке \"Withdrawal\"")
    public void tapWithdrawal(){
        tabWithdrawal.click();
        tabWithdrawal.shouldBe(Condition.visible, Duration.ofSeconds(4));
    }

    @Step("Клик по вкладке \"History\"")
    public void tapHistory(){
        tabHistory.click();
        tabHistory.shouldBe(Condition.visible, Duration.ofSeconds(4));
    }

    @Step("Клик по кнопке \"Money manage\"")
    public void btnCreateClick(){
        btnMoneyManage.shouldBe(Condition.visible, Duration.ofSeconds(5));
        btnMoneyManage.find("button span").shouldHave(Condition.text("Create"));
        btnMoneyManage.click();
    }
}
