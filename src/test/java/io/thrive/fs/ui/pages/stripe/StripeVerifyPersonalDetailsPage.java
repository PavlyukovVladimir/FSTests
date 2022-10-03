package io.thrive.fs.ui.pages.stripe;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StripeVerifyPersonalDetailsPage {

    private String baseUrl;

    public StripeVerifyPersonalDetailsPage(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private WebDriverWait wait = null;

    private WebDriverWait getWait() {
        if (wait == null) {
            return new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(10));
        }
        return wait;
    }

    public void fillVerijyPersonalDetails(String email, String phone) {
        // fill email
        By byElementEmail = By.cssSelector("input[type=email]");
        getWait().until(ExpectedConditions.visibilityOfElementLocated(byElementEmail));
        SelenideElement fldEmail = $(byElementEmail);
        fldEmail.setValue(email);

        // fill birthday
/*<input
    width="400"
    id="dob"
    maxlength="2"
    name="dob-day"
    pattern="[0-9]*"
    placeholder="DD"
    type="text"
    aria-describedby="DateInput-input-149-description"
    aria-label="Day"
    class="
        Input
        Input--nowrap
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
        DateInput-input
        DateInput-input--day
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
    "
    value=""
    style="color: rgb(60, 66, 87);"
>*/


        SelenideElement sdd = $("#dob-day");
        if(sdd.exists()){
            sdd.click();
            $$("#dob-day option").get(1).click();
        }else{
            SelenideElement dd = $("input[aria-label=Day]");
            dd.val("01");
        }
/*<input
    width="400"
    maxlength="2"
    name="dob-month"
    pattern="[0-9]*"
    placeholder="MM"
    type="text"
    aria-describedby="DateInput-input-150-description"
    aria-label="Month"
    class="
        Input
        Input--nowrap
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
        DateInput-input
        DateInput-input--month
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
    "
    value=""
    style="color: rgb(60, 66, 87);"
>*/
        SelenideElement smm = $("#dob-month");
        if(smm.exists()){
            smm.click();
            $$("#dob-month option").get(1).click();
        }else {
            SelenideElement mm = $("input[aria-label=Month]");
            mm.val("01");
        }
/*<input
    width = "400"
    maxlength = "4"
    name = "dob-year"
    pattern = "[0-9]*"
    placeholder = "YYYY"
    type = "text"
    aria-describedby = "DateInput-input-151-description"
    aria-label = "Year"
    class=
    "
        Input
        Input--nowrap
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
        DateInput-input
        DateInput-input--year
        PressableContext
        Padding-vertical--8
        PressableContext--cursor--text
        PressableContext--display--inlineFlex
        PressableContext--fontLineHeight--28
        PressableContext--fontSize--16
        PressableContext--fontWeight--regular
        PressableContext--height
        PressableContext--height--jumbo
        PressableContext--radius--all
        PressableContext--width
        PressableContext--width--auto
    "
    value = ""
    style = "color: rgb(60, 66, 87);"
>*/
        SelenideElement syyyy = $("#dob-year");
        if(syyyy.exists()){
            syyyy.click();
            $$("#dob-year option").get(1).click();
        }else {
            SelenideElement yyyy = $("input[aria-label=Year]");
            yyyy.val("1980");
        }

        // fill postal code
        $("input[name=zip]").val("00000000");
        // fill phone
        $("#phone").setValue(phone.substring(3));
        // fill CPF (Cadastro de Pessoas Físicas) аналог инд.налог.номера ИНН
        $("#id_number").setValue("000.000.000-00");
        // choose "No" to the question of belonging to high-government positions
        $$("div[layout=stacked] input[name=\"person[political_exposure]\"]").get(1).click();

        $("button[data-test=bizrep-submit-button]").click();
    }
}
