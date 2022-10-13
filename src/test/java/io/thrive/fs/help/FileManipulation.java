package io.thrive.fs.help;

import io.qameta.allure.Step;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;

import java.io.*;

public class FileManipulation {
    @Step("Сохраняю креды пользователя: mail {email}, pass {password}")
    @DisplayName("Сохраняю креды пользователя")
    public static void saveUserLoginData(String email, String password) throws IOException {
        File file = new File("src/test/resources","user.json");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter= new FileWriter(file, false);
        fileWriter.write(String.format("{\n\t\"email\": \"%s\",\n\t\"password\": \"%s\"\n}", email, password));
        fileWriter.flush();
        fileWriter.close();
    }

    @Step("Получаю креды пользователя")
    @DisplayName("Получаю креды пользователя")
    public static JSONObject loadUserLoginData() throws IOException, ParseException {
        File file = new File("src/test/resources","user.json");
        String fileStr;
        try (FileInputStream inputStreamReader = new FileInputStream(file)) {
            fileStr = new String(inputStreamReader.readAllBytes());
        }
//        System.out.println(fileStr);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileStr);
//        System.out.println(jsonObject.get("email"));
//        System.out.println(jsonObject.get("password"));
        return jsonObject;
    }

    @Step("Получаю таблицу уровней")
    @DisplayName("Получаю таблицу уровней")
    public static JSONObject loadLvlsTable() throws IOException, ParseException {
        File file = new File("src/test/resources","lvls.json");
        String fileStr;
        try (FileInputStream inputStreamReader = new FileInputStream(file)) {
            fileStr = new String(inputStreamReader.readAllBytes());
        }
//        System.out.println(fileStr);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileStr);
//        System.out.println(jsonObject.get("email"));
//        System.out.println(jsonObject.get("password"));
        return jsonObject;
    }
}
