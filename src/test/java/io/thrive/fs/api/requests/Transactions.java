package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Transactions {
    public Transactions(String url){
        baseUrl = url;
    }
    private String baseUrl;
    private final String endpointTransactionsHistory = "transactions/history";
    private final String endpointTransactionsHistoryAdmin = "transactions/history/admin";
    private final String endpointTransactionsCommissionForTodayAdmin = "transactions/commission-for-today/admin";
    private final String endpointTransactionsBalance = "transactions/balance";
    private final String endpointTransactionsPaymentRequest = "transactions/payment-request";
    private final String endpointTransactionsTotalPendingPayoutAmountAdmin =
            "transactions/total-pending-payout-amount/admin";


    /**
     * @param accessToken
     * @param startDate template: "2022-09-06T16:57:43.413Z"
     * @param endDate template: "2022-09-06T16:57:43.413Z"
     * @return Returns transactions history
     *<pre>{@code
     *[
     *  {
     *    "id": 0,
     *    "createdAt": "2022-09-06T16:57:43.413Z",
     *    "updatedAt": "2022-09-06T16:57:43.413Z",
     *    "deletedAt": "2022-09-06T16:57:43.413Z",
     *    "amount": 0,
     *    "type": "string",
     *    "currency": "string",
     *    "status": "Successes"
     *  }
     *]
     *}</pre>
     */
    public Response getStripeTransactionsHistory(String accessToken, String startDate, String endDate){
        // date template: "2022-09-06T16:57:43.413Z"
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsHistory)
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
     * @param adminToken
     * @param startDate template: "2022-09-06T16:57:43.413Z"
     * @param endDate template: "2022-09-06T16:57:43.413Z"
     * @return Returns transactions history
     *<pre>{@code
     *[
     *  {
     *    "id": 0,
     *    "createdAt": "2022-09-06T16:57:43.413Z",
     *    "updatedAt": "2022-09-06T16:57:43.413Z",
     *    "deletedAt": "2022-09-06T16:57:43.413Z",
     *    "amount": 0,
     *    "type": "string",
     *    "currency": "string",
     *    "status": "Successes"
     *    "userId": 0,
     *    "fullName": "string",
     *    "nickname": "string",
     *    "email": "string",
     *    "phoneNumber": "string"
     *  }
     *]
     *}</pre>
     */
    public Response getStripeTransactionsHistoryAdmin(String adminToken, String startDate, String endDate){
        // date template: "2022-09-06T16:57:43.413Z"
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsHistoryAdmin)
                .queryParams("startDate", startDate, "endDate", endDate)
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
     * @return Returns amount for paid
     *<pre>{@code
     *{
     *  "total": 0,
     *}
     *}</pre>
     */
    public Response getTransactionsCommissionForTodayAdmin(String adminToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsCommissionForTodayAdmin)
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
     * @return Returns user's balance
     *<pre>{@code
     *{
     *  "amount": 0,
     *  "currency": "string"
     *}
     *}</pre>
     */
    public Response getTransactionsBalance(String accessToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsBalance)
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
     * @return Returns status of payment request
     *<pre>{@code
     *[
     *  {
     *    "message": "string"
     *  }
     *]
     *}</pre>
     */
    public Response postTransactionsPaymentRequest(String accessToken, Double amount){
        Map<String,String> map = new HashMap<>();
        map.put("amount", amount.toString());
        JSONObject requestBody = new JSONObject(map);

        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsPaymentRequest)
                .headers("Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON)
                .auth().oauth2(accessToken)
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
     * @return Returns pending payout amount
     *<pre>{@code
     *{
     *  "amount": 0,
     *  "currency": "string"
     *  "usersAmount": 0,
     *  "paymentIdHash": "string",
     *  "fee": 0,
     *  "total": 0
     *}
     *}</pre>
     */
    public Response getTransactionsTotalPendingPayoutAmountAdmin(String adminToken){
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointTransactionsTotalPendingPayoutAmountAdmin)
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
