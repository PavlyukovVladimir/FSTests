package io.thrive.fs.ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.*;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.Constants;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.help.MailAPI;
import io.thrive.fs.help.PostgrestDataBase;
import io.thrive.fs.ui.BaseUISelenideTest;
import io.thrive.fs.ui.pages.fs.ui.LoginPage;
import io.thrive.fs.ui.pages.fs.ui.NewPasswordPage;
import io.thrive.fs.ui.pages.fs.ui.RegistrationPage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


import static com.codeborne.selenide.Selenide.switchTo;

@Epic("Registering new user with refer code")
@Feature("Registration user")
@Severity(SeverityLevel.CRITICAL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HappyFlowRegisteringNewUser2Test extends BaseUISelenideTest {
    public final static String story = "Happy flow registration new user with refer code";
    private String baseUrl = "";
    private DataGenerator dataGenerator;

    private String email;

    MailAPI mailAPI;
    private String registrationToken;
    private String password;
    // Объект страницы установки пароля
    private NewPasswordPage newPasswordPage;
    private int userId;
    // Объект с методами страницы авторизации
    private LoginPage loginPage;

    // объект с методами страницы регистрации
    private RegistrationPage registrationPage;

    @BeforeAll
    public void setUp(){
        baseUrl = Constants.BASE_URL;
        // создаем генератор данных
        dataGenerator = new DataGenerator();
        // создаем "ждалку"
        Selenide.open();
        wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(10));
    }

    @Test
    @Step("Open browser on registration page")
    @Story(story)
    @Order(1)
    public void openBrowser(){
        // открываем страницу регистрации нового пользователя
//        String referSuffix = "?referCode=eyJ1c2VySWQiOjM5Nn0=";
        String referSuffix = "";
        super.openBrowser(RegistrationPage.endpoint + referSuffix);
//        Selenide.open();
        // Ждем когда текущий url сменится на нужный
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + RegistrationPage.endpoint + referSuffix));
        registrationPage = new RegistrationPage();
    }

    @Test
    @Step("Fills full name field")
    @Story(story)
    @Order(2)
    public void fillFullNameField(){
        // Запишем случайное Бразильское имя
        String fullName = dataGenerator.generateFullName("pt-BR");
        registrationPage.setFullName(fullName);
    }

    @Test
    @Step("Fills email field")
    @Story(story)
    @Order(3)
    public void fillEmailField(){
        // Создаем имейл
        email = dataGenerator.getEmail();
        registrationPage.setEmail(email);
    }

    @Test
    @Step("Fills phone field")
    @Story(story)
    @Order(4)
    public void fillPhoneField(){
        // Создаем телефон
        String phone = dataGenerator.getPhone();
        //  registrationPage.setPhone("1"+phone);
        registrationPage.setPhone(phone);
    }

    @Test
    @Step("Sets country")
    @Story(story)
    @Order(5)
    public void setCountry(){
        // "Случайно" выберем из предложенных на форме сайта стран (только Бразилия)
        registrationPage.setCountryRandomly();
    }

    @Test
    @Step("Sets state")
    @Story(story)
    @Order(6)
    public void setState(){
        // Случайно выберем штат из предложенных на форме сайта
        registrationPage.setStateRandomly();
    }

    @Test
    @Step("Sets city")
    @Story(story)
    @Order(7)
    public void setCity(){
        // Создаем Название города
        String city = dataGenerator.generateCity("pt-BR");
        registrationPage.setCity(city);
    }

    @Test
    @Step("Taps F12 key")
    @Story(story)
    @Order(8)
    public void tupF12Key(){
        // включим панель разработчика
        String buttonName = "F12";
        Keys key = Keys.valueOf(buttonName.toUpperCase());
        switchTo().activeElement().sendKeys(key);
    }

    @Test
    @Step("Registration button clicks")
    @Story(story)
    @Order(9)
    public void registrationButtonClick(){
        // Отправим форму на регистрацию(клик по кнопке)
        registrationPage.registrationClick();
        // Ждем когда текущий url поменяется
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl+ LoginPage.endpoint));
    }

    @Test
    @Step("Approves user registration")
    @Story(story)
    @Order(10)
    public void approveUserRegistration() throws MessagingException {
        // создаем слушатель почты здесь,
        // чтобы зафиксировать количество писем до появления нового с регистрацией
        mailAPI = new MailAPI();
        // через api логинимся в админку и подтверждаем регистрацию пользователя
        // TODO надо бы это через UI переделать
        AuthMethods authMethods = new AuthMethods(Configuration.baseUrl + "api");
        JSONObject adminResponse = authMethods.adminLogin("root@admin.com","rootadmin");
        String adminToken = (String) adminResponse.get("accessToken");
        UsersMethods usersMethods = new UsersMethods(Configuration.baseUrl + "api");
        List<JSONObject> pendingRegResponse = usersMethods.usersPending(adminToken);
        for(var usr : pendingRegResponse){
            String usrEmail = (String) usr.get("email");
            Integer usrId = (Integer) usr.get("id");
            if(usrEmail.equals(email)){
                Assertions.assertEquals(0, userId, "Duplicate registration requests with userId:" + usrId);
                userId = usrId;
            }
        }
        Assertions.assertNotEquals(0, userId, "No registration requests found from email:" + email);
        // подтвердим регистрацию пользователя
        usersMethods.usersApprove(adminToken, userId);
    }

    @Test
    @Step("Gets registration token")
    @Story(story)
    @Order(11)
    public void getRegistrationToken() throws IOException, InterruptedException {
        // Далее из базы получаем токен подтверждения регистрации(чтобы не лезть в мыло)
//        PostgrestDataBase postgrest = new PostgrestDataBase();
//
//        registrationToken = postgrest.getToken(userId, "registration");

        String registrationLink = mailAPI.getFluencyStrikersRegistrationLinkFromMail(email , 200);

//        System.out.println(registrationLink);

        registrationToken = registrationLink.substring(registrationLink.indexOf("token=") + 6);
    }

    @Test
    @Step("Opens set password page")
    @Story(story)
    @Order(12)
    public void openSetPasswordPage(){
        // Объект страницы установки пароля
        newPasswordPage = new NewPasswordPage(registrationToken);
        // перейдем на страницу установки пароля
        Selenide.open(Configuration.baseUrl + newPasswordPage.getEndpoint());
    }

    @Test
    @Step("Set password")
    @Story(story)
    @Order(13)
    public void setPassword(){
        // генерируем пароль
        password = dataGenerator.getPassword();
        // установим пароль
        newPasswordPage.setPassword(password);
        newPasswordPage.setConfirmationPassword(password);
        newPasswordPage.btnSubmitClick();
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl + LoginPage.endpoint));
    }

    @Test
    @Step("Logs in")
    @Story(story)
    @Order(15)
    public void logsIn(){
        // Объект с методами страницы авторизации
        LoginPage loginPage = new LoginPage();
        // авторизуемся
        loginPage.setUsername(email);
        loginPage.setPassword(password);
        loginPage.btnLoginClick();
        // главная страница
        wait.until(ExpectedConditions.urlToBe(Configuration.baseUrl));
    }

    @Test
    @Step("Checks user id")
    @Story(story)
    @Order(16)
    public void checksUserId(){
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
    }
}
