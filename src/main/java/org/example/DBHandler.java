package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class DBHandler {

    static String url = "jdbc:mysql://localhost:3306/employees"; // database ka url represent kregi baad m
    static String username = "root";
    static String password = "newpassword123";

    static Connection con;
    static Statement stmt;

    // field pe direct getConnection nahi likh sakte kyunki SQLException checked hai,
    // isliye static block mein try/catch ke saath initialize karna padta hai
    static {
        try {
            con = DriverManager.getConnection(url, username, password);
            // ye line kehri hai deivermanager ek connection build kro url ye hai url se db ka address mila
            // user and pass ye hai , and connection build ho jae toh ek con object return kr dena

            stmt = con.createStatement(); //con  yaha ek statement object bana do
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet selectEmployees(String query) throws SQLException {
        return stmt.executeQuery(query);
    }

    public static ResultSet selectEmployeeById(String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeQuery();
    }


    public static String getEmployees(String query) throws SQLException {

        ResultSet rs = stmt.executeQuery(query);

        LinkedHashMap<String, Object> row;
        ArrayList<LinkedHashMap<String, Object>> employees = new ArrayList<>();

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {

            row = new LinkedHashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnName(i), rs.getObject(i));
            }

            employees.add(row);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(employees);
        } catch (Exception e) {
            return "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}";
        }
    }



    public static String insertEmployee(
            String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);

        int rows = ps.executeUpdate();

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("rowsAffected", rows);
        response.put("success", rows > 0);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public static String updateEmployee(String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);

        int rows = ps.executeUpdate();

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("rowsAffected", rows);
        response.put("success", rows > 0);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    public static String deleteEmployee(String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);

        int rows = ps.executeUpdate();

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("rowsAffected", rows);
        response.put("success", rows > 0);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

}
