package org.example;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

public class Main {


    // GET ALL EMPLOYEES
    public static String getEmployeesAsJson() throws Exception {

        String query = QueryBuilder.buildSelectAllQuery("employees");
        System.out.println("Select Query: " + query);
        return DBHandler.getEmployees(query);
    }

    // INSERT
    public static String insertEmployee(String json) throws Exception {

        Gson gson = new Gson();

        LinkedHashMap<String, Object> map =
                gson.fromJson(json, LinkedHashMap.class);

        System.out.println("Received JSON:");
        System.out.println(map);

        String query =
                QueryBuilder.buildInsertQuery("employees", map);

        System.out.println("Insert Query: " + query);

        return DBHandler.insertEmployee(query);
    }

    // UPDATE
    public static String updateEmployee(String json, int empNo) throws Exception {

        Gson gson = new Gson();

        LinkedHashMap<String, Object> map =
                gson.fromJson(json, LinkedHashMap.class);

        System.out.println("Received JSON:");
        System.out.println(map);

        String query =
                QueryBuilder.buildUpdateQuery(
                        "employees",
                        map,
                        "emp_no",
                        empNo
                );

        System.out.println("Update Query: " + query);

        return DBHandler.updateEmployee(query);
    }

    // DELETE
    public static String deleteEmployee(int empNo) throws Exception {

        String query =
                QueryBuilder.buildDeleteQuery(
                        "employees",
                        "emp_no",
                        empNo
                );

        System.out.println("Delete Query: " + query);

        return DBHandler.deleteEmployee(query);
    }
}