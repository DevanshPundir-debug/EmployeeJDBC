package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.ai.AIController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

//import static org.example.DBHandler.password;
//import static org.example.DBHandler.username;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Executors;

public class HttpServerMain {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ek hi instance, taki andar ka HttpClient har request par dobara na bane
    private static final AIController aiController = new AIController();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        System.out.println("Server Started on http://localhost:8080");

        // yahan se token milega, ye endpoint khula hai warna token milega hi nahi
        server.createContext("/login", (HttpExchange exchange) -> {

            try {

                if (!exchange.getRequestMethod().equals("POST")) {

                    sendResponse(exchange, 405, "{\"error\":\"Use POST for /login\"}");
                    return;
                }

                // body se {"username":"...","password":"..."} padho
                String body = new String(exchange.getRequestBody().readAllBytes());

                Map<String, String> credentials = objectMapper.readValue(body, Map.class);

                String username = credentials.get("username");
                String password = credentials.get("password");

                Authentication auth = new Authentication();

                // galat credentials par token banana hi nahi hai
                if (!auth.authenticate(username, password)) {

                    sendResponse(exchange, 401, "{\"error\":\"Invalid username or password\"}");
                    return;
                }

                // har login par nayi key pair, uski public key token ke andar jayegi
                KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();

                String publicKeyBase64 = Base64.getEncoder()
                        .encodeToString(keyPair.getPublic().getEncoded());

                String token = TokenManager.createToken(username, password, publicKeyBase64);

                sendResponse(exchange, 200, "{\"token\":\"" + token + "\"}");

            } catch (Exception e) {

                e.printStackTrace();

                sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        });

        server.createContext("/employees", (HttpExchange exchange) -> {

            String response = "";
//
//            Authentication auth = new Authentication();
//
//            if(!auth.authenticate(username,password)){
//
//                System.out.println("401 Unauthorized");
//                return;
//
//

            // purana Basic auth, ab Bearer token use kar rahe hain
//            Authentication auth = new Authentication();
//
//            String authHeader = exchange.getRequestHeaders().getFirst("Authorization"); //Auth header
//
//            if (authHeader == null || !authHeader.startsWith("Basic ")) { //check if the auth header exists
//                                                                          // or start with basic in inverted coma
//
//                System.out.println("401 Unauthorized");
//                return;
//            }
//
//            String encoded = authHeader.substring(6); // Removes the "Basic  prefix. like Basic abcdfls
//
//            String decoded = new String(Base64.getDecoder().decode(encoded)); //Decodes the Base64 string. usko pass m badalna
//
//            String[] credentials = decoded.split(":", 2);// Splits the decoded string into two parts:
//                                                                    // username password
//                                                                    // The 2 ensures that only the first colon is used as the separator, so passwords containing : still work
//            String username = credentials[0];             //john:abc123  becomes credentials[0] = "john";
//            String password = credentials[1];              //credentials[1] = "abc123";
//
//            if (!auth.authenticate(username, password)) {
//
//                System.out.println("401 Unauthorized");
//                return;
//            }

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization"); //Auth header

            // header hona chahiye aur "Bearer " se shuru hona chahiye
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                System.out.println("401 Unauthorized");

                sendResponse(exchange, 401,
                        "{\"error\":\"Missing or invalid Authorization header. Use: Bearer <token>\"}");
                return;
            }

            // "Bearer " prefix hatao jo bacha vohi token hai
            String token = authHeader.substring(7).trim();

            // token decode karke andar ke username/password check karo
            if (!TokenManager.isValidToken(token)) {

                System.out.println("401 Unauthorized");

                sendResponse(exchange, 401, "{\"error\":\"Invalid token\"}");
                return;
            }

            else {
                try {

                    String method = exchange.getRequestMethod();

                    switch (method) {

                        case "GET":

                            String getQuery = exchange.getRequestURI().getQuery();

                            Integer limit = null;

                            // /employees?limit=5 — limit na ho toh saara data
                            if (getQuery != null && getQuery.startsWith("limit=")) {

                                limit = Integer.parseInt(getQuery.substring(6));
                            }

                            response = Main.getEmployeesAsJson(limit);
                            break;

                        case "POST":

                            String insertJson = new String(exchange.getRequestBody().readAllBytes());

                            response = Main.insertEmployee(insertJson);
                            break;

                        case "PUT":

                            // ab id URL mein nahi, body ke "where" mein aati hai
                            String updateJson = new String(exchange.getRequestBody().readAllBytes());

                            response = Main.updateEmployee(updateJson);
                            break;

                        case "DELETE":

                            String deleteQuery = exchange.getRequestURI().getQuery();

                            if (deleteQuery == null || !deleteQuery.startsWith("id=")) {

                                response = "{\"error\":\"Please provide employee id in URL. Example: /employees?id=10001\"}";

                                exchange.sendResponseHeaders(400, response.getBytes().length);

                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                                return;
                            }

//                            int deleteId = Integer.parseInt(deleteQuery.substring(3));
//
//                            response = Main.deleteEmployee(deleteId);
//                            break;

                            String deleteJson = new String(exchange.getRequestBody().readAllBytes());

                            response = Main.deleteEmployee(deleteJson);

                        default:

                            response = "{\"error\":\"Method Not Allowed\"}";

                            exchange.sendResponseHeaders(405, response.getBytes().length);

                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                            return;
                    }

                    exchange.getResponseHeaders().add("Content-Type", "application/json");

                    exchange.sendResponseHeaders(200, response.getBytes().length);

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                } catch (Exception e) {

                    e.printStackTrace();

                    response = "{\"error\":\"" + e.getMessage() + "\"}";

                    exchange.getResponseHeaders().add("Content-Type", "application/json");

                    exchange.sendResponseHeaders(500, response.getBytes().length);

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }


        });

        // English request andar, wahi JSON bahar jo pehle khud body mein likhna padta tha
        server.createContext("/ai", (HttpExchange exchange) -> {

            try {

                if (!exchange.getRequestMethod().equals("POST")) {

                    sendResponse(exchange, 405, errorJson("Use POST for /ai"));
                    return;
                }

                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                    sendResponse(exchange, 401,
                            errorJson("Missing or invalid Authorization header. Use: Bearer <token>"));
                    return;
                }

                if (!TokenManager.isValidToken(authHeader.substring(7).trim())) {

                    sendResponse(exchange, 401, errorJson("Invalid token"));
                    return;
                }

                String body = new String(
                        exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                sendResponse(exchange, 200, aiController.handle(body));

            } catch (IllegalArgumentException e) {

                // user ki body ya query adhuri hai, uska galti batana hai
                sendResponse(exchange, 400, errorJson(e.getMessage()));

            } catch (Exception e) {

                e.printStackTrace();

                sendResponse(exchange, 500, errorJson(e.getMessage()));

            } catch (Throwable e) {

                e.printStackTrace();

                sendResponse(exchange, 500, errorJson(e.getMessage()));
            }
        });

        server.setExecutor(Executors.newFixedThreadPool(10));

        server.start();
    }

    // messages mein quotes hote hain, isliye haath se JSON banane ke bajay mapper se
    private static String errorJson(String message) {

        try {
            return objectMapper.writeValueAsString(Map.of("error", String.valueOf(message)));

        } catch (Exception e) {

            return "{\"error\":\"Something went wrong\"}";
        }
    }

    // ek hi jagah se response bhejne ke liye, warna har baar 4 line likhni padti hai
    private static void sendResponse(HttpExchange exchange, int statusCode, String response)
            throws IOException {

        byte[] bytes = response.getBytes();

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        exchange.sendResponseHeaders(statusCode, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
