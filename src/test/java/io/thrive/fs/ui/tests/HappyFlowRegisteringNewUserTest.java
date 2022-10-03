package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.*;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.MailAPI;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.ui.pages.fs.ui.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


@Epic("Full registering new user")
@Feature("Registration user")
@Severity(SeverityLevel.CRITICAL)
public class HappyFlowRegisteringNewUserTest extends BaseUISelenideTest{

    @Test()
    @DisplayName("Регистрируем нового пользователя вместе со Stripe аккаунтом")
    @Story("Happy flow registration new user with Stripe")
    @Description("From UI it registers new user and registers Stripe account hor him")
    public void registrationNewUserHappyTest() throws MessagingException, IOException, InterruptedException {

//        String referSuffix = "?referCode=eyJ1c2VySWQiOjM5Nn0=";
        String referSuffix = "";

        // открываем страницу регистрации нового пользователя
        openBrowser(RegistrationPage.endpoint + referSuffix);
        // создаем "ждалку"
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(100));
        // объект с методами страницы регистрации
        RegistrationPage registrationPage = new RegistrationPage();
        // Ждем когда текущий url сменится на нужный
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + RegistrationPage.endpoint + referSuffix));
        // Запишем случайное Бразильское имя
        DataGenerator dataGenerator = new DataGenerator();
        String fullName = dataGenerator.generateFullName("pt-BR");
        registrationPage.setFullName(fullName);
        // Создаем имейл
        String email = dataGenerator.getEmail();
        registrationPage.setEmail(email);
        // Создаем телефон
        String phone = dataGenerator.getPhone();
//        registrationPage.setPhone("1"+phone);
        registrationPage.setPhone(phone);
        // "Случайно" выберем из предложенных на форме сайта стран (только Бразилия)
        registrationPage.setCountryRandomly();
        // Случайно выберем штат из предложенных на форме сайта
        registrationPage.setStateRandomly();
        // Создаем Название города
        String city = dataGenerator.generateCity("pt-BR");
        registrationPage.setCity(city);

        String selectAll = Keys.chord(Keys.CONTROL, Keys.SHIFT,"i");
        WebDriverRunner.getWebDriver().findElement(By.tagName("html")).sendKeys(selectAll);
        // Отправим форму на регистрацию(клик по кнопке)
        registrationPage.registrationClick();
        // Ждем когда текущий url поменяется
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl+ LoginPage.endpoint));

        // через api логинимся в админку и подтверждаем регистрацию пользователя
        // TODO надо бы это через UI переделать
        AuthMethods authMethods = new AuthMethods(Configuration.baseUrl + "api");
        JSONObject adminResponse = authMethods.adminLogin("root@admin.com","rootadmin");
        String adminToken = (String) adminResponse.get("accessToken");
        UsersMethods usersMethods = new UsersMethods(Configuration.baseUrl + "api");
        List<JSONObject> pendingRegResponse = usersMethods.usersPending(adminToken);
        long userId = 0L;
        for(var usr : pendingRegResponse){
            String usrEmail = (String) usr.get("email");
            Integer usrId = (Integer) usr.get("id");
            if(usrEmail.equals(email)){
                Assertions.assertEquals(0L, userId, "Duplicate registration requests with userId:" + usrId);
                userId = usrId.longValue();
            }
        }
        Assertions.assertNotEquals(0L, userId, "No registration requests found from email:" + email);

        MailAPI mailAPI = new MailAPI();

        // подтвердим регистрацию пользователя
        usersMethods.usersApprove(adminToken, userId);

        // Далее из базы получаем токен подтверждения регистрации(чтобы не лезть в мыло)
        // надо бы получение токена из мыла тоже автоматизировать
//        PostgrestDataBase postgrest = new PostgrestDataBase();

//        String registrationToken = postgrest.getToken(userId, "registration");

        String registrationLink = mailAPI.getFluencyStrikersRegistrationLinkFromMail(email , 200);

        System.out.println(registrationLink);

        String registrationToken = registrationLink.substring(registrationLink.indexOf("token=") + 6);
        // получаем пароль
        String pass = dataGenerator.getPassword();
        // установим пароль
        // Объект страницы установки пароля
        NewPasswordPage newPasswordPage = new NewPasswordPage(registrationToken);
        // перейдем на страницу установки пароля
        Selenide.open(Configuration.baseUrl + newPasswordPage.getEndpoint());

        newPasswordPage.setPassword(pass);
        newPasswordPage.setConfirmationPassword(pass);
        newPasswordPage.btnSubmitClick();

//        usersMethods.usersSetPassword(registrationToken, pass);
        // Объект с методами страницы авторизации
        LoginPage loginPage = new LoginPage();
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + LoginPage.endpoint));
        // авторизуемся
        loginPage.setUsername(email);
        loginPage.setPassword(pass);
        loginPage.btnLoginClick();
        // главная страница
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
        // получаем токен
        LocalStorage localStorage = Selenide.localStorage();
        JSONParser parser = new JSONParser();
        JSONObject lsObj;
        try {
            lsObj = (JSONObject)parser.parse(localStorage.getItem("persist:fluency-strikers-dashboard-auth"));
            lsObj = (JSONObject)parser.parse((String)lsObj.get("auth"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String accessToken = (String)lsObj.get("accessToken");
        // по совпадению user id косвенно проверяем что токен от нужного пользователя
        Assertions.assertEquals(userId, ((Long) lsObj.get("userId")).longValue());
        // Объект с методами главной страницы
        HomePage homePage = new HomePage();
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
    }
}
