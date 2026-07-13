package org.example;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServerMain {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        System.out.println("Server Started...");

        server.createContext("/employees", (HttpExchange exchange) -> {

            String response = Main.getEmployeesAsJson();

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();

            os.write(response.getBytes());

            os.close();

        });

        server.start();
    }
}