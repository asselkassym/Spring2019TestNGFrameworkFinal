package com.vytrack.tests.API_Tests;

import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class JsonValidationOfRegions {

    /*
    Given Content type is JSON
    And Limit is 10
    When I send request to REST API url http://18.208.222.87:1000/ords/hr/regions
    Then I should see the following data:
	1. Europe
	2. Americas
	3. Asia
	4. Middle East and Africa
     */
    @Test
    public void testRegions(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/regions";

        Response response = given().accept(ContentType.JSON)
                            .and().params("limit", 10)
                            .when().get(url);

        Assert.assertEquals(response.statusCode(),200);

        JsonPath json = response.jsonPath();

        String[] regionId = {"1","2","3","4"};
        String[] regionName = {"Europe", "Americas", "Asia", "Middle East and Africa"};

        for(int i=0; i<regionId.length; i++) {
            Assert.assertEquals(json.getString("items["+i+"].region_id"), regionId[i]);
            Assert.assertEquals(json.getString("items["+i+"].region_name"), regionName[i]);
        }
    }

    @Test
    public void testRegions2(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/regions";

        Response response = given().accept(ContentType.JSON)
                .and().params("limit", 10)
                .when().get(url);

        Assert.assertEquals(response.statusCode(),200);

        JsonPath json = response.jsonPath();

        //de-serialize json to List<Map>
        List<Map> regions = json.getList("items", Map.class);

        //every time we say regions.get(0) = we are getting the Map object
        Map r = regions.get(0);

        Map<Integer, String> expectedRegions = new HashMap<>();
        expectedRegions.put(1, "Europe");
        expectedRegions.put(2, "Americas");
        expectedRegions.put(3, "Asia");
        expectedRegions.put(4, "Middle East and Africa");

        for(Integer regionId : expectedRegions.keySet()){
            System.out.println("Looking for region: "+ regionId);
            for(Map eachRegion : regions){
                if(eachRegion.get("region_id")==regionId){
                    Assert.assertEquals(eachRegion.get("region_name"), expectedRegions.get(regionId));
                }
            }
        }

    }
}
