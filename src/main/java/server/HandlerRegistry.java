package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.handler.AdsHandler;
import server.handler.CampaignHandler;
import server.handler.EntitiesHandler;

public enum HandlerRegistry implements HttpHandler{
    ENTITIES_HANDLER(new EntitiesHandler()),
    CAMPAIGN_HANDLER( new CampaignHandler()),
    ADS_HANDLER(new AdsHandler());

    private HttpHandler handler;

    HandlerRegistry(HttpHandler handler) {
        this.handler = handler;
    }

    public void handle(HttpExchange exchange) throws java.io.IOException {
        handler.handle(exchange);
    }
}
