package io.thrive.fs.api.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.thrive.fs.api.requests.Auth;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;


public class AuthMethods {
    private Auth auth;
    public AuthMethods(String baseUrl){
        auth = new Auth(baseUrl);
    }

    /**
     *
     * @param username
     * @param password
     * @return
     * {
     *   "accessToken": "string",
     *   "expirationDate": "2022-09-13T08:13:09.635Z",
     *   "userId": 0
     * }
     */
    public JSONObject userLogin(String username, String password){
        Response response = auth.postAuthUser(username, password);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(201, statusCode);
        return response.getBody().as(JSONObject.class);
    }

    /**
     *
     * @param username
     * @param password
     * @return
     * {
     *   "accessToken": "string",
     *   "expirationDate": "2022-09-13T08:13:09.635Z",
     *   "userId": 0
     * }
     */
    public JSONObject adminLogin(String username, String password){
        Response response = auth.postAuthAdmin(username, password);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(201, statusCode);
        return response.getBody().as(JSONObject.class);
    }

}
