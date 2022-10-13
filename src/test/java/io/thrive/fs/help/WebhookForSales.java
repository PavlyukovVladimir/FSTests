package io.thrive.fs.help;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class WebhookForSales {

    @Step("Регистрация продажи: userId {userId}, status {status}, transactionNumber {transactionNumber}")
    @DisplayName("Регистрация продажи")
    public void registrationOfSale(long userId, @NotNull String status, @NotNull String transactionNumber, String url) throws IOException {
        if (url == null) {
            url = Constants.BASE_URL + "api/hotmart/webhook/request-event";
        }

        String src = new String(Base64.getEncoder().encode(("{\"userId\": " + userId + "}").getBytes()));
        String body = Files.readString(Path.of("src/test/resources/sales.webhook.json"));
        body = body.replace("{{src}}", src);
        body = body.replace("{{status}}", status);
        body = body.replace("{{transaction}}", transactionNumber);
        body = body.replace("{{transaction}}", transactionNumber);

        Response response = RestAssured.given()
                .baseUri(url)
                .headers("Content-Type", "application/json",
                        "Accept", "application/json")
                .body(body)
                .when()
                .log()
                .all()
                .post();
        response.then()
                .log()
                .all();
    }

    @Step("Регистрирую только подтвержденную продажу")
    @DisplayName("Регистрация только подтвержденной продажи")
    public void approvedSale(long userId, String transactionNumber, String status, String url) throws IOException {
        if (status == null) {
            status = "approved";
        }

        if (transactionNumber == null) {
            long timestamp = System.currentTimeMillis() - 10000000000000L;
            transactionNumber = "TS" + timestamp;
        }

        registrationOfSale(userId, status, transactionNumber, url);
    }

    @Step("Регистрирую полное завершение продажи")
    @DisplayName("Регистрация полного завершения продажи")
    public void completedSale(long userId, String transactionNumber, String status, String url) throws IOException {
        if (status == null) {
            status = "completed";
        }

        if (transactionNumber == null) {
            long timestamp = System.currentTimeMillis() - 10000000000000L;
            transactionNumber = "TS" + timestamp;
        }

        registrationOfSale(userId, status, transactionNumber, url);
    }

    @Step("Регистрирую полную продажу")
    @DisplayName("Регистрация полной продажи")
    public void fullSale(long userId, String transactionNumber, String url) throws IOException {
        if (transactionNumber == null) {
            long timestamp = System.currentTimeMillis() - 10000000000000L;
            transactionNumber = "TS" + timestamp;
        }

        registrationOfSale(userId, "approved", transactionNumber, url);
        registrationOfSale(userId, "completed", transactionNumber, url);
    }

    @Step("Регистрирую полные продажи в количестве: {salesCount}")
    @DisplayName("Регистрация полных продаж")
    public void fullSales(long userId, int salesCount, String url) throws IOException {
        for (int i = 0; i < salesCount; i++) {
            long timestamp = System.currentTimeMillis() - 10000000000000L;
            String transactionNumber = "TS" + timestamp;

            registrationOfSale(userId, "approved", transactionNumber, url);
            registrationOfSale(userId, "completed", transactionNumber, url);
        }
    }

    @Test
    public void test() throws IOException {
        fullSales(500L, 10, null);
    }
}
