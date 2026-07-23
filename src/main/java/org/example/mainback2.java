//package org.example;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Scanner;
//import com.google.gson.Gson;
//
//public class Main {
//
//    public static String getEmployeesAsJson() {
//        return "Coming Soon";
//
//    }
//
//    public static void main(String[] args) throws Exception {
//
//
//        System.out.println("Enter Your JSON");
//        Scanner sc  = new Scanner(System.in);
//        String json = sc.nextLine();
//        System.out.println("mera json"+json);
//        // ab main mereko ye krna hai ki ye jo mai string mai lera hu input usko
//        //mujhae scanner mai lena hai
//        // lekin mai scanner se ek ek krke ni dalunga , ok json toh as string le hee skte hai
//        // ab figure out krna hai ke fir linked list ka kya ?
//
//        Gson gson = new Gson();
//        LinkedHashMap<?, ?> map = gson.fromJson(json, LinkedHashMap.class);
//        System.out.println("hashmap json" + map);
//
//        // hoo gaya , ab last dikket error json ki vajah se aaya usme kyuki mera scanner nextline bs ek hee line
//        // le raha tha toh json puri read hee ni ho pari thi
//        // filhal humne ek hee line ka json de dia inko bina line chode likh di json mtlb toh ho gaya
//
//
//
//
//        // sample JSON data — API call mein body ke roop mein jaega
/// /        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
//
////        values.put("emp_no", 500027);
////        values.put("birth_date", "2004-01-01");
////        values.put("first_name", "Devansh");
////        values.put("last_name", "Pundir");
////        values.put("gender", "M");
////        values.put("hire_date","2026-07-20");
//
//        // QueryBuilder se query banao (testing ke lie) table number aur hashmap de dia
//        String query = QueryBuilder.buildInsertQuery("employees", (LinkedHashMap<String, Object>) map);
//        System.out.println("Query: " + query);
//
//        // DBHandler se execute karo aur JSON response lo
//
//
//        String response = DBHandler.insertEmployee(query);
//
//        System.out.println("Response: " + response);
//
//
//        //update
//        System.out.println("Enter Your JSON    where and wherekivalue like first_name ko dev kr do ");
//        Scanner sc2 = new Scanner(System.in);
//        String json2 = sc2.nextLine();
//
//        Gson gson2 = new Gson();
//        LinkedHashMap<?, ?> map2 = gson2.fromJson(json2, LinkedHashMap.class);
//        System.out.println("hashmap json" + map2);
//
//        System.out.println("Enter wherevalue or employee id");
//        Scanner sc3 = new Scanner(System.in);
//        int id = sc3.nextInt();
//
//
//
////        LinkedHashMap<String, Object> updateValues = new LinkedHashMap<>();
////        updateValues.put("first_name", "Dev");
//
//        String updateQuery = QueryBuilder.buildUpdateQuery("employees", map2, "emp_no", id);
//        System.out.println("Query: " + updateQuery);
//
//        String updateResponse = DBHandler.updateEmployee(updateQuery);
//        System.out.println("Response: " + updateResponse);
//
//
////delete
//        System.out.println("Enter Employee ID:");
//        Scanner sc4 = new Scanner(System.in);
//        int whereValue= sc4.nextInt();
//
//        String deleteQuery = QueryBuilder.buildDeleteQuery("employees", "emp_no", whereValue);
//        System.out.println("Query: " + deleteQuery);
//
//        String deleteResponse = DBHandler.deleteEmployee(deleteQuery);
//        System.out.println("Response: " + deleteResponse);
//
//    }
//}