package com.vytrack.tests.API_Tests;

import com.vytrack.beans.Countries;
import com.vytrack.beans.CountriesResponse;
import com.vytrack.beans.Employee;
import com.vytrack.beans.EmployeeResponse;
import com.vytrack.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

public class API_day5_POST_then_GET {

    @Test
    public void postEmpThenGetEmp(){
        String url = ConfigurationReader.getProperty("hrapp.baseresturl")+"/employees/";

        Employee emp = new Employee();
        emp.setEmployeeId(5556);
        emp.setFirstName("Asselka");
        emp.setLastName("Kassi");
        emp.setEmail("AKASSI");
        emp.setPhoneNumber("590.423.5544");
        emp.setHireDate("2007-02-07T05:00:00Z");
        emp.setJobId("IT_PROG");
        emp.setSalary(13000);
        emp.setCommissionPct(null);
        emp.setManagerId(103);
        emp.setDepartmentId(60);

        Response response = given().log().all()
                .accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(emp)
                .when().post(url);

        assertEquals(response.statusCode(), 201);

        EmployeeResponse employeeResponse = response.body().as(EmployeeResponse.class);

        //assertions

        assertEquals(emp.getFirstName(), employeeResponse.getFirstName());

        response = given().accept(ContentType.JSON)
                  .when().get(url+emp.getEmployeeId());
        assertEquals(response.statusCode(),200);

        employeeResponse = response.body().as(EmployeeResponse.class);

        assertEquals(emp.getFirstName(), employeeResponse.getFirstName());


    }
}
