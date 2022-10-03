package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Auth {
    public Auth(String url){
        baseUrl = url;
    }
    private String baseUrl;
    private final String endpointAuthUser = "auth/user";
    private final String endpointAuthAdmin = "auth/admin";
    private final String endpointUsersSetPassword = "users/set-password";

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
    public Response postAuthUser(String username, String password){
        Map<String,String> map = new HashMap<>();
        map.put("password", password);
        map.put("username", username);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointAuthUser)
                .headers("Content-Type", "application/json",
                        "Accept", "application/json")
                .body(requestBody.toJSONString())
                .when()
                .log()
                .all()
                .post();
        response.then()
                .log()
                .all();
        return response;
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
    public Response postAuthAdmin(String username, String password){
        Map<String,String> map = new HashMap<>();
        map.put("password", password);
        map.put("username", username);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointAuthAdmin)
                .headers("Content-Type", "application/json",
                        "Accept", "application/json")
                .body(requestBody.toString())
                .when()
                .log()
                .all()
                .post();
        response.then()
                .log()
                .all();
        return response;
    }
}
