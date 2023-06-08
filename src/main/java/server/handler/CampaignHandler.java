package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.DBManager;

import java.io.IOException;

public class CampaignHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!method.equals("POST")){
            handleInvalidMethod(exchange);
            return;
        }
        try {
            generateResponse(DBManager.insertNewCampaign(exchange.getRequestBody()), exchange, 200);

        } catch (IllegalArgumentException e) {
            generateResponse("Invalid request body - most likely illegal product ID in campaign.", exchange, 405);
        }
    }

    private static void generateResponse(String response, HttpExchange exchange, int rCode) throws IOException {
        exchange.sendResponseHeaders(rCode, response.length());
        exchange.getResponseBody().write(response.getBytes());
    }

    private static void handleInvalidMethod(HttpExchange exchange) throws IOException {
        System.out.println("Invalid request method");
        exchange.sendResponseHeaders(405, -1);
        return;
    }
}
