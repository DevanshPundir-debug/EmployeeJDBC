package org.example;

import java.util.LinkedHashMap;
import java.util.StringJoiner;


public class QueryBuilder {

    // INSERT — where ka koi matlab nahi hota, agar pass kiya toh ignore karke note print karega

    public static String buildInsertQuery(String tableName, LinkedHashMap<String, Object> data) {

//        if (whereColumn != null && !whereColumn.isBlank()) {
//            System.out.println("[NOTE] INSERT query mein WHERE clause ka use nahi hota. '" + whereColumn + "' ignore kar diya gaya.");
//        }

        StringJoiner columns = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        for (String key : data.keySet()) {
            columns.add(key);
            Object value = data.get(key);
            if (value == null) {
                placeholders.add("NULL");
            }
            else if (value instanceof Number) {
                placeholders.add(data.get(key).toString());
            }
            else {
                placeholders.add("'"+data.get(key).toString()+"'");
            }

        }

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
    }

    // UPDATE — ek ya multiple WHERE, conditions AND ya OR se judengi (caller batata hai kaunsa)
    // WHERE missing ho toh poori table update ho jaegi, isliye error
    public static String buildUpdateQuery(String tableName, LinkedHashMap<?, ?> data, LinkedHashMap<String, Object> whereConditions, String whereOperator) {
        if (whereConditions == null || whereConditions.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] UPDATE query mein WHERE  mandatory hai. Bina WHERE ke poori table update ho jaegi.");
        }

        // sirf AND ya OR chalega, kuch aur daala toh ni  hoga
        if (whereOperator == null || (!whereOperator.equalsIgnoreCase("AND") && !whereOperator.equalsIgnoreCase("OR"))) {
            throw new IllegalArgumentException("[ERROR] WHERE operator sirf AND ya OR ho sakta hai. Mila: " + whereOperator);
        }

        StringJoiner setPairs = new StringJoiner(", ");
        for (Object key : data.keySet()) {
            Object value = data.get(key);
            if (value == null) {
                setPairs.add(key + " = NULL");
            }
            else if (value instanceof Number) {
                setPairs.add(key + " = " + value.toString());
            }
            else {
                setPairs.add(key + " = '" + value.toString() + "'");
            }
        }

        //number hai toh bina quote, warna quote ke saath
        StringJoiner whereClause = new StringJoiner(" " + whereOperator.toUpperCase() + " ");
        for (String key : whereConditions.keySet()) {
            Object whereValue = whereConditions.get(key);
            if (whereValue instanceof Number) {
                whereClause.add(key + " = " + whereValue.toString());
            } else {
                whereClause.add(key + " = '" + whereValue.toString() + "'");
            }
        }

        return String.format("UPDATE %s SET %s WHERE %s", tableName, setPairs, whereClause);
    }

//     DELETE — WHERE missing ho toh poori table delete ho jaegi, isliye error
    public static String buildDeleteQuery(String tableName, String whereColumn, Object whereValue) {
        if (whereColumn == null || whereColumn.isBlank()) {
            throw new IllegalArgumentException("[ERROR] DELETE query mein WHERE clause mandatory hai. Bina WHERE ke poori table delete ho jaegi.");
        }

        String whereVal;
        if (whereValue instanceof Number) {
            whereVal = whereValue.toString();
        } else {
            whereVal = "'" + whereValue.toString() + "'";
        }

        return String.format("DELETE FROM %s WHERE %s = %s", tableName, whereColumn, whereVal);
    }
//
//    // SELECT — WHERE na ho toh SELECT * FROM table return karega (safe hai, sirf read hai)
    public static String buildSelectQuery(String tableName, String whereColumn, Object whereValue) {
        if (whereColumn == null || whereColumn.isBlank()) {
            System.out.println("[NOTE] WHERE clause nahi diya, SELECT * FROM " + tableName + " chalega — saari rows fetch hongi.");
            return String.format("SELECT * FROM %s", tableName);
        }

        String whereVal;
        if (whereValue instanceof Number) {
            whereVal = whereValue.toString();
        } else {
            whereVal = "'" + whereValue.toString() + "'";
        }

        return String.format("SELECT * FROM %s WHERE %s = %s", tableName, whereColumn, whereVal);
    }

    public static String buildSelectAllQuery(String tableName) {
        return String.format("SELECT * FROM %s", tableName);

    }

}