package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.MailAPI;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.ui.BaseUISelenideTest;
import io.thrive.fs.ui.pages.fs.ui.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static io.thrive.fs.api.tests.ApiRegistrationTest.getReferralCodeRandomlyAndAdminToken;
import static io.thrive.fs.help.FileManipulation.saveUserLoginData;


@Epic("Тестируем функционал UI")
@Feature("Регистрация пользователя")
@Severity(SeverityLevel.CRITICAL)
public class UIRegistrationTest extends BaseUISelenideTest {

    private String referSuffix = "";

    @Test()
    @DisplayName("Регистрирую нового пользователя без реферального кода")
    @Story("Happy flow регистрация нового пользователя без реферального кода")
    @Description("Из UI регистрируем нового пользователя без реферального кода")
    public void registrationNewUserWithoutReferralCodeTest() throws MessagingException, IOException, InterruptedException {
        // открываем страницу регистрации нового пользователя
        openBrowser(RegistrationPage.endpoint + this.referSuffix);
        // объект с методами страницы регистрации
        RegistrationPage registrationPage = new RegistrationPage();
        // Ждем когда текущий url сменится на нужный
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + RegistrationPage.endpoint + referSuffix));
        // Запишем случайное Бразильское имя
        DataGenerator dataGenerator = new DataGenerator();
        String fullName = dataGenerator.generateFullName("pt-BR");
        registrationPage.setFullName(fullName);
        // Создаем email
        String email = dataGenerator.getEmail();
        registrationPage.setEmail(email);
        // Создаем телефон
        String phone = dataGenerator.getPhone();
        registrationPage.setPhone(phone);
        // "Случайно" выберем из предложенных на форме сайта стран (только Бразилия)
        registrationPage.setCountryRandomly();
        // Случайно выберем штат из предложенных на форме сайта
        registrationPage.setStateRandomly();
        // Создаем Название города
        String city = dataGenerator.generateCity("pt-BR");
        registrationPage.setCity(city);
        // Отправим форму на регистрацию(клик по кнопке)
        registrationPage.registrationClick();
        // Ждем когда текущий url поменяется
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + LoginPage.endpoint));

        // через api логинимся в админку и подтверждаем регистрацию пользователя
        // TODO надо бы это через UI переделать
        AuthMethods authMethods = new AuthMethods(Configuration.baseUrl + "api");
        JSONObject adminResponse = authMethods.adminLogin("root@admin.com", "rootadmin");
        String adminToken = (String) adminResponse.get("accessToken");
        UsersMethods usersMethods = new UsersMethods(Configuration.baseUrl + "api");
        List<JSONObject> pendingRegResponse = usersMethods.usersPending(adminToken);
        long userId = 0L;
        for (var usr : pendingRegResponse) {
            String usrEmail = (String) usr.get("email");
            Integer usrId = (Integer) usr.get("id");
            if (usrEmail.equals(email)) {
                Assertions.assertEquals(0L, userId, "Duplicate registration requests with userId:" + usrId);
                userId = usrId.longValue();
            }
        }
        Assertions.assertNotEquals(0L, userId, "No registration requests found from email:" + email);

        // создаем слушалку для мыла в этом месте,
        // чтобы она зафиксировала количество писем до утверждения админом регистрации пользователя
        MailAPI mailAPI = new MailAPI();

        // подтвердим регистрацию пользователя
        usersMethods.usersApprove(adminToken, userId);
        // найдем в новых письмах подтверждающее регистрацию и вытянем из него ссылку на регистрацию
        String registrationLink = mailAPI.getFluencyStrikersRegistrationLinkFromMail(email, 200);
        // извлечем из ссылки на регистрацию, регистрационный токен
        String registrationToken = registrationLink.substring(registrationLink.indexOf("token=") + 6);
        registrationToken = registrationToken.replaceAll("\r\n", "");
        registrationToken = registrationToken.replaceAll("\n", "");
        // получаем пароль из генератора данных
        String pass = dataGenerator.getPassword();
        // установим пароль
        // Объект страницы установки пароля
        NewPasswordPage newPasswordPage = new NewPasswordPage(registrationToken);
        // перейдем на страницу установки пароля
        newPasswordPage.openSetPasswordPage();

        newPasswordPage.setPassword(pass);
        newPasswordPage.setConfirmationPassword(pass);
        newPasswordPage.btnSubmitClick();

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
            lsObj = (JSONObject) parser.parse(localStorage.getItem("persist:fluency-strikers-dashboard-auth"));
            lsObj = (JSONObject) parser.parse((String) lsObj.get("auth"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // по совпадению user id косвенно проверяем что токен от нужного пользователя
        long extractionedUserId = ((Long) lsObj.get("userId")).longValue();
        Assertions.assertEquals(userId, extractionedUserId);

        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));

        // Сохраняю email и пароль в файл user.json в тест ресурсах
        saveUserLoginData(email, pass);

        HomePage homePage = new HomePage();
        homePage.logout();
    }

    @Test()
    @DisplayName("Регистрирую нового пользователя c реферальным кодом")
    @Story("Happy flow регистрация нового пользователя c реферальным кодом")
    @Description("Получаю реферальный код через апи и из UI регистрирую нового пользователя")
    public void registrationNewUserWithReferralCodeTest() throws MessagingException, IOException, InterruptedException {
        JSONObject data = getReferralCodeRandomlyAndAdminToken();

        referSuffix = "?referCode=" + data.get("referCode");

        registrationNewUserWithoutReferralCodeTest();
    }
}
