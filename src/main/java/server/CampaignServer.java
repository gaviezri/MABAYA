package server;

import com.sun.net.httpserver.HttpServer;
import server.handler.AdsHandler;
import server.handler.CampaignHandler;
import server.handler.EntitiesHandler;
import server.handler.ProductHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CampaignServer {
    private HttpServer server;

    public CampaignServer(int port)  throws IOException{
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/campaigns/create", new CampaignHandler());
        server.createContext("/ads/retrieve", new AdsHandler());
        server.createContext("/entities/products", new ProductHandler());
        server.createContext("/entities/campaigns", new EntitiesHandler());
        server.createContext("/entities/{entity}", new EntitiesHandler());
        server.start();
    }
}
