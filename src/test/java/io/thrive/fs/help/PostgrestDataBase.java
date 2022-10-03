package io.thrive.fs.help;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class PostgrestDataBase {
    /**
     * Returned user id from postgres database by postgrest WEB API request.
     * <p> Finds the first line when the "email" field is equal to the specified.
     * <p> Returns the value of the "user_id" field in the found string.
     * <p> Returns "null" if nothing founds.
     * <p> Throws assertion exception if found more than one value.
     */
    public long getUserId(String email){
        RequestSpecification request = RestAssured.given()
                .baseUri(Constants.DATA_BASE_URL).basePath("users?select=Id&email=eq." + email)
                .queryParam("select","id")
                .queryParam("email","eq." + email);
        Response response = request.get();
        Assertions.assertEquals(200, response.getStatusCode(), "Status response code not 200");
        String strBody = response.getBody().asString();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(strBody);
            Assertions.assertEquals(1, jsonArray.size(), "More than one match found");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            long rez = (long)jsonObject.get("id");
            return rez;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Returned token from postgres database by postgrest WEB API request.
     * <p> Finds the first row when the "user_id" and "type" fields are equal to the specified.
     * <p> Returns the value of the "token" field in the found string.
     * <p> Returns "null" if nothing founds.
     * <p> Throws assertion exception if found more than one value.
     * Types: [registration, password_reset, user_email_confirmation]
     */
    public String getToken(long userId, String type){
        RequestSpecification request = RestAssured.given()
                .baseUri(Constants.DATA_BASE_URL).basePath("mail_tokens")
                .queryParam("select","token")
                .queryParam("user_id","eq." + userId)
                .queryParam("type","eq." + type);
        Response response = request.get();
        Assertions.assertEquals(200, response.getStatusCode(), "Status response code not 200");
        String strBody = response.getBody().asString();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(strBody);
            if (jsonArray.size() == 0) return null;
            Assertions.assertEquals(1, jsonArray.size(), "More than one match found");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String rez = (String)jsonObject.get("token");
            return rez;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Returned users count from postgres database by postgrest WEB API request.
     */
    public long getUsersCount(){
        ExtractableResponse extractableResponse = RestAssured.given()
                .baseUri(Constants.DATA_BASE_URL).basePath("users")
                .headers("Prefer", "count=exact")
                .when()
                .log()
                .all()
                .head()
                .then()
                .log()
                .all()
                .assertThat().statusCode(HttpStatus.SC_OK) // 200
                .extract();
        int count = Integer.parseInt(extractableResponse
                .header("Content-Range").split("/")[1]);
        return count;
    }

    /**
     *
     * @return
     *[
     *     {
     *         "id": 0,
     *         "created_at": "2022-09-12T13:17:16.681205",
     *         "updated_at": "2022-09-12T13:17:16.681205",
     *         "deleted_at": null,
     *         "confirmed_at": null,
     *         "full_name": "string",
     *         "nickname": "string",
     *         "avatar_image_id": null,
     *         "email": "string",
     *         "password": "string",
     *         "phone_number": "string",
     *         "cpf": null,
     *         "profession": null,
     *         "birth_date": null,
     *         "about": null,
     *         "zip": null,
     *         "country_id": 0,
     *         "state_id": 0,
     *         "city": "São Mateus do Maranhão",
     *         "district": null,
     *         "street": null,
     *         "house": null,
     *         "apartment": null,
     *         "refer_id": 0,
     *         "locked_at": null,
     *         "stripe_account_id": "string"
     *     }
     *]
     */
    public List<JSONObject> getUsers(){
        ExtractableResponse extractableResponse = RestAssured.given()
                .baseUri(Constants.DATA_BASE_URL).basePath("users")
                .queryParams("order", "id.desc")
                .when()
                .log()
                .all()
                .get()
                .then()
                .log()
                .all()
                .assertThat().statusCode(HttpStatus.SC_OK) // 200
                .extract();
        List<JSONObject> responseBody = extractableResponse.body().jsonPath().getList("$", JSONObject.class);
        return responseBody;
    }
}
