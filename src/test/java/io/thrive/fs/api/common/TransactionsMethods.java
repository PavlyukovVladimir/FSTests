package io.thrive.fs.api.common;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.thrive.fs.api.requests.Transactions;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.List;

public class TransactionsMethods{
    private Transactions transactions;
    public TransactionsMethods(String baseUrl){
        transactions = new Transactions(baseUrl);
    }

    /**
     * @param accessToken
     * @param startDate   template: "2022-09-06T16:57:43.413Z"
     * @param endDate     template: "2022-09-06T16:57:43.413Z"
     * @return Returns transactions history
     * <pre>{@code
     * [
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
     * ]
     * }</pre>
     * @type List&lt;JSONObject&gt;
     */
    public List<JSONObject> getStripeTransactionsHistory(String accessToken, String startDate, String endDate) {
        Response response = transactions.getStripeTransactionsHistory(accessToken, startDate, endDate);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getList("$", JSONObject.class);
    }

    /**
     * @param adminToken
     * @param startDate  template: "2022-09-06T16:57:43.413Z"
     * @param endDate    template: "2022-09-06T16:57:43.413Z"
     * @return Returns transactions history
     * <pre>{@code
     * [
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
     * ]
     * }</pre>
     * @type List&lt;JSONObject&gt;
     */
    public List<JSONObject> getStripeTransactionsHistoryAdmin(String adminToken, String startDate, String endDate) {
        Response response = transactions.getStripeTransactionsHistoryAdmin(adminToken, startDate, endDate);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getList("$", JSONObject.class);
    }

    /**
     * @return Returns amount for paid
     * @type Double
     */
    public Double getTransactionsCommissionForTodayAdmin(String adminToken) {
        Response response = transactions.getTransactionsCommissionForTodayAdmin(adminToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return (Double) response.getBody().as(JSONObject.class).get("total");
    }

    /**
     * @return Returns user's balance
     * <pre>{@code
     * {
     *  "amount": 0,
     *  "currency": "string"
     * }
     * }</pre>
     * @type JSONObject
     */
    public JSONObject getTransactionsBalance(String accessToken) {
        Response response = transactions.getTransactionsBalance(accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().as(JSONObject.class);
    }

    /**
     * @param accessToken
     * @param amount
     * @return Returns status of payment request
     * @type String;
     */
    public String postTransactionsPaymentRequest(String accessToken, Double amount) {
        Response response = transactions.postTransactionsPaymentRequest(accessToken, amount);
        response.then()
                .statusCode(HttpStatus.SC_CREATED) // 201
                .contentType(ContentType.JSON);
        return response.getBody().jsonPath().getString("@/message");
    }

    /**
     * @return Returns pending payout amount
     * <pre>{@code
     * {
     *  "amount": 0,
     *  "currency": "string"
     *  "usersAmount": 0,
     *  "paymentIdHash": "string",
     *  "fee": 0,
     *  "total": 0
     * }
     * }</pre>
     * @type JSONObject
     */
    public JSONObject getTransactionsTotalPendingPayoutAmountAdmin(String adminToken) {
        Response response = transactions.getTransactionsTotalPendingPayoutAmountAdmin(adminToken);
        response.then()
                .statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response.getBody().as(JSONObject.class);
    }
}
