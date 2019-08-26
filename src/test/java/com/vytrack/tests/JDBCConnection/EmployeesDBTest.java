package com.vytrack.tests.JDBCConnection;

import com.vytrack.utilities.DBType;
import com.vytrack.utilities.DBUtility;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EmployeesDBTest {

    @BeforeClass
    public void setUp()throws SQLException{
        DBUtility.establishConnection(DBType.ORACLE);
    }

    @Test
    public void countTest() throws SQLException {
        //connect to oracle database
        //run following sql query
        //select * from employees where job_id = 'IT_PROG'
        //more than 0 records should be returned
        int rowsCount = DBUtility.getRowsCount("select * from employees where job_id = 'IT_PROG'");
        Assert.assertTrue(rowsCount>0);
    }

    @Test
    public void nameTestByID() throws SQLException{
        //connect to oracle database
        //firstname and lastname of the employee with employee_id 105
        //should be David Austin
        List<Map<String,Object>> empData = DBUtility.runSQLQuery("SELECT first_name,last_name FROM employees WHERE employee_id=105");
        Assert.assertEquals(empData.get(0).get("FIRST_NAME"),"David");
        Assert.assertEquals(empData.get(0).get("LAST_NAME"),"Austin");
    }

    @AfterClass
    public void tearDown(){
        DBUtility.closeConnections();
    }
}
