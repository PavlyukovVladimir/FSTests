package io.thrive.fs.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.GlossaryMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.help.PostgrestDataBase;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;

@Epic("New user registration")
public class ApiRegistrationTest extends BaseAPITest{

//<editor-fold desc="Fields">
    private final GlossaryMethods glossaryMethods = new GlossaryMethods(getBaseURL());
    private final AuthMethods authMethods = new AuthMethods(getBaseURL());
    private final UsersMethods usersMethods = new UsersMethods(getBaseURL());
    private final DataGenerator dataGenerator = new DataGenerator();
    private final PostgrestDataBase postgrestDataBase = new PostgrestDataBase();
//</editor-fold>
//<editor-fold desc="Tests">
    @Test
    @Story("Registration new user with refer code")
    @DisplayName("\"Happy Flow\" Registration new user with refer code")
    @Description("Registration new user from API with refer code")
    public void registrationWithReferCodeScenarioTest(){
        // получить глоссарий страны(вернется всего 1 - Бразилия)
        List<JSONObject> lstCountry = getGlossary();
        // получить случайную страну
        JSONObject country = getCountryIdRandomly(lstCountry);
        int countryId = (Integer) country.get("id");
        // Получить id случайного штата
        int stateId = getStateIdRandomly(country);
        // получить id действующего пользователя, для refer id
        // log in to Admin
        String adminToken = adminLogin("root@admin.com","rootadmin");
        // Получить список пользователей,
        List<JSONObject> users = getUserList(adminToken, false, false);
        // найти в нем случайного пользователя у которого в мейле содержится "vladimir.pavlyukov",
        // получить id этого пользователя
        int referId = getUserIdForReferCode(users);
        // создать рефер код
        String referCode = Base64.getEncoder().encodeToString(("{\"userId\":" + referId + "}").getBytes());
        // запомнить количество пользователей в базе до регистрации
        int usersCount = getUsersCountBeforeRegistration();
        // генерируем данные пользователя и отправляем запрос на регистрацию
        // создать данные пользователя: full_name, email, phone_number, city
        String fullName = dataGenerator.generateFullName("pt-BR");
        String email = dataGenerator.getEmail();
        String phone = dataGenerator.getPhone();
        String city = dataGenerator.generateCity("pt-BR");
        // {{baseUrl}}/users/register
        registerNewUser(referCode, fullName, email, phone, countryId, stateId, city);
        // TODO проверить что количество пользователей стало на 1 больше и запомнить id последнего
        // Получаем список пользователей после регистрации нового
        users = getUsersListAfterRegistration(adminToken, false);

        Assertions.assertEquals(1, users.size() - usersCount, "Unexpected number of new users");

        int  userId = (Integer) users.get(0).get("id");
//        // TODO проверить, что нельзя создать пользователя у которого телефон как у существующего
//        JSONObject obj = usersMethods.usersRegister400(referCode, fullName, "s" + email, phone, countryId, stateId, city);
//
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                new DataGenerator().getEmail(),
//                phone, countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO проверить, что нельзя создать пользователя у которого email как у существующего
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                email,
//                new DataGenerator().getPhone(), countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO проверить, что нельзя создать пользователя у которого телефон и email как у существующего
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                email,
//                phone, countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO {{baseUrl}}/users/set-password проверить, что нельзя установить пароль до подтверждения регистрации
//        usersMethods.usersSetPassword401("ebfdcb8b-8251-4411-8b0c-538227337704", "Bb@45678");
        // TODO {{baseUrl}}/users/pending получить ожидающих подтверждения регистрации, проверить наличие текущего
        // получить пользователей ожидающих регистрации и проверить что новый в этом списке
        List<JSONObject> pendingUsers = getPendingUsers(adminToken);
        // проверить что нужный пользователь присутствует
        checkingUserInPendingList(pendingUsers, userId, email);
        // подтвердим регистрацию пользователя
        approveUserRegistration(adminToken, userId);
        // Далее из базы получаем токен подтверждения регистрации(чтобы не лезть в мыло)
        String registrationToken = getRegistrationToken(userId, "registration");
        // получаем пароль
        String pass = dataGenerator.getPassword();
        // установим пароль
        setPassword(registrationToken, pass);
        // логинимся новым пользователем
        JSONObject creds = auserLogin(email, pass);
        Assertions.assertEquals(userId, (Integer) creds.get("userId"));
    }

    @Test
    @Story("Registration new user without refer code")
    @DisplayName("\"Happy Flow\" Registration new user without refer code\"")
    @Description("Registration new user from API without refer code")
    public void registrationWithoutReferCodeScenarioTest(){
        // получить глоссарий страны(вернется всего 1 - Бразилия)
        List<JSONObject> lstCountry = getGlossary();
        // получить случайную страну
        JSONObject country = getCountryIdRandomly(lstCountry);
        int countryId = (Integer) country.get("id");
        // Получить id случайного штата
        int stateId = getStateIdRandomly(country);


        // запомнить количество пользователей в базе до регистрации
        int usersCount = getUsersCountBeforeRegistration();
        // генерируем данные пользователя и отправляем запрос на регистрацию
        // создать данные пользователя: full_name, email, phone_number, city
        String fullName = dataGenerator.generateFullName("pt-BR");
        String email = dataGenerator.getEmail();
        String phone = dataGenerator.getPhone();
        String city = dataGenerator.generateCity("pt-BR");
        // {{baseUrl}}/users/register
        registerNewUser(null, fullName, email, phone, countryId, stateId, city);
        // TODO проверить что количество пользователей стало на 1 больше и запомнить id последнего
        // log in to Admin
        String adminToken = adminLogin("root@admin.com","rootadmin");
        // Получаем список пользователей после регистрации нового
        List<JSONObject> users = getUsersListAfterRegistration(adminToken, false);

        Assertions.assertEquals(1, users.size() - usersCount, "Unexpected number of new users");

        int  userId = (Integer) users.get(0).get("id");
//        // TODO проверить, что нельзя создать пользователя у которого телефон как у существующего
//        JSONObject obj = usersMethods.usersRegister400(referCode, fullName, "s" + email, phone, countryId, stateId, city);
//
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                new DataGenerator().getEmail(),
//                phone, countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO проверить, что нельзя создать пользователя у которого email как у существующего
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                email,
//                new DataGenerator().getPhone(), countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO проверить, что нельзя создать пользователя у которого телефон и email как у существующего
//        obj = usersMethods.usersRegister400(referCode, fullName,
//                email,
//                phone, countryId, stateId, city);
//        Assertions.assertEquals("User with this email or phone already exists", (String) obj.get("message"));
//        Assertions.assertEquals(Integer.valueOf(400), (Integer) obj.get("statusCode"));
//        // TODO {{baseUrl}}/users/set-password проверить, что нельзя установить пароль до подтверждения регистрации
//        usersMethods.usersSetPassword401("ebfdcb8b-8251-4411-8b0c-538227337704", "Bb@45678");
        // TODO {{baseUrl}}/users/pending получить ожидающих подтверждения регистрации, проверить наличие текущего
        // получить пользователей ожидающих регистрации и проверить что новый в этом списке
        List<JSONObject> pendingUsers = getPendingUsers(adminToken);
        // проверить что нужный пользователь присутствует
        checkingUserInPendingList(pendingUsers, userId, email);
        // подтвердим регистрацию пользователя
        approveUserRegistration(adminToken, userId);
        // Далее из базы получаем токен подтверждения регистрации(чтобы не лезть в мыло)
        String registrationToken = getRegistrationToken(userId, "registration");
        // получаем пароль
        String pass = dataGenerator.getPassword();
        // установим пароль
        setPassword(registrationToken, pass);
        // логинимся новым пользователем
        JSONObject creds = auserLogin(email, pass);
        Assertions.assertEquals(userId, (Integer) creds.get("userId"));
    }
//</editor-fold>
//<editor-fold desc="Steps">
    @Step("Log in as a new user")
    private JSONObject auserLogin(String email, String pass) {
        return authMethods.userLogin(email, pass);
    }

    @Step("Setting a password")
    private void setPassword(String registrationToken, String pass) {
        usersMethods.usersSetPassword(registrationToken, pass);
    }

    @Step("Getting a registration token from the database")
    private String getRegistrationToken(int userId, String type) {
        PostgrestDataBase postgrest = new PostgrestDataBase();
        return postgrest.getToken(userId, type);
    }

    @Step("Confirming the user's registration")
    private void approveUserRegistration(String adminToken, int userId) {
        usersMethods.usersApprove(adminToken, userId);
    }

    @Step("Checking that the desired user is present in the list of pending confirmation")
    void checkingUserInPendingList(List<JSONObject> users, int checkUserId, String checkUsrEmail){
        int userId = 0;
        for(var usr : users){
            String usrEmail = (String) usr.get("email");
            Integer usrId = (Integer) usr.get("id");
            if(usrEmail.equals(checkUsrEmail)){
                Assertions.assertEquals(0L, userId, "Duplicate registration requests with userId:" + usrId);
                userId = usrId;
            }
        }
        Assertions.assertNotEquals(0L, userId, "No registration requests found from email:" + checkUsrEmail);
        Assertions.assertEquals(checkUserId, userId, "No matched user id's");
    }

    @Step("Getting a list of users waiting for confirmation")
    private List<JSONObject> getPendingUsers(String adminToken) {
        return usersMethods.usersPending(adminToken);
    }

    @Step("Registering a new user")
    private void registerNewUser(String referCode, String fullName, String email, String phone, int countryId, int stateId, String city) {
        usersMethods.usersRegister(referCode, fullName, email, phone, countryId, stateId, city);
    }

    @Step("Узнаем количество пользователей в базе после регистрации нового")
    private List<JSONObject> getUsersListAfterRegistration(String adminToken, boolean b) {
        return postgrestDataBase.getUsers();
    }
    @Step("Узнаем количество пользователей в базе до регистрации")
    private int getUsersCountBeforeRegistration() {
        return  (int) postgrestDataBase.getUsersCount();
    }

    @Step("Getting the user ID for the referral code")
    private int getUserIdForReferCode(List<JSONObject> users) {
        Random random = new Random();
        int[] randomUsersIDArray = random.ints(users.size(),0,users.size()).toArray();

        int referId = 0;
        for(int id: randomUsersIDArray){
            JSONObject user = users.get(id);
            String mail = (String) user.get("email");
            if(mail.contains("vladimir.pavlyukov")){
                referId = (Integer) user.get("id");
                break;
            }
        }
        if(referId == 0) throw new RuntimeException("Not founded user for referral code");
        return referId;
    }

    @Step("Getting a list of users before registering a new one")
    private List<JSONObject> getUserList(String adminToken, boolean isReferral, boolean invoicingPermissions) {
        return usersMethods.usersAll(adminToken, isReferral, invoicingPermissions);
    }

    @Step("Log in as an administrator\n")
    private String adminLogin(String adminname, String password) {
        return  (String) authMethods
                .adminLogin(adminname,password)
                .get("accessToken");
    }

    @Step("Getting the id of a random state from the country")
    private int getStateIdRandomly(JSONObject country) {
        List<LinkedHashMap> lstStates = (ArrayList<LinkedHashMap>) country.get("states");
        LinkedHashMap state = lstStates.get((int)Math.round(Math.random() * lstStates.size()));
        return  (Integer) state.get("id");
    }

    @Step("Get a random country from the glossary")
    private JSONObject getCountryIdRandomly(List<JSONObject> lstCountry) {
        return lstCountry.get((int)(Math.random() * lstCountry.size()));
    }

    @Step("Get a list of countries and states from the glossary")
    private List<JSONObject> getGlossary() {
        return glossaryMethods.getGlossaryCountries();
    }
//</editor-fold>

}
