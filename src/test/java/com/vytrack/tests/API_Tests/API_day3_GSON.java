package com.vytrack.tests.API_Tests;

import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class API_day3_GSON {

    @Test
    public void testWithJSONToHashMap(){
        //we are sending basic get request to get information of employee with id 120
        //assigning the response to the response class veriable
        Response response = given().accept(ContentType.JSON)
        .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees/120");

        //we are using as() to convert it to HashMap, doing de-serialization
        //GSON makes it possible by running at the back
        Map<String, String> map = response.as(HashMap.class);
        System.out.println(map.keySet());
        System.out.println(map.values());

        Assert.assertEquals(map.get("employee_id"), 120.0);
        Assert.assertEquals(map.get("job_id"), "ST_MAN");
    }

    @Test
    public void convertJsonToListOfMaps(){
        Response response = given().accept(ContentType.JSON)
                .when().get(ConfigurationReader.getProperty("hrapp.baseresturl")+"/departments");
        //convert response that contains department information into List of Maps
        //List<Map<String, String>> departments = response.as(ArrayList.class);
        //we converted to jsonpath first, then getList will give as all items and store the values in Map
        List<Map> departments = response.jsonPath().getList("items", Map.class);
        System.out.println(departments.get(0));
        //assert that first department name is "Administration"
        Assert.assertEquals(departments.get(0).get("department_name"), "Administration");
    }


}
