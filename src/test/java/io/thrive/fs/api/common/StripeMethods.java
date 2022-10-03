package io.thrive.fs.api.common;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.thrive.fs.api.requests.Stripe;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

//import org.junit.jupiter.api.Assertions;

public class StripeMethods {
    private Stripe stripe;
    public StripeMethods(String baseUrl){
        stripe = new Stripe(baseUrl);
    }

    /**
     * @return status ["Unregistered", "Pending", "Registered"]
     */
    public String getStripeConnectStatus(String accessToken){
        Response response = stripe.getStripeConnectStatus(accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return (String)response.getBody().as(JSONObject.class).get("status");
    }

    /**
     * @return
     * accountLink
     */
    public String postStripeConnectRegister(String adminToken){
        Response response = stripe.postStripeConnectRegister(adminToken);
        response.then()
                .statusCode(HttpStatus.SC_CREATED) // 201
                .contentType(ContentType.JSON);
        return (String)response.getBody().as(JSONObject.class).get("accountLink");
    }

    /**
     * @return accountLink
     */
    public String getStripeConnectAuthLink(String accessToken){
        Response response = stripe.getStripeConnectAuthLink(accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return (String)response.getBody().as(JSONObject.class).get("accountLink");
    }

    /**
     * @return
     *<pre>{@code
     * {
     *   "amount": "Integer",
     *   "currency": "string"
     * }
     *}</pre>
     * @type JSONObject
     */
    public JSONObject getStripeConnectBalance(String accessToken){
        Response response = stripe.getStripeConnectBalance(accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().as(JSONObject.class);
    }

    /**
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
     * @type List&lt;JSONObject&gt;
     */
    public List<JSONObject> getStripePaymentsHistory(String accessToken, String startDate, String endDate) {
        Response response = stripe.getStripePaymentsHistory(accessToken, startDate, endDate);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getList("$", JSONObject.class);
    }

    /**
     * @param paymentIdHash String
     * @return paymentLink
     */
    public String postStripePaymentsPayout(String adminToken, String paymentIdHash) {
        Response response = stripe.postStripePaymentsPayout(adminToken, paymentIdHash);
        response.then()
                .statusCode(HttpStatus.SC_CREATED) // 201
                .contentType(ContentType.JSON);
        return (String)response.getBody().as(JSONObject.class).get("paymentLink");
    }

    public String postStripePaymentsCancelPayout(String adminToken, String paymentIdHash) {
        Response response = stripe.postStripePaymentsCancelPayout(adminToken, paymentIdHash);
        response.then().statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return (String)response.getBody().as(JSONObject.class).get("message");
    }

    public List<Map> postStripePaymentsStatus(String adminToken) {
        Response response = stripe.postStripePaymentsStatus(adminToken);
        response.then().statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getList("$", Map.class);
    }

    public List<Map> postStripePaymentsLink(String adminToken) {
        Response response = stripe.postStripePaymentsLink(adminToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getList("$", Map.class);
    }
}
