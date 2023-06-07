package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CampaignServer {
    private HttpServer server;

    public CampaignServer(int port)  throws IOException{
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/campaigns/create", HandlerRegistry.CAMPAIGN_HANDLER);
        server.createContext("/ads/retrieve", HandlerRegistry.ADS_HANDLER);
        server.createContext("/entities/products", HandlerRegistry.ENTITIES_HANDLER);
        server.createContext("/entities/category", HandlerRegistry.ENTITIES_HANDLER);
//        server.createContext("/entities/campaigns",HandlerRegistry.ENTITIES_HANDLER);
        server.start();
    }
}
