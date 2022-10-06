package io.thrive.fs.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.thrive.fs.api.BaseAPITest;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.GlossaryMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.Constants;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.help.MailAPI;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;


@Epic("Регистрация нового пользователя")
public class ApiRegistrationTest extends BaseAPITest {
    private final GlossaryMethods glossaryMethods = new GlossaryMethods(getBaseURL());
    private final AuthMethods authMethods = new AuthMethods(getBaseURL());
    private final UsersMethods usersMethods = new UsersMethods(getBaseURL());
    private final DataGenerator dataGenerator = new DataGenerator();

    //</editor-fold>
//<editor-fold desc="Tests">
    @Test
    @Story("Регистрация пользователя без реферального кода")
    @DisplayName("\"Happy Flow\" Регистрация пользователя без реферального кода")
    @Description("Регистрация нового пользователя с помощью API без реферального кода")
    public void registrationWithoutReferCodeScenarioTest() throws MessagingException, IOException, InterruptedException {
        generalPart(null, null);
    }

    @Test
    @Story("Регистрация пользователя с реферальным кодом")
    @DisplayName("\"Happy Flow\" Регистрация пользователя с реферальным кодом")
    @Description("Регистрация нового пользователя с помощью API с реферальным кодом")
    public void registrationWithReferCodeScenarioTest() throws MessagingException, IOException, InterruptedException {
        JSONObject data = getReferralCodeRandomlyAndAdminToken();
        String referCode = (String) data.get("referCode");
        String adminToken = (String) data.get("adminToken");

        generalPart(referCode, adminToken);
    }

    //</editor-fold>
    @Step("Ищу в списке пользователя по email {checkUsrEmail}")
    private int searchUserInPendingList(List<JSONObject> users, String checkUsrEmail) {
        int userId = 0;
        for (var usr : users) {
            String usrEmail = (String) usr.get("email");
            Integer usrId = (Integer) usr.get("id");
            if (usrEmail.equals(checkUsrEmail)) {
                Assertions.assertEquals(0L, userId, "Duplicate registration requests with userId:" + usrId);
                userId = usrId;
            }
        }
        Assertions.assertNotEquals(0L, userId, "No registration requests found from email:" + checkUsrEmail);
        return userId;
    }

    @Step("Через апи получаю реферальный код и админ токен")
    public static JSONObject getReferralCodeRandomlyAndAdminToken() {
        // логинимся админом и получаем adminToken (accessToken)
        AuthMethods authMethods = new AuthMethods(Constants.BASE_URL + "/api");
        JSONObject responseObj = authMethods.adminLogin(Constants.ROOT_ADMIN_NAME, Constants.ROOT_ADMIN_PASSWORD);
        String adminToken = (String) responseObj.get("accessToken");

        UsersMethods usersMethods = new UsersMethods(Constants.BASE_URL + "/api");
        List<JSONObject> users = usersMethods.usersAll(adminToken, false, false);

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

        String referCode = Base64.getEncoder().encodeToString(("{\"userId\":" + referId + "}").getBytes());
        JSONObject obj = new JSONObject();
        obj.put("adminToken", adminToken);
        obj.put("referCode", referCode);
        return obj;
    }

    @Step("Основное действие")
    void generalPart(String referCode, String adminToken) throws IOException, InterruptedException, MessagingException {
        // получить глоссарий страны(вернется всего 1 - Бразилия)
        List<JSONObject> lstCountry = glossaryMethods.getGlossaryCountries();
        // получить случайную страну
        JSONObject country = lstCountry.get((int) (Math.random() * lstCountry.size()));
        int countryId = (Integer) country.get("id");
        // Получить id случайного штата
        List<LinkedHashMap> lstStates = (ArrayList<LinkedHashMap>) country.get("states");
        LinkedHashMap state = lstStates.get((int) Math.round(Math.random() * lstStates.size()));
        int stateId = (Integer) state.get("id");
        // log in to Admin
        if (adminToken == null) {
            adminToken = (String) authMethods
                    .adminLogin(Constants.ROOT_ADMIN_NAME, Constants.ROOT_ADMIN_PASSWORD)
                    .get("accessToken");
        }
        // запомнить количество пользователей ожидающих подтверждения регистрации
        int pendingUsersCount = usersMethods.usersPending(adminToken).size();
        // создаем слушалку для мыла в этом месте,
        // чтобы она зафиксировала количество писем до утверждения админом регистрации пользователя
        MailAPI mailAPI = new MailAPI();
        // генерируем данные пользователя и отправляем запрос на регистрацию
        // создать данные пользователя: full_name, email, phone_number, city
        String fullName = dataGenerator.generateFullName("pt-BR");
        String email = dataGenerator.getEmail();
        String phone = dataGenerator.getPhone();
        String city = dataGenerator.generateCity("pt-BR");
        // {{baseUrl}}/users/register
        usersMethods.usersRegister(referCode, fullName, email, phone, countryId, stateId, city);

        // Получаем список пользователей ожидающих регистрации после регистрации нового
        List<JSONObject> pendingUsers = usersMethods.usersPending(adminToken);

        Assertions.assertEquals(1, pendingUsers.size() - pendingUsersCount, "Unexpected number of new pending users");

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
        // TODO {{baseUrl}}/users/set-password проверить, что нельзя установить пароль до подтверждения регистрации
        usersMethods.usersSetPassword401("ebfdcb8b-8251-4411-8b0c-538227337704", "Bb@45678");
        // TODO {{baseUrl}}/users/pending получить ожидающих подтверждения регистрации, проверить наличие текущего
        // поищем в списке пользователей, пользователя с регистрационным email
        // проверим что он есть в списке, проверим что такой только 1
        // получим id этого пользователя
        int userId = searchUserInPendingList(pendingUsers, email);
        // подтвердим регистрацию пользователя
        usersMethods.usersApprove(adminToken, userId);
        // найдем в новых письмах подтверждающее регистрацию и вытянем из него ссылку на регистрацию
        String registrationLink = mailAPI.getFluencyStrikersRegistrationLinkFromMail(email, 200);
        // извлечем из ссылки на регистрацию, регистрационный токен
        String registrationToken = registrationLink.substring(registrationLink.indexOf("token=") + 6);
        registrationToken = registrationToken.replaceAll("\r\n", "");
        registrationToken = registrationToken.replaceAll("\n", "");
        // получаем пароль
        String pass = dataGenerator.getPassword();
        // установим пароль
        usersMethods.usersSetPassword(registrationToken, pass);
        // логинимся новым пользователем
        JSONObject creds = authMethods.userLogin(email, pass);
        Assertions.assertEquals(userId, (Integer) creds.get("userId"));
    }
}
