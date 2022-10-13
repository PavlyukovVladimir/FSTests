package io.thrive.fs.api.tests;

import com.google.gson.JsonArray;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static io.thrive.fs.help.FileManipulation.loadLvlsTable;
import static io.thrive.fs.help.FileManipulation.saveUserLoginData;


@Epic("Проверка системы уровней")
public class APILvlsTest extends BaseAPITest {
    private final GlossaryMethods glossaryMethods = new GlossaryMethods(getBaseURL());
    private final AuthMethods authMethods = new AuthMethods(getBaseURL());
    private final UsersMethods usersMethods = new UsersMethods(getBaseURL());
    private final DataGenerator dataGenerator = new DataGenerator();

    //</editor-fold>
//<editor-fold desc="Tests">
    @Test
    @Story("Последовательное повышение уровня с 0 в течении дня")
    @DisplayName("Последовательное повышение уровня с 0 в течении дня")
    @Description("Последовательное повышение уровня с 0 в течении дня, с помощью API")
    public void registrationWithoutReferCodeScenarioTest() throws IOException, ParseException {
        JSONObject lvlTable = loadLvlsTable();
//        System.out.println(lvlTable);
        JSONArray keys = (JSONArray) lvlTable.get("keys");
        int sales = 0;
        for(Object key:keys){

            System.out.println((String) key);
        }
//        String[] keys = (String[]) ((JSONArray) lvlTable.get("keys")).toArray();
//        for(String str:keys) System.out.println(str);
    }

}
