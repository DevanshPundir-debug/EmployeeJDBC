//package org.example;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class jdbc_backup {
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
//    public static String getEmployeesAsJson() {
//        return "Coming Soon";
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
//    public static void insertEmployeeByData(
//            int empNo,
//            String birthDate,
//            String firstName,
//            String lastName,
//            String gender
//    ) throws SQLException {
//        // LinkedHashMap kyunki HashMap order guarantee nahi karta — columns aur ps.setXxx ka order match hona zaroori hai
//        HashMap<String, Object> data = new LinkedHashMap<>();
//        data.put("emp_no", empNo);
//        data.put("birth_date", birthDate);
//        data.put("first_name", firstName);
//        data.put("last_name", lastName);
//        data.put("gender", gender);
//        data.put("hire_date", "CURDATE()");
//
//        String sqlQuery = QueryBuilder.buildInsertQuery("employees", data, null);
//        PreparedStatement ps = con.prepareStatement(sqlQuery);
//        ps.setInt(1, empNo);
//        ps.setString(2, birthDate);
//        ps.setString(3, firstName);
//        ps.setString(4, lastName);
//        ps.setString(5, gender);
//        ps.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
//
//        int rows = ps.executeUpdate();
//        System.out.println(rows);
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
//    }
//
//
//    public static void main(String[] args) {
//        try {
//            insertEmployeeByData(500001, "2004-01-01", "Devansh", "Pundir", "M");  // insert
//
//            updateEmployee(500001, "DEV");  // update
//
//            deleteEmployee(500001);  // delete
//
//            insertEmployeeByData(500099, "2005-12-01", "Deva", "Mittal", "M");
//
//            int empNo = 10005;
//
//            ResultSet rs = selectEmployeeById(empNo);
////            ResultSet rs = selectEmployees();
//
//            List<Employee> employees = new ArrayList<>(); // bina list ke lie mereko har employee ke lie ek new object
//            // banana pad raha tha
//            while (rs.next()) {
//                Employee emp = new Employee();
//                emp.setEmpNo(rs.getInt("emp_no"));
//                emp.setFirstName(rs.getString("first_name"));
//                emp.setLastName(rs.getString("last_name"));
//                emp.setGender(rs.getString("gender"));
//                emp.setHireDate(rs.getDate("hire_date"));
//                employees.add(emp);
//            }
//
//            for (Employee emp : employees) {
//                System.out.println(emp.getEmpNo() + " " + emp.getFirstName());
//            }
//
//            System.out.println(employees.size());
//
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                String json = mapper.writerWithDefaultPrettyPrinter()
//                        .writeValueAsString(employees);
//                System.out.println(json);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//
//            System.out.println(con);
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//        }
//    }
//}