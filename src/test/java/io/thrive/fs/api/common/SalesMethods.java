package io.thrive.fs.api.common;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.thrive.fs.api.requests.Sales;
import io.thrive.fs.api.requests.Users;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class SalesMethods {
    private Sales sales;
    public SalesMethods(String baseUrl){
        sales = new Sales(baseUrl);
    }

    @Step("Получаем данные о текущем уровне")
    /**
     *
     * @param accessToken
     */
    public JSONObject salesUserLevel(String accessToken){
        Response response = sales.getSalesUserLevel(accessToken);
        return response.then()
                .assertThat().statusCode(HttpStatus.SC_OK) // 200
                .extract().as(JSONObject.class);
    }

//    @Step("401 Ошибка при попытке установить пароль пользователя")
//    public void usersSetPassword401(String token, String password){
//        Response response = users.patchUsersSetPassword(token, password);
//        response.then()
//                .statusCode(HttpStatus.SC_UNAUTHORIZED) // 401
//                .contentType(ContentType.JSON)
//                .body("message", Matchers.equalTo("Invalid token"));
//    }

}
