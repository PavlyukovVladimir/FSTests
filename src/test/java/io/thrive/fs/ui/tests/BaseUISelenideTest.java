package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.thrive.fs.help.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class BaseUISelenideTest {

    @BeforeEach
    @DisplayName("Set browser configuration, add Allure selenide listener.")
    void setAll(){

//        Configuration.driverManagerEnabled = true;
//        Configuration.webdriverLogsEnabled = true;
        Configuration.browser = "chrome";
//        Configuration.browserVersion = "104.0.5112.101-1";
        Configuration.headless = false;  // true запускает браузер в невидимом режиме
        Configuration.baseUrl = Constants.BASE_URL;
        Configuration.holdBrowserOpen = true;  // false не оставляет браузер открытым по завершению теста
        SelenideLogger.addListener(
                "AllureSelenide", new AllureSelenide()
                        .screenshots(true)  // делать скриншоты
                        .savePageSource(false));  // не сохранять копии html страниц
    }

    @AfterEach
    @DisplayName("Close browser.")
    public void tearDown() {
        Selenide.webdriver().driver().getWebDriver().close();
        Selenide.webdriver().driver().getWebDriver().quit();
    }

    @Step("Откроем браузер на странице: " + Constants.BASE_URL + "{url}")
    @DisplayName("Open browser.")
    public  void openBrowser(String url){
        Selenide.open(url);
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }
}
