package io.thrive.fs.api.tests;

import com.google.gson.JsonArray;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.thrive.fs.api.BaseAPITest;
import io.thrive.fs.api.common.AuthMethods;
import io.thrive.fs.api.common.GlossaryMethods;
import io.thrive.fs.api.common.SalesMethods;
import io.thrive.fs.api.common.UsersMethods;
import io.thrive.fs.help.Constants;
import io.thrive.fs.help.DataGenerator;
import io.thrive.fs.help.MailAPI;
import io.thrive.fs.help.WebhookForSales;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static io.thrive.fs.help.FileManipulation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Epic("Проверка системы уровней")
public class APILvlsTest extends BaseAPITest {
    private final GlossaryMethods glossaryMethods = new GlossaryMethods(getBaseURL());
    private final AuthMethods authMethods = new AuthMethods(getBaseURL());
    private final UsersMethods usersMethods = new UsersMethods(getBaseURL());
    private final DataGenerator dataGenerator = new DataGenerator();
    private final SalesMethods salesMethods = new SalesMethods(Constants.BASE_URL + "api/");
    private final WebhookForSales webhookForSales = new WebhookForSales();

    //</editor-fold>
//<editor-fold desc="Tests">
    @Test
    @Story("Последовательное повышение уровня с 0 в течении дня")
    @DisplayName("Последовательное повышение уровня с 0 в течении дня")
    @Description("Последовательное повышение уровня с 0 в течении дня, с помощью API")
    public void lvlsTest() throws IOException, ParseException {
        // получаю креды пользователя
        JSONObject creds = loadUserLoginData();
        String email = (String) creds.get("email");
        String pass = (String) creds.get("password");

        // авторизуюсь
        JSONObject loginData = authMethods.userLogin(email, pass);
        int userId = (Integer) loginData.get("userId");
        String accessToken = (String) loginData.get("accessToken");
        // получаю таблицу уровней
        JSONObject lvlTable = loadLvlsTable();
        // получаю список названий уровней
        JSONArray keys = (JSONArray) lvlTable.get("keys");
        for (int level = 0; level < keys.size() - 1; level++) {
            String key = (String) keys.get(level);
            int totalSales = 0;
            JSONObject lvlInf = (JSONObject) lvlTable.get(key);
            // узнать сколько всего звезд на уровне
            int starsCount = (int) ((Long) lvlInf.get("stars")).longValue();
            // узнать сколько нужно продаж для увеличения на 1 звезду
            int starPrice = (int) ((Long) lvlInf.get("starPrice")).longValue();
            //  посчитать до какого числа продаж включительно не заполниться треть звезды
            int zeroThirdMax = starPrice / 2 - 1;
            //  посчитать до какого числа продаж включительно не заполниться 2 трети звезды
            int firstThirdMax = starPrice - 2;
            //  посчитать до какого числа продаж включительно не заполниться 3 трети звезды
            int secondThirdMax = starPrice - 1;
            // по количеству звезд нужно запустить цикл
            int[] sales = new int[]{
                    zeroThirdMax,
                    1,
                    firstThirdMax - 1 - zeroThirdMax,
                    1,
                    secondThirdMax - firstThirdMax - 1,
                    1
            };
            for (int i = 0; i < starsCount; i++) {
                for (int j = 0; j < sales.length; j++) {
                    // GET /sales/user-level
                    JSONObject lvlData = salesMethods.salesUserLevel(accessToken);
                    // проверяем что звезд как в счетчике
                    assertEquals(i, (Integer) lvlData.get("intStars"));
                    // проверяем что третей 0
                    assertEquals(j / 2, (Integer) lvlData.get("starThirds"));
                    // проверяем что уровень такой, какой должен быть на текущем шаге
                    assertEquals(key, lvlData.get("levelTitle"));
                    // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
                    int expectedSalesToNextLvl = starsCount * starPrice - totalSales;
                    int actualSalesToNextLvl = (Integer) lvlData.get("salesToNextLevel");
//                    assertEquals(expectedSalesToNextLvl , actualSalesToNextLvl);

                    // накручиваю продажи почти до первой трети
                    webhookForSales.fullSales(userId, sales[j], null);
                    totalSales += sales[j];
                }

//                // GET /sales/user-level
//                JSONObject lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 0
//                assertEquals(0, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи почти до первой трети
//                webhookForSales.fullSales(userId, sales[0], null);
//                totalSales += sales[0];
//
//                // GET /sales/user-level
//                lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 0
//                assertEquals(0, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи до первой трети
//                webhookForSales.fullSales(userId, sales[1], null);
//                totalSales += sales[1];
//
//                // GET /sales/user-level
//                lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 1
//                assertEquals(1, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи почти до второй трети
//                webhookForSales.fullSales(userId, sales[2], null);
//                totalSales += sales[2];
//
//                // GET /sales/user-level
//                lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 1
//                assertEquals(1, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи до второй трети
//                webhookForSales.fullSales(userId, sales[3], null);
//                totalSales += sales[3];
//
//                // GET /sales/user-level
//                lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 2
//                assertEquals(2, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи почти до уровня
//                webhookForSales.fullSales(userId, sales[4], null);
//                totalSales += sales[4];
//
//                // GET /sales/user-level
//                lvlData = salesMethods.salesUserLevel(accessToken);
//                // проверяем что звезд как в счетчике
//                assertEquals(i, (Integer) lvlData.get("intStars"));
//                // проверяем что третей 2
//                assertEquals(2, (Integer) lvlData.get("starThirds"));
//                // проверяем что уровень такой, какой должен быть на текущем шаге
//                assertEquals(key, lvlData.get("levelTitle"));
//                // проверяем, что продаж до следующего уровня: (продажи до уровня - набежавшие продажи)
//                assertEquals(starsCount * starPrice - totalSales, (Integer) lvlData.get("salesToNextLevel"));
//
//                // накручиваю продажи до уровня
//                webhookForSales.fullSales(userId, sales[5], null);
//                totalSales += sales[5];
            }
        }
//        String[] keys = (String[]) ((JSONArray) lvlTable.get("keys")).toArray();
//        for(String str:keys) System.out.println(str);
    }

}
