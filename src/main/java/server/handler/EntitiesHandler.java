package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.DBManager;

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
        System.out.println(exchange.getRequestURI().getQuery());
        String response = DBManager.retrieveAllCategories();
        exchange.sendResponseHeaders(200, response.length());
        java.io.OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
