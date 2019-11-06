package com.vytrack.tests.components.login_navigation;

import com.vytrack.utilities.ExcelUtils;
import com.vytrack.utilities.TestBase;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DDTLoginTests extends TestBase {

    @Test(dataProvider = "credentials_info")
    public void loginTestWithDataProvider(String execute, String username, String password,String firstname, String lastname, String result){
        extentLogger = report.createTest("Data Driven Testing with Excel");
        if(execute.equalsIgnoreCase("y")){
            pages.loginPage().login(username, password);
            String actualFullName = pages.dashboardPage().getUsersFullName();
            String expectedName = firstname+" "+lastname;
            Assert.assertEquals(actualFullName, expectedName, "Name doesn't match");
            pages.dashboardPage().logout();
        }else{
            throw new SkipException("Test ignored");
        }
    }

    @DataProvider(name="credentials_info")
    public Object[][] credentials(){
        ExcelUtils qa2 = new ExcelUtils("QA2-short");
        return  qa2.getDataArray();
    }

}
