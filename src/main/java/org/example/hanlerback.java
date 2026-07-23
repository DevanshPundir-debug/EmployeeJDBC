//package org.example;
//
//import java.sql.*;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class DBHandler {
//
//    static String url = "jdbc:mysql://localhost:3306/employees"; // database ka url represent kregi baad m
//    static String username = "root";
//    static String password = "newpassword123";
//
//    static Connection con;
//    static Statement stmt;
//
//    // field pe direct getConnection nahi likh sakte kyunki SQLException checked hai,
//    // isliye static block mein try/catch ke saath initialize karna padta hai
//    static {
//        try {
//            con = DriverManager.getConnection(url, username, password);
//            // ye line kehri hai deivermanager ek connection build kro url ye hai url se db ka address mila
//            // user and pass ye hai , and connection build ho jae toh ek con object return kr dena
//
//            stmt = con.createStatement(); //con  yaha ek statement object bana do
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static ResultSet selectEmployees() throws SQLException {
//        String sqlQuery = QueryBuilder.buildSelectAllQuery("employees") + " LIMIT 5";
//        return stmt.executeQuery(sqlQuery);
//    }
//
//    public static ResultSet selectEmployeeById(int empNo) throws SQLException {
//        String sqlQuery = QueryBuilder.buildSelectQuery("employees", "emp_no");
//        PreparedStatement ps = con.prepareStatement(sqlQuery);
//        ps.setInt(1, empNo);
//        return ps.executeQuery();
//    }
//
//    public static String insertEmployee(
//            String query) throws SQLException {
//        PreparedStatement ps = con.prepareStatement(query);
//
//        int rows = ps.executeUpdate();
//
//        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//        response.put("rowsAffected", rows);
//        response.put("success", rows > 0);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.writeValueAsString(response);
//        } catch (Exception e) {
//            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
//        }
//    }
//
//    public static void updateEmployee(int empNo, String firstName) throws SQLException {
//        HashMap<String, Object> data = new LinkedHashMap<>();
//        data.put("first_name", firstName);
//
//        String sqlQuery = QueryBuilder.buildUpdateQuery("employees", data, "emp_no");
//        PreparedStatement ps = con.prepareStatement(sqlQuery);
//        ps.setString(1, firstName);
//        ps.setInt(2, empNo);
//
//        int rows = ps.executeUpdate();
//        System.out.println(rows);
//    }
//
//    public static void deleteEmployee(int empNo) throws SQLException {
//        String sqlQuery = QueryBuilder.buildDeleteQuery("employees", "emp_no");
//        PreparedStatement ps = con.prepareStatement(sqlQuery);
//        ps.setInt(1, empNo);
//
//        int rows = ps.executeUpdate();
//        System.out.println(rows);
//
//
//
//
//    }
//
//}