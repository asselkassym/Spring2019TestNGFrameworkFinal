package com.vytrack.tests.API_Tests;

import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class API_day3_jsonpath {

    /*
    Given Accept type is json
    When I send a GET request to REST url http://54.167.93.43:1000/ords/hr/regions
    Then response status code should be 200
    And Response content should be json
    And 4 regions should be returned
    And Americas is one of the region names
     */

    //validation of multiple values in response json
    @Test
    public void testItemsCountFromResponseBody(){
        given().accept(ContentType.JSON)
        .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/regions")
        .then().assertThat().statusCode(200)
        .and().assertThat().contentType(ContentType.JSON)
        //here we need to provide address starting from the root element
        .and().assertThat().body("items.region_id", hasSize(4))
        .and().assertThat().body("items.region_name", hasItem("Americas")); //hasItem accepts only 1 item
        //.and().assertThat().body("items.region_name", hasItems("Americas","Asia")); //hasItems accepts multiple items, because it uses varags as argument
    }

    /*
    Given Accept type is json
    And Params are limit 100
    When I send GET request to http://54.167.93.43:1000/ords/hr/employees
    Then response status code should be 200
    And Response content should be json
    And 100 employees data should be returned in json response body
     */

    @Test //make sure it comes from testNG
    public void testWithQueryParameterAndList(){
        given().accept(ContentType.JSON)
        .and().params("limit",100)
        .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees")
        .then().assertThat().statusCode(200)
        .and().assertThat().contentType(ContentType.JSON)
        .and().assertThat().body("items.employee_id", hasSize(100));
    }

    /*
    Given Accept type is json
    And Params are limit 100
    And path param is 110
    When I send GET request to http://54.167.93.43:1000/ords/hr/employees
    Then response status code should be 200
    And Response content should be json
    And Jhon Chen's data should be returned
     */
    @Test
    public void testWithPathParameters(){
        given().accept(ContentType.JSON)
        .and().params("limit",100)
        .and().pathParams("employee_id", 110)
        .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees/{employee_id}")
        .then().assertThat().statusCode(200)
        .and().assertThat().contentType(ContentType.JSON)
        .and().assertThat().body("employee_id", equalTo(110),
                                 "first_name", equalTo("John"),
                                 "last_name", equalTo("Chen"),
                                 "email", equalTo("JCHEN") );
    }

    /*
    Given Accept type is json
    And Params are limit 100
    When I send GET request to http://54.167.93.43:1000/ords/hr/employees
    Then response status code should be 200
    And Response content should be json
    And all employee ids should be returned (I want to store all ids asList
     */
    @Test
    public void testWithJsonPath(){
        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 100);

        //we are getting the response and assigning it to this variable
        Response response = given().accept(ContentType.JSON) //header
                            .and().params(rParamMap) //query param/request param
                            .and().pathParams("employee_id", 177) //path param
                            .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees/{employee_id}");

        //here we are saying get json body and assign it yo jsonpath object
        JsonPath json = response.jsonPath();

        System.out.println(json.getInt("employee_id"));
        System.out.println(json.getString("last_name"));
        System.out.println(json.getString("job_id"));
        System.out.println(json.getInt("salary"));
        //we put index to links, because links is an array, but href is just a value of the array
        System.out.println(json.getString("links[0].href")); //to get only the first link
        //we use upper statement to get specific element from array

        //assign all href into a List of strings
        List<String> hrefs = json.getList("links.href");
        System.out.println(hrefs);
    }

    /*
    Given Accept type is json
    And Params are limit 100
    When I send GET request to http://18.208.222.87:1000/ords/hr/employees
    Then response status code should be 200
    And Response content should be json
    And all employee data should be returned (I want to store all in List)
     */
    @Test
    public void testJsonPathWithLists(){
        Map<String, Integer> rParamMap = new HashMap<>();
        rParamMap.put("limit", 100);


        Response response = given().accept(ContentType.JSON) //header
                .and().params(rParamMap) //query param/request param
                .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees");

        Assert.assertEquals(response.statusCode(),200); //to check status code

        //First way to use JsonPath
        JsonPath json = response.jsonPath();

        //Second way
        //JsonPath json = new JsonPath(response.asString());

        //Third way
        //JsonPath json = new JsonPath(new File(FilePath.json));

        //get all employee ids into ArrayList
        List<Integer> empIds = json.getList("items.employee_id");
        System.out.println(empIds);
        //assert that there are 100 employee ids
        Assert.assertEquals(empIds.size(), 100);

        //get all employee emails and assign into ArrayList
        List<String> empEmails = json.getList("items.email");
        System.out.println(empEmails);
        Assert.assertEquals(empEmails.size(), 100);

        //get all employee ids that are greater than 150
        List<Integer> empIdsMoreThan150 = json.getList("items.findAll{it.employee_id>150}.employee_id");
        System.out.println(empIdsMoreThan150);

        //get all employees last name whose salary > 15000
        List<String> empSalaryMoreThan7000 = json.getList("items.findAll{it.salary>15000}.last_name");
        System.out.println(empSalaryMoreThan7000);

        //get data from saved JSON file
        JsonPath jsonFromFile = new JsonPath(new File("/Users/asselkassymbekova/Desktop/employees.json"));
        List<String> empEmails1 = jsonFromFile.getList("items.email");
        System.out.println(empEmails1);

    }


}
