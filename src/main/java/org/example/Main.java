package org.example;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {


    // GET ALL EMPLOYEES
//    public static String getEmployeesAsJson() throws Exception {
//
//        String query = QueryBuilder.buildSelectAllQuery("employees");
//        System.out.println("Select Query: " + query);
//        return DBHandler.getEmployees(query);
//    }

    // GET EMPLOYEES
    public static String getEmployeesAsJson(Integer limit) throws Exception {

        String query = QueryBuilder.buildSelectQuery("employees", null, null);

        if (limit != null) {
            query = query + " LIMIT " + limit;
        }

        System.out.println("Select Query: " + query);
        return DBHandler.getEmployees(query);
    }


    public static String getEmployees(String json) throws Exception {

        Gson gson = new Gson();

        LinkedHashMap<String, Object> body =
                gson.fromJson(json, LinkedHashMap.class);

        Map<String, Object> rawWhere =
                (Map<String, Object>) body.get("where");

        LinkedHashMap<String, Object> where =
                new LinkedHashMap<>();

        if (rawWhere != null) {

            where.putAll(rawWhere);

        }

        Object rawOperator = body.get("operator");

        String operator =
                rawOperator == null ? "AND" : rawOperator.toString();

        Integer limit = null;

        Object rawLimit = body.get("limit");

        if (rawLimit instanceof Number) {

            limit = ((Number) rawLimit).intValue();

        }

        String query =
                QueryBuilder.buildSelectQuery(
                        "employees",
                        where,
                        operator,
                        limit
                );

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

    // UPDATE — body mein data (kya update karna hai), where (kis row par) aur optional operator (AND/OR)
    public static String updateEmployee(String json) throws Exception {

        Gson gson = new Gson();

        LinkedHashMap<String, Object> body =
                gson.fromJson(json, LinkedHashMap.class);

        System.out.println("Received JSON:");
        System.out.println(body);

        // gson nested object ko LinkedTreeMap banata hai, isliye Map mein le kar copy kar rahe hain
        Map<String, Object> rawData = (Map<String, Object>) body.get("data");
        Map<String, Object> rawWhere = (Map<String, Object>) body.get("where");

        if (rawData == null || rawData.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Body mein 'data' mandatory hai. Example: {\"data\":{...}, \"where\":{...}}");
        }

        LinkedHashMap<String, Object> data = new LinkedHashMap<>(rawData);

        LinkedHashMap<String, Object> where = new LinkedHashMap<>();
        if (rawWhere != null) {
            where.putAll(rawWhere);
        }

        // body mein operator ni  dera mai  toh and
        Object rawOperator = body.get("operator");
        String operator = (rawOperator == null) ? "AND" : rawOperator.toString();

        String query =
                QueryBuilder.buildUpdateQuery(
                        "employees",
                        data,
                        where,
                        operator
                );

        System.out.println("Update Query: " + query);

        return DBHandler.updateEmployee(query);
    }

    // DELETE
//    public static String deleteEmployee(int empNo) throws Exception {
//
//        String query =
//                QueryBuilder.buildDeleteQuery(
//                        "employees",
//                        "emp_no",
//                        empNo
//                );
//
//        System.out.println("Delete Query: " + query);
//
//        return DBHandler.deleteEmployee(query);
//    }

    public static String deleteEmployee(String json) throws Exception {

        Gson gson = new Gson();

        LinkedHashMap<String, Object> body =
                gson.fromJson(json, LinkedHashMap.class);

        Object empNo = body.get("emp_no");

        if (empNo == null) {
            throw new IllegalArgumentException("emp_no is required");
        }

        String query =
                QueryBuilder.buildDeleteQuery(
                        "employees",
                        "emp_no",
                        ((Number) empNo).intValue()
                );

        System.out.println("Delete Query: " + query);

        return DBHandler.deleteEmployee(query);
    }



}