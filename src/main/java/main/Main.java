package main;

import server.CampaignServer;

public class Main {
    public static void main(String[] args) {
        try {
            new CampaignServer(8080);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}