package com.vytrack.tests.JDBCConnection;

import org.testng.annotations.Test;

import java.sql.*;
import java.util.*;


public class JDBCConnection {

    String oracleDbUrl = "jdbc:oracle:thin:@ec2-3-15-187-120.us-east-2.compute.amazonaws.com:1521:xe";
    String oracleDbPassword = "hr";
    String oracleDbUsername = "hr";

    @Test
    public void oracleJDBC() throws SQLException {
        Connection connection = DriverManager.getConnection(oracleDbUrl, oracleDbUsername, oracleDbPassword);
        //Statement statement = connection.createStatement(); //by default oracle created forward only ResultSet
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from countries");

//        while (resultSet.next()) {
//            System.out.println(resultSet.getString(1) + "-" + resultSet.getString("country_name") + "-" + resultSet.getInt("region_id"));
//        }

        //find out how many records in the resultSeet
        resultSet.last();
        int rowsCount = resultSet.getRow();
        System.out.println("NUmber of rows: "+ rowsCount);

        //now it will not print anything because we are on last row
        //we need to change it by adding the following
        //beforeFirst will go to row 0, then next() will point to row 1
        resultSet.beforeFirst();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "-" + resultSet.getString("country_name") + "-" + resultSet.getInt("region_id"));
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void jdbcMetaData() throws Exception {
        Connection connection = DriverManager.getConnection(oracleDbUrl, oracleDbUsername, oracleDbPassword);
        //Statement statement = connection.createStatement(); //by default oracle created forward only ResultSet
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        //String sql = "select employee_id, last_name, job_id, salary from employees";
        String sql = "select * from employees";

        ResultSet resultSet = statement.executeQuery(sql);

        //Database metadata
        DatabaseMetaData dbMetadata = connection.getMetaData();
        System.out.println("User: "+ dbMetadata.getUserName());
        System.out.println("Database type: "+ dbMetadata.getDatabaseProductName());

        //resultSet metadata
        ResultSetMetaData rsMetadata = resultSet.getMetaData();
        System.out.println(rsMetadata.getColumnCount());
        System.out.println(rsMetadata.getColumnName(1));

        //print all column names using a loop
        for (int i=1; i<=rsMetadata.getColumnCount(); i++){
            System.out.println(i+" -> "+rsMetadata.getColumnName(i));
        }


        //throw resultSet into a List of Maps
        //create a List of Maps
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData rsMdata = resultSet.getMetaData();

        int colCount = rsMdata.getColumnCount();
        while(resultSet.next()){
            Map<String, Object> rowMap = new HashMap<>();

            for (int col = 1; col <= colCount; col++) {
                rowMap.put(rsMdata.getColumnName(col), resultSet.getObject(col));
            }

            list.add(rowMap);
        }

        //print all employee ids from a list of map

        for (Map<String, Object> row: list) {
            System.out.println(row.get("EMPLOYEE_ID"));
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
