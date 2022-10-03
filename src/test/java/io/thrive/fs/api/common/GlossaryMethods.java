package io.thrive.fs.api.common;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.thrive.fs.api.requests.Glossary;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.List;

public class GlossaryMethods{
    private Glossary glossary;
    public GlossaryMethods(String baseUrl){
        glossary = new Glossary(baseUrl);
    }

    /**
     * @return
     *<pre>{@code
     *[
     *  {
     *    "id": 0,
     *    "name": "string",
     *    "states": [
     *      {
     *        "id": 0,
     *        "name": "string",
     *        "code": "string"
     *      }
     *    ],
     *    "code": "string"
     *  }
     *]
     *}</pre>
     * @type List&lt;JSONObject&gt;
     */
    public List<JSONObject> getStripePaymentsHistory() {
        Response response = glossary.getGlossaryCountries();
        response.then()
                .assertThat().statusCode(HttpStatus.SC_OK) // 200
                .contentType(ContentType.JSON);
        return response
                .getBody()
                .jsonPath()
                .getList("$", JSONObject.class);
    }
}
