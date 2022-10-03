package io.thrive.fs.api.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class Glossary {
    public Glossary(String url){
        baseUrl = url;
    }
    private String baseUrl;
    private final String endpointGlossaryCountries = "glossary/countries";


    public Response getGlossaryCountries() {
        Response response = RestAssured.given()
                .baseUri(baseUrl).basePath(endpointGlossaryCountries)
                .headers("Accept", ContentType.JSON)
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
