package com.vytrack.tests.API_Tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.*;

public class HR_RESTAPIGetRequests {


    /**
     * When I send a GET request to http://54.167.93.43:1000/ords/hr/employees
     * Then response status code should be 200
     */
    @Test
    public void simpleGet(){
        when()
            .get("http://54.167.93.43:1000/ords/hr/employees")
            .then()
            .statusCode(200);
    }

    /**
     * When I send a GET request to http://54.167.93.43:1000/ords/hr/countries
     * Then I should see JSON response
     */
    @Test
    public void printResponse(){
        when()
            .get("http://54.167.93.43:1000/ords/hr/employees")
            .body().prettyPrint();
    }

    /**
     * When I send GET request to REST API url to http://54.167.93.43:1000/ords/hr/countries/US
     * And Accept type is “application/json”
     * Then response  status code should be 200
     */
    @Test
    public void getWithHeaders(){
        with().accept(ContentType.JSON)
        .when()
        .get("http://54.167.93.43:1000/ords/hr/countries/US")
        .then().statusCode(200);
    }

    /**
     * When I send a GET request to REST url http://54.167.93.43:1000/ords/hr/employees/1234
     * Then response status code should be 404
     * And Response body error message is "Not Found"
     */
    @Test
    public void negativeGet(){
//        when().get("http://54.167.93.43:1000/ords/hr/employees/1234")
//        .then().statusCode(404);
        Response response = when().get("http://54.167.93.43:1000/ords/hr/employees/1234");
        assertEquals(response.statusCode(),404);
        assertTrue(response.asString().contains("Not Found"));
        //esponse.prettyPrint();
    }

    /**
     * When I send a GET request to REST url http://54.167.93.43:1000/ords/hr/employees/100
     * And accept type is json
     * Then response status code should be 200
     * And Response content should be json
     */
    @Test
    public void verifyContentTypeWithAssertThat(){
        String url = "http://54.167.93.43:1000/ords/hr/employees/100";

        //everything we specify before get is request type
        given().accept(ContentType.JSON)
        .when()
        //here I'm sending my request to that url
        .get(url)

        //everything after get is response
        .then().assertThat().statusCode(200)
        .and().contentType(ContentType.JSON);
    }

    /**
     * Given Accept type is json
     * When I send a GET request to REST url http://54.167.93.43:1000/ords/hr/employees/100
     * Then response status code should be 200
     * And Response content should be json
     * And first name should be "Steven"
     * And employee id is 100
     */
    @Test
    public void verifyFirstName() throws URISyntaxException {
        URI uri = new URI("http://54.167.93.43:1000/ords/hr/employees/100");
        given().accept(ContentType.JSON)
        .when().get(uri)
        .then().assertThat().statusCode(200)
        .and().contentType(ContentType.JSON)
        .and().assertThat().body("first_name", equalTo("Steven"))
        .and().assertThat().body("employee_id", equalTo(100));
    }
}
