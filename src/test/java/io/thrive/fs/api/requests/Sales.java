package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.thrive.fs.help.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class Sales{
    public Sales(String url) {
        baseUrl = url;
    }

    private final String baseUrl;
    private final String endpointSalesSummary = "sales/summary";
    private final String endpointSalesAll = "sales/all";
    private final String endpointSalesChild = "sales/child";
    private final String endpointSalesUserLevel = "sales/user-level";
    private final String endpointSalesUserStatistic = "sales/user-statistic";
    private final String endpointSalesGroupStatistic = "sales/group-statistic";
    private final String endpointSalesStatisticsNumberSales = "sales/statistics/number-sales";
    private final String endpointSalesStatisticsNumberSalesToday = "sales/statistics/number-sales/today";
    private final String endpointSalesTest = "sales/test";



    /**
     * @param accessToken
     * @param startDate
     * @param endDate
     * @return <pre>{@code
     * {
     *   "history": [
     *     {
     *       "date": "2022-10-13T12:53:55.989Z",
     *       "numberOfSales": 0
     *     }
     *   ],
     *   "total": 0
     * }
     * }</pre>
     */
    public Response getSalesSummary(String accessToken, String startDate, String endDate) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointSalesSummary)
                .header("Accept", "application/json")
                .auth().oauth2(accessToken)
                .queryParams("startDate", startDate, "endDate", endDate)
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
     * @return <pre>{@code
     * [
     *   {
     *     "id": 0,
     *     "createdAt": "2022-10-13T12:59:00.223Z",
     *     "updatedAt": "2022-10-13T12:59:00.223Z",
     *     "deletedAt": "2022-10-13T12:59:00.223Z",
     *     "hotmartTransactionCode": "string",
     *     "purchaseDate": "2022-10-13T12:59:00.223Z",
     *     "productName": "string",
     *     "price": 0,
     *     "clientName": "string",
     *     "status": "string",
     *     "warrantyDate": "2022-10-13T12:59:00.223Z",
     *     "paymentType": "string"
     *   }
     * ]
     * }</pre>
     */
    public Response getSalesAll(String adminToken) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointSalesAll)
                .headers("Accept", "application/json")
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
     * @param adminToken
     * @param hotmartTransactionCode
     * @return <pre>{@code
     * [
     *   {
     *     "id": 0,
     *     "createdAt": "2022-10-13T13:03:29.899Z",
     *     "updatedAt": "2022-10-13T13:03:29.900Z",
     *     "deletedAt": "2022-10-13T13:03:29.900Z",
     *     "hotmartTransactionCode": "string",
     *     "purchaseDate": "2022-10-13T13:03:29.900Z",
     *     "productName": "string",
     *     "price": 0,
     *     "clientName": "string",
     *     "status": "string",
     *     "warrantyDate": "2022-10-13T13:03:29.900Z",
     *     "paymentType": "string"
     *   }
     * ]
     * }</pre>
     */
    public Response getSalesChild(String adminToken, String hotmartTransactionCode) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointSalesChild)
                .header("Accept", "application/json")
                .auth().oauth2(adminToken)
                .queryParam("hotmartTransactionCode", hotmartTransactionCode)
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
     * @param accessToken
     * @param sales <pre>{@code
     * [
     *    {
     *        "numberOfSales": 0,
     *        "weeksAgo": 0
     *    }
     * ]
     *}</pre>
     * @return <pre>{@code
     * {
     *   "level": 0,
     *   "levelTitle": "GOAL_GETTER_I",
     *   "intStars": 0,
     *   "starThirds": 0
     * }
     * }</pre>
     */
    public Response postUsersRegister(String accessToken, JSONArray sales) {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointSalesTest)
                .headers("Accept", ContentType.JSON, "Content-Type", ContentType.JSON)
                .auth().oauth2(accessToken)
                .body(sales.toJSONString())
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
