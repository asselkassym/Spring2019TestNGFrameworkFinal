package com.vytrack.tests.API_Tests;

import com.vytrack.beans.Countries;
import com.vytrack.beans.CountriesResponse;
import com.vytrack.beans.Region;
import com.vytrack.beans.RegionResponse;
import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class API_day4_POST_Requests {

    /*
    Given content type is Json
    And Accept type is Json
    When I send POST request to http://18.208.222.87:1000/ords/hr/regions
    With request body:
    {
	    “region_id” : 5,
	    “region_name” : “Assel’s region”
    }
    Then status code should be 200
    And response body should match request body
     */
    @Test
    public void postNewRegion(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/regions/";
        //this is hard-coded way to do it(possible, but not recommended), we will use different way
        //String requestJson = "{\"region_id\" : 5,\"region_name\" : \"Assel’s region\"}";

        Map requestMap = new HashMap<>();
        requestMap.put("region_id", "5");
        requestMap.put("region_name", "assel's land");

        Response response = given().accept(ContentType.JSON)
                            .and().contentType(ContentType.JSON)
                            .and().body(requestMap)
                            .when().post(url);
        System.out.println(response.statusLine());
        response.prettyPrint();

        //assert that status code is 200
        Assert.assertEquals(response.statusCode(), 201);

        //to be able to assert that response body matches request body we need de-serialize
        Map responseMap = response.body().as(Map.class);
        //Assert.assertEquals(requestMap, responseMap); //did not work

//        Integer actualId = (Integer) responseMap.get("region_id");
//        Integer expectedId = (Integer) requestMap.get("region_id");
//
        //Assert.assertEquals(actualId,expectedId);
        assertEquals(responseMap.get("region_id"),requestMap.get("region_id"));
        assertEquals(responseMap.get("region_name"), requestMap.get("region_name"));
    }
    @Test
    public void postWithPojo(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/regions/";
        Region region = new Region();
        region.setRegionId(new Random().nextInt(10));
        region.setRegionName("assel's region");

        Response response = given().log().all()
                .accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                //we provide object of region in request body
                .and().body(region)
                .when().post(url);
        assertEquals(response.statusCode(), 201);
        RegionResponse responseRegion = response.body().as(RegionResponse.class);

        //assertions

        assertEquals(region.getRegionId(),responseRegion.getRegionId());
        assertEquals(region.getRegionName(), responseRegion.getRegionName());
    }

    @Test
    public void postWithPojo2(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/countries/";
        Countries country = new Countries();
        country.setCountryId("AA");
        country.setCountryName("A country");
        country.setRegionId(4);

        Response response = given().log().all()
                .accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                //we provide object of region in request body
                .and().body(country)
                .when().post(url);
        assertEquals(response.statusCode(), 201);
        CountriesResponse responsecountry = response.body().as(CountriesResponse.class);

        //assertions

        assertEquals(country.getCountryId(), responsecountry.getCountryId());
        assertEquals(country.getCountryName(), responsecountry.getCountryName());
        assertEquals(country.getRegionId(), responsecountry.getRegionId());
    }
}
