package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;

//import static org.example.DBHandler.password;
//import static org.example.DBHandler.username;
import java.util.Base64;

public class HttpServerMain {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        System.out.println("Server Started on http://localhost:8080");

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
//            }

            Authentication auth = new Authentication();

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization"); //Auth header

            if (authHeader == null || !authHeader.startsWith("Basic ")) { //check if the auth header exists
                                                                          // or start with basic in inverted coma

                System.out.println("401 Unauthorized");
                return;
            }

            String encoded = authHeader.substring(6); // Removes the "Basic  prefix. like Basic dXNlcjpwYXNzd29yZA==
                                                                // dXNlcjpwYXNzd29yZA==

            String decoded = new String(Base64.getDecoder().decode(encoded)); //Decodes the Base64 string. usko pass m badalna

            String[] credentials = decoded.split(":", 2);// Splits the decoded string into two parts:
                                                                    // username password
                                                                    // The 2 ensures that only the first colon is used as the separator, so passwords containing : still work
            String username = credentials[0];             //john:abc123  becomes credentials[0] = "john";
            String password = credentials[1];              //credentials[1] = "abc123";

            if (!auth.authenticate(username, password)) {

                System.out.println("401 Unauthorized");
                return;
            }

            else {
                try {

                    String method = exchange.getRequestMethod();

                    switch (method) {

                        case "GET":

                            response = Main.getEmployeesAsJson();
                            break;

                        case "POST":

                            String insertJson = new String(exchange.getRequestBody().readAllBytes());

                            response = Main.insertEmployee(insertJson);
                            break;

                        case "PUT":

                            String updateJson = new String(exchange.getRequestBody().readAllBytes());

                            String updateQuery = exchange.getRequestURI().getQuery();

                            if (updateQuery == null || !updateQuery.startsWith("id=")) {

                                response = "{\"error\":\"Please provide employee id in URL. Example: /employees?id=10001\"}";

                                exchange.sendResponseHeaders(400, response.getBytes().length);

                                OutputStream os = exchange.getResponseBody();
                                os.write(response.getBytes());
                                os.close();
                                return;
                            }

                            int updateId = Integer.parseInt(updateQuery.substring(3));

                            response = Main.updateEmployee(updateJson, updateId);
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

                            int deleteId = Integer.parseInt(deleteQuery.substring(3));

                            response = Main.deleteEmployee(deleteId);
                            break;

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

        server.setExecutor(null);

        server.start();
    }
}