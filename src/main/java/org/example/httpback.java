//package org.example;
//
//import com.sun.net.httpserver.HttpServer;
//
//import java.io.OutputStreamWriter;
//import java.net.InetSocketAddress;
//
//import com.sun.net.httpserver.HttpExchange;
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class HttpServerMain {
//
//    public static void main(String[] args) throws Exception {
//
//        HttpServer server = HttpServer.create(
//                new InetSocketAddress(8080), // address banao using port 8080
//                0                                // how many connection(incomming) can wait in a queue
//        );                                      // nothing is listening yet only server's address created
//
//        System.out.println("Server Started...");
//
//
//        //HttpServer Object    ->    Listening Address     ->    localhost:8080  ->    Not Started
//
//
//        server.createContext("/employees", (HttpExchange exchange) -> {   // route banata hai (regiser krta)
//                                                                             //Browser says localhost:8080/employees
//                                                                            //Server checks , Do I know /employees?
//
//            String response = null;
//            try {
//                response = Main.getEmployeesAsJson();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
/// /            String response = Main.main();
//
//            exchange.sendResponseHeaders(200, response.getBytes().length);
//
//            OutputStream os = exchange.getResponseBody();
//
////            OutputStreamWriter os = exchange.get
//
//            os.write(response.getBytes());
//
//            os.close();
//
//        });
//
//        server.start();
//    }
//}
