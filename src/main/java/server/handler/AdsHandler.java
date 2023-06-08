package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.DBManager;

import java.io.IOException;
import java.util.Map;

public class AdsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        if (!exchange.getRequestMethod().equals("GET")) {
            handleInvalidMethod(exchange);
            return;
        }
        Map<String, String> params = Utils.createMapFromQuery(exchange.getRequestURI().getQuery());
        if (params.isEmpty()) {
            handleInvalidQParams(exchange);
        }
        String response = DBManager.retrieveAds(params);
        if (response.equals("")) {
            response = "No ads found in category";
        }
        try {
            sendResponse(exchange, 200, response.length(), response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleInvalidQParams(HttpExchange exchange) {
        System.out.println("Invalid request params");
        try {
            String response = "request params are missing!";
            sendResponse(exchange, 400, -1, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(HttpExchange exchange, int rCode, int responseLength, String response) throws IOException {
        exchange.sendResponseHeaders(rCode, responseLength);
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }

    private static void handleInvalidMethod(HttpExchange exchange) {
        System.out.println("Invalid request method");
        try {
           sendResponse(exchange, 405, -1, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
