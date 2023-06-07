package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.DBManager;

import java.util.Map;

public class EntitiesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws java.io.IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            default:
                System.out.println("Invalid request method");
                exchange.sendResponseHeaders(405, -1);
                break;
        }
    }

    private void handleGet(HttpExchange exchange) throws java.io.IOException {
        System.out.println(exchange.getRequestURI().getQuery());
        String response = DBManager.retrieveAllCategories();
        exchange.sendResponseHeaders(200, response.length());
        java.io.OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePost(HttpExchange exchange) throws java.io.IOException {
        Map<String,Object> bodyPairs = Utils.createMapFromBody(exchange.getRequestBody());
        if (bodyPairs == null) {
            System.out.println("Invalid request body");
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        String response = "";
        if(DBManager.insertNewProduct(bodyPairs)){
            System.out.println("Product inserted");
            response = "Product inserted\n + " + bodyPairs.toString();
            exchange.sendResponseHeaders(200, response.length());
        } else {
            response = "Product not inserted\n + " + bodyPairs.toString() + "\n. maybe casing error?";
            exchange.sendResponseHeaders(400, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
        java.io.OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
