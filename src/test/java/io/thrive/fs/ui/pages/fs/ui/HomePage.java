package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    public static String endpoint = "";
    private SelenideElement hrfCommissions = $("span.ant-menu-title-content > a[href=\"/commissions\"]");
    private SelenideElement userProfileDropdown = $("div.user-profile-dropdown");
    private SelenideElement btnLogout = $x("//button[@class='btn ']");

    @Step("Клик по ссылке на страницу комиссий")
    public void hrfCommissionsClick() {
        hrfCommissions.click();
    }

    @Step("Выходим из аккаунта")
    public void logout() {
        userProfileDropdown.click();
        btnLogout.shouldBe(Condition.exist, Duration.ofSeconds(5))
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldBe(Condition.enabled)
                .click();
    }
}
