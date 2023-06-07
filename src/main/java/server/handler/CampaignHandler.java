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
            System.out.println("Invalid request method");
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        try {
            String JSONResult = DBManager.insertNewCampaign(exchange.getRequestBody());
            exchange.sendResponseHeaders(200, JSONResult.length());
            exchange.getResponseBody().write(JSONResult.getBytes());

        } catch (IllegalArgumentException e) {
            String response = "Invalid request body - most likely illegal product ID in campaign.";
            exchange.sendResponseHeaders(405, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
    }
}
