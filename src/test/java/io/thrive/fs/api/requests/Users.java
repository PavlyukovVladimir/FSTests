package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Users {
    public Users(String url){
        baseUrl = url;
    }
    private String baseUrl;
    private final String endpointUsersRegister = "users/register";
    private final String endpointUsersSetPassword = "users/set-password";
    private final String endpointUsersAll = "users/all";
    private final String endpointUsersPending = "users/pending";
    private final String endpointUsersApprove = "users/Approve";

    /**
     *
     * @param referCode id of the user who created the invitation link
     * @param fullName
     * @param email
     * @param phoneNumber
     * @param countryId
     * @param StateId
     * @param city
     * @return
     *<pre>{@code
     *{
     *   "message": "string"
     *}
     *}</pre>
     */

    public Response postUsersRegister(
            String referCode,
            String fullName,
            String email,
            String phoneNumber,
            int countryId,
            int StateId,
            String city) {
        Map<String,Object> map = new HashMap<>();
        map.put("city", city);
        map.put("stateId", StateId);
        map.put("countryId", countryId);
        map.put("phoneNumber", phoneNumber);
        map.put("email", email);
        map.put("fullName", fullName);
        map.put("referCode", referCode);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointUsersRegister)
                .headers("Accept", ContentType.JSON, "Content-Type", ContentType.JSON)
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
    public Response patchUsersSetPassword(String token, String password) {
        Map<String,String> map = new HashMap<>();
        map.put("password", password);
        map.put("token", token);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointUsersSetPassword)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody.toJSONString())
                .when()
                .log()
                .all()
                .patch();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param adminToken
     * @param isReferral
     * @return
     *<pre>{@code
     *[
     *   {
     *     "id": 0,
     *     "createdAt": "2022-09-12T09:39:39.853Z",
     *     "updatedAt": "2022-09-12T09:39:39.853Z",
     *     "deletedAt": "2022-09-12T09:39:39.853Z",
     *     "confirmedAt": "2022-09-12T09:39:39.853Z",
     *     "fullName": "string",
     *     "nickname": "string",
     *     "email": "string",
     *     "phoneNumber": "string",
     *     "cpf": "string",
     *     "profession": "string",
     *     "birthDate": "2022-09-12T09:39:39.853Z",
     *     "about": "string",
     *     "zip": "string",
     *     "countryId": 0,
     *     "stateId": 0,
     *     "city": "string",
     *     "district": "string",
     *     "street": "string",
     *     "house": "string",
     *     "apartment": "string",
     *     "avatarImageId": 0,
     *     "isLocked": true,
     *     "level": 0,
     *     "levelTitle": "STRIKER_I",
     *     "intStars": 0,
     *     "starThirds": 0
     *   }
     *]
     *}</pre>
     */
    public Response getUsersAll(String adminToken, boolean isReferral, boolean invoicingPermissions) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointUsersAll)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .queryParams("isReferral", isReferral, "invoicingPermissions", invoicingPermissions)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    public Response getUsersPending(String adminToken) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointUsersPending)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    public Response patchUsersApprove(String adminToken, long userId) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointUsersApprove)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("id", userId)
                .when()
                .log()
                .all()
                .patch();
        response.then()
                .log()
                .all();
        return response;
    }
}
