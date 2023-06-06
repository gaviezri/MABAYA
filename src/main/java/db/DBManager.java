package db;


import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class DBManager {
    //Singleton class
    private static  final  String DB_URL = "jdbc:mysql://localhost:3306/mabaya";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "gal5467612";
    private static DBManager instance = new DBManager();
    private DBManager() {
    }
    public static DBManager getInstance() {
        return instance;
    }

    public static String retrieveAllCategories() {
        JSONArray jsonArray = new JSONArray();
        try {
            // create a connection to the database
            ResultSet resultSet = queryDataBase("SELECT * FROM category");

            // create a JSON object to hold the data
            while (resultSet.next()) {
                JSONObject currentCategory = new JSONObject();
                currentCategory.put("category", resultSet.getString("Name"));
                jsonArray.put(currentCategory);
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database" + e);
        }
        return jsonArray.toString();
    }
    public static String retrieveAllProducts() {
        JSONArray jsonArray = new JSONArray();
        try {
            // create a connection to the database
            ResultSet resultSet = queryDataBase("SELECT * FROM product");

            // create a JSON object to hold the data
            while (resultSet.next()) {
                JSONObject currentProduct = new JSONObject();
                currentProduct.put("serialNumber", resultSet.getString("SerialNumber"));
                currentProduct.put("title", resultSet.getString("Title"));
                currentProduct.put("category", resultSet.getString("Category"));
                currentProduct.put("price", resultSet.getString("Price"));
                jsonArray.put(currentProduct);
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database" + e);
        }
        return jsonArray.toString();
    }
    private static ResultSet queryDataBase(String query) throws SQLException {
        Connection connection =  DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        // create a statement object to send to the database
        Statement statement = connection.createStatement();
        // create the query to send to the database
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }
}
