package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

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

    public static String getEmployeesAsJson() {

        return "Coming Soon";

    }

    public static List<Employee> selectEmployees() throws SQLException {

        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM employees LIMIT 5"
        );

        List<Employee> employees = new ArrayList<>(); // bina list ke lie mereko har employee ke lie ek new object
                                                      // banana pad raha tha
        while (rs.next()) {

            Employee emp = new Employee();

            emp.setEmpNo(rs.getInt("emp_no"));
            emp.setFirstName(rs.getString("first_name"));
            emp.setLastName(rs.getString("last_name"));
            emp.setGender(rs.getString("gender"));
            emp.setHireDate(rs.getDate("hire_date"));

            employees.add(emp);
        }

        return employees;
    }

    public static void insertEmployee() throws SQLException {
        String sqlQuery =
                """
                INSERT INTO employees
                (
                    emp_no,
                    birth_date,
                    first_name,
                    last_name,
                    gender,
                    hire_date
                )
                VALUES
                (
                    500001,
                    '2004-01-01',
                    'Devansh',
                    'Pundir',
                    'M',
                    CURDATE()
                );
                """;

        int insertEmp = stmt.executeUpdate(sqlQuery);

        System.out.println(insertEmp);

    }


    public static void updateEmployee() throws SQLException {
        String sqlQuery =
                """
                UPDATE employees
                SET first_name = 'DEV'
                WHERE emp_no = 500001;
                """;

        int rows = stmt.executeUpdate(sqlQuery);

        System.out.println(rows);

    }


    public static void deleteEmployee() throws SQLException {
        String sqlQuery =
                """
                DELETE FROM employees
                WHERE emp_no = 500001;
                """;

        int rows = stmt.executeUpdate(sqlQuery);

        System.out.println(rows);
    }


    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/employees"; // database ka url represent kregi baad m
//        String username = "root";
//        String password = "newpassword123";
        try {
//            Connection con = DriverManager.getConnection(url, username, password);
//            // ye line kehri hai deivermanager ek connection build kro url ye hai url se db ka address mila
//            // user and pass ye hai , and connection build ho jae toh ek con object return kr dena
//
//            Statement stmt = con.createStatement(); //con  yaha ek statement object bana do

            insertEmployee();   // pehle naya employee (500001) insert hoga

            updateEmployee();   // fir uska first_name 'DEV' ho jaega

            deleteEmployee();   // fir vo delete ho jaega

            List<Employee> employees = selectEmployees();

            for (Employee emp : employees) {
                System.out.println(emp.getEmpNo() + " " + emp.getFirstName());
            }


//            ResultSet rs = stmt.executeQuery(
//                    "SELECT * FROM employees LIMIT 5"
//                   " SELECT first_name, \n" +
//                    "COUNT(*) \n" +
//                     " FROM employees \n"+
//                        "GROUP BY first_name \n"+
//                     "HAVING COUNT(*) > 10000;"
//                    "SELECT e.first_name \n,"+
//                    "e.last_name, \n"+
//                    "s.salary \n "+
//                    "FROM employees e \n "+
//                    "JOIN salaries s \n "+
//                    "ON e.emp_no = s.emp_no \n "+
//                    "WHERE s.salary > 10000 \n ;"
//                    "SELECT *"+
//                            "FROM salaries"+
//                    "WHERE salary > 10000;"

//                    "SELECT *\n" +
//                            "first_name,\n" +
//                            "COUNT(*)\n" +
//                            "FROM employees\n" +
//                            "GROUP BY name\n" +
//                            "HAVING COUNT(*) > 10000;"

//            ); // YAHA MERE RESULT SET MAI JO QUERY KA REPLY AAEGA SQL SE VO STORE HO JAEGA


            System.out.println(employees.size());


            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(employees);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

//ResultSet temporary hai. Jaise hi:
//con.close();
//Ya program end. //ResultSet khatam.
//Data kahan rakhenge?
//Isliye hum ResultSet se data nikal ke object m dalenge ek


//            System.out.println("Connection Created");
//            Thread.sleep(30000);
//            con.close();
//            System.out.println("Connection Closed");

        System.out.println(con);
        }

        catch(SQLException e){
            e.printStackTrace();
        }

    }
}