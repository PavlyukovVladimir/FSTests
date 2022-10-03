package io.thrive.fs.ui.pages.fs.ui;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    public static String endpoint = "";
    private SelenideElement hrfCommissions = $(new By.ByCssSelector("span.ant-menu-title-content > a[href=\"/commissions\"]"));

    @Step("Клик по ссылке на страницу комиссий")
    public void hrfCommissionsClick(){
        hrfCommissions.click();
    }
}
