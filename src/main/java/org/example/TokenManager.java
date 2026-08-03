package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String createToken(String username,
                                     String password,
                                     String publicKey) throws JsonProcessingException {

        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("username", username);
        tokenData.put("password", password);
        tokenData.put("publicKey", publicKey);

        // Map se JSON String
        String json = objectMapper.writeValueAsString(tokenData);

        // JSON String se Base64 String, taaki decodeToken wapas padh sake
        return Base64.getEncoder().encodeToString(json.getBytes());

    }
    public static Map<String, String> decodeToken(String token) {

        Map<String, String> tokenData = new HashMap<>();

        try {

            // Base64 String se JSON String
            String json = new String(Base64.getDecoder().decode(token));

            // JSON String se Map
            tokenData = objectMapper.readValue(json, Map.class);

        } catch (Exception e) {

            // galat / toota hua token aaya toh khaali map hi wapas jayega
            e.printStackTrace();

        }

        return tokenData;
    }

    // token ke andar ke username/password sahi hain ya nahi
    public static boolean isValidToken(String token) {

        Map<String, String> tokenData = decodeToken(token);

        String username = tokenData.get("username");
        String password = tokenData.get("password");

        if (username == null || password == null) {
            return false;
        }

        return new Authentication().authenticate(username, password);
    }
}
