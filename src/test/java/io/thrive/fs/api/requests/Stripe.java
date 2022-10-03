package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.oauth2;

public class Stripe {
    public Stripe(String url){
        baseUrl = url;
    }
    private String baseUrl;
    private final String endpointStripeConnectRegister = "stripe/connect/register";
    private final String endpointStripeConnectStatus = "stripe/connect/status";
    private final String endpointStripeConnectAuthLink = "stripe/connect/auth-link";
    private final String endpointStripeConnectBalance = "stripe/connect/balance";
    private final String endpointStripePaymentsHistory = "stripe/payments/history";
    private final String endpointStripePaymentsPayout = "stripe/payments/payout";
    private final String endpointStripePaymentsCancelPayout = "stripe/payments/cancel/payout";
    private final String endpointStripePaymentsStatus = "stripe/payments/status";
    private final String endpointStripePaymentsLink = "stripe/payments/link";

    /**
     *
     * @param accessToken
     * @return
     *<pre>{@code
     *{
     *  "accountLink": "string"
     *}
     *}</pre>
     */
    public Response postStripeConnectRegister(String accessToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripeConnectRegister)
                .header("Accept", "application/json")
                .auth().oauth2(accessToken)
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
     * @param accessToken
     * @return
     *<pre>{@code
     *{
     *  "status": "string"
     *}
     *}</pre>
     */
    public Response getStripeConnectStatus(String accessToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripeConnectStatus)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param accessToken
     * @return
     *<pre>{@code
     *{
     *  "accountLink": "string"
     *}
     *}</pre>
     */
    public Response getStripeConnectAuthLink(String accessToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripeConnectAuthLink)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param accessToken
     * @return
     *<pre>{@code
     *{
     *  "amount": 0,
     *  "currency": "string"
     *}
     *}</pre>
     */
    public Response getStripeConnectBalance(String accessToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripeConnectBalance)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param accessToken
     * @param startDate template: "2022-09-06T16:57:43.413Z"
     * @param endDate template: "2022-09-06T16:57:43.413Z"
     * @return
     *<pre>{@code
     *[
     *  {
     *    "id": 0,
     *    "createdAt": "2022-09-06T16:57:43.413Z",
     *    "updatedAt": "2022-09-06T16:57:43.413Z",
     *    "deletedAt": "2022-09-06T16:57:43.413Z",
     *    "paymentId": "string",
     *    "chargeId": "string",
     *    "status": "Successes",
     *    "deliveredAt": "2022-09-06T16:57:43.413Z"
     *  }
     *]
     *}</pre>
     */
    public Response getStripePaymentsHistory(String accessToken, String startDate, String endDate){
        // date template: "2022-09-06T16:57:43.413Z"
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripePaymentsHistory)
                .queryParams("startDate", startDate, "endDate", endDate)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param adminToken
     * @param paymentIdHash
     * @return
     *<pre>{@code
     *{
     *  "paymentLink": "string"
     *}
     *}</pre>
     */
    public Response postStripePaymentsPayout(String adminToken, String paymentIdHash){
        Map<String,String> map = new HashMap<>();
        map.put("paymentIdHash", paymentIdHash);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripePaymentsPayout)
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON)
                .auth().oauth2(adminToken)
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
     * @param adminToken
     * @param paymentIdHash
     * @return
     *<pre>{@code
     *{
     *  "message": "string"
     *}
     *}</pre>
     */
    public Response postStripePaymentsCancelPayout(String adminToken, String paymentIdHash){
        Map<String,String> map = new HashMap<>();
        map.put("paymentIdHash", paymentIdHash);
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripePaymentsCancelPayout)
                .headers("Accept", ContentType.JSON,
                        "Content-Type", ContentType.JSON)
                .auth().oauth2(adminToken)
                .body(requestBody.toJSONString())
                .when()
                .log()
                .all()
                .put();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param adminToken
     * @return
     *<pre>{@code
     *[
     *  {
     *    "status": true
     *  }
     *]
     *}</pre>
     */
    public Response postStripePaymentsStatus(String adminToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripePaymentsStatus)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(adminToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }

    /**
     *
     * @param adminToken
     * @return
     *<pre>{@code
     *[
     *  {
     *    "status": true
     *  }
     *]
     *}</pre>
     */
    public Response postStripePaymentsLink(String adminToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointStripePaymentsLink)
                .headers("Accept", ContentType.JSON)
                .auth().oauth2(adminToken)
                .when()
                .log()
                .all()
                .get();
        response.then()
                .log()
                .all();
        return response;
    }
}
