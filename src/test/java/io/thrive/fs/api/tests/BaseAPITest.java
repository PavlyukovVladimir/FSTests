package io.thrive.fs.api.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.thrive.fs.help.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.filters;


public class BaseAPITest {
    private static final Logger log = LoggerFactory.getLogger(BaseAPITest.class);
    private String baseURL;
    public BaseAPITest() {
        this.baseURL = Constants.BASE_URL + "api/";
        filters(new AllureRestAssured());
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    @BeforeAll
    public static void setUpScenario(){
        log.info("Scenario \"" + "" + "\"");
    }
}
