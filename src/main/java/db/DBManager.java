package db;

import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static server.handler.Utils.createMapFromBody;

public class DBManager {
    Connection connection;




    private static class QueryBuilder {
        private StringBuilder Query;

        public QueryBuilder() {
            Query = new StringBuilder();
        }

        public QueryBuilder insertInto(String table){
            Query.append("INSERT INTO ");
            Query.append(table);
            Query.append(" ");
            switch(table){
                case "product":
                    Query.append("(Title, Price, CategoryId, SerialNumber) ");
                    break;
                case "campaign":
                    Query.append("(Name, StartDate, Bid) ");
                    break;
                case "category":
                    Query.append("(Name) ");
                    break;
                case "campaign_product":
                    Query.append("(ProductSerialNumber, CampaignId) ");
                    break;
            }
            return this;
        }
        public QueryBuilder values(Object... values){
            List valuesList = new LinkedList();
            for(Object value : values){
                if(value instanceof String || value instanceof java.sql.Date){
                    valuesList.add("'" + value + "'");
                } else {
                    valuesList.add(value);
                }
            }
            int size = valuesList.size();
            Query.append("VALUES (");
            for (int i = 0; i < size; i++) {
               Query.append(valuesList.get(i))
                       .append((i!=size-1)?", ":"");
            }
            Query.append(") ");
            return this;
        }

        public QueryBuilder select(String... columns){
            Query.append("SELECT ");
            int size = columns.length;
            for (int i = 0; i < size; i++) {
                Query.append(columns[i])
                        .append((i!=size-1)?", ":" ");
            }
            return this;
        }
        public QueryBuilder from(String table){
            Query.append("FROM ");
            Query.append(table);
            Query.append(" ");
            return this;
        }

        public QueryBuilder join(String table){
            Query.append("JOIN ");
            Query.append(table);
            Query.append(" ");
            return this;
        }

        public QueryBuilder on(String colA, String colB){
            Query.append("ON ");
            Query.append(colA);
            Query.append(" = ");
            Query.append(colB);
            Query.append(" ");
            return this;
        }

        public QueryBuilder where(String condition){
            Query.append("WHERE ");
            Query.append(condition);
            Query.append(" ");
            return this;
        }

        public QueryBuilder and(String condition){
            Query.append("AND ");
            Query.append(condition);
            Query.append(" ");
            return this;
        }

        public QueryBuilder orderBy(String columns, boolean ascending){
            Query.append("ORDER BY ");
            Query.append(columns);
            Query.append(" ");
            Query.append((ascending)?"ASC ":"DESC ");
            return this;
        }
        public QueryBuilder groupBy(String... columns){
            Query.append("GROUP BY ");
            int size = columns.length;
            for (int i = 0; i < size; i++) {
                Query.append(columns[i])
                        .append((i!=size-1)?", ":" ");
            }
            return this;
        }

        public QueryBuilder limit(Integer limit){
            Query.append("limit " +limit.toString() );
            return this;
        }
        @Override
        public String toString(){
            String finalizedQuery = Query.toString();
            Query = new StringBuilder();
            return finalizedQuery;
        }
    }
    //Singleton class
    private static List<String> SQL_KEY_WORDS = new ArrayList<>();
    static {
        SQL_KEY_WORDS.add("SELECT");
        SQL_KEY_WORDS.add("FROM");
        SQL_KEY_WORDS.add("WHERE");
        SQL_KEY_WORDS.add("AND");
        SQL_KEY_WORDS.add("OR");
        SQL_KEY_WORDS.add("INSERT INTO");
        SQL_KEY_WORDS.add("VALUES");
    }
    public static boolean isSafeParam(String param){
        // return true if the param is not a SQL key word
        // a cheap way to prevent SQL injection
        return !SQL_KEY_WORDS.contains(param);
    }
    private static String DB_URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static DBManager instance = new DBManager();
    private DBManager() {
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("config.prop")) {
            java.util.Properties prop = new java.util.Properties();
            prop.load(input);
            DB_URL = prop.getProperty("DB_URL");
            USERNAME = prop.getProperty("USERNAME");
            PASSWORD = prop.getProperty("PASSWORD");
        } catch (Exception e) {
            System.out.println("Error reading config file");
        }
    }


    public static String retrieveAds(Map<String, String> params) {
        // given a category name, from the campaigns with the highest bid and which StartDate is less than 10 days from now
        // select one of the products with the highest price
        // return the product title and price
        // if no such campaign exists, return an empty list
        String category = params.get("cat");
        if (!isSafeParam(category)) {
            return "No category was given";
        }
        String FinalizedQuery = new QueryBuilder()
                .select("p.*")
                .from("product p")
                .join("campaign_product cp").on("p.SerialNumber", "cp.ProductSerialNumber")
                .join("campaign c").on("cp.campaignId", "c.id")
                .join("category cat").on("p.categoryId", "cat.id")
                .where("cat.name = '" + category + "'")
                .and("c.startdate >= CURDATE() - INTERVAL 10 DAY")
                .orderBy("c.bid , p.Price", false) // Sort by bid in descending order
                .limit(1)
                .toString();



        System.out.println(FinalizedQuery);
        try {
            ResultSet resultSet = queryDataBase(FinalizedQuery);
            if(resultSet.next()){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", resultSet.getString("Title"));
                jsonObject.addProperty("price", resultSet.getString("Price"));
                return jsonObject.toString();
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database" + e);
        }
        return "";
    }

    public static String retrieveAllCategories() {
        JsonArray jsonArray = new JsonArray();
        try {
            // create a connection to the database
            ResultSet resultSet = queryDataBase(new QueryBuilder().select("*").from("category").toString());

            // create a JSON object to hold the data
            while (resultSet.next()) {
                JsonObject currentCategory = new JsonObject();
                currentCategory.addProperty("category", resultSet.getString("Name"));
                jsonArray.add(currentCategory);
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database" + e);
        }
        return jsonArray.toString();
    }
    public static String retrieveAllProducts() {
        JsonArray jsonArray = new JsonArray();
        try {
            // create a connection to the database
            ResultSet resultSet = queryDataBase(new QueryBuilder().select("*").from("product").toString());

            // create a JSON object to hold the data
            while (resultSet.next()) {
                JsonObject currentProduct = new JsonObject();
                currentProduct.addProperty("serialNumber", resultSet.getString("SerialNumber"));
                currentProduct.addProperty("title", resultSet.getString("Title"));
                currentProduct.addProperty("category", resultSet.getString("Category"));
                currentProduct.addProperty("price", resultSet.getString("Price"));
                jsonArray.add(currentProduct);
            }
        } catch (Exception e) {
            System.out.println("Error connecting to the database" + e);
        }
        return jsonArray.toString();
    }
    private static ResultSet queryDataBase(String query) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Statement statement = getStatementForRead();
        // create the query to send to the database
        return statement.executeQuery(query);
    }
    private static Statement getStatementForRead() throws SQLException {
        instance.connection =  DriverManager.getConnection(DB_URL, USERNAME ,PASSWORD);
        // create a statement object to send to the database
        Statement statement =  instance.connection.createStatement();
        return statement;
    }
    private synchronized static Statement getStatementForWrite() throws SQLException {
        instance.connection =  DriverManager.getConnection(DB_URL, USERNAME ,PASSWORD);
        instance.connection.setAutoCommit(false);
        return instance.connection.createStatement();
    }

    /**
     * Inserts a new campaign into the database based on the provided request body.
     * The campaign data includes the name, start date, bid, and associated products.
     *
     * @param requestBody the input stream containing the request body
     * @return a JSON string representation of the inserted campaign data
     * @throws Exception if an error occurs during the database operation
     */
    public synchronized static String insertNewCampaign(InputStream requestBody) throws IllegalArgumentException {
        Map<String, Object> campaignValues = createMapFromBody(requestBody);
        Boolean success = false;

        try {
            java.util.Date startDate = new java.util.Date();
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());

            Statement statementForWrite = getStatementForWrite();

            // Insert new campaign into the campaign table
            String campaignInsertQuery = new QueryBuilder()
                    .insertInto("campaign")
                    .values(campaignValues.get("Name"), sqlStartDate, campaignValues.get("Bid"))
                    .toString();
            statementForWrite.executeUpdate(campaignInsertQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statementForWrite.getGeneratedKeys();

            int campaignId;
            if (generatedKeys.next()) {
                campaignId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve campaign ID.");
            }

            instance.connection.commit(); // Commit changes for the campaign table

            Collection products = (Collection) campaignValues.get("Products");

            for (Object productSN : products) {
                String campaignProductQuery = new QueryBuilder()
                        .insertInto("campaign_product")
                        .values(productSN, campaignId)
                        .toString();

                success = statementForWrite.executeUpdate(campaignProductQuery) > 0;
            }

            instance.connection.commit(); // Commit changes for the campaign_product table

            // Commit the transaction

        } catch (SQLException e) {
            System.out.println(e);
            // Rollback the transaction
            throw new IllegalArgumentException("Invalid campaign data.");
        }

        Gson gson = new Gson();
        return gson.toJson(campaignValues);
    }

    /**
     * Inserts a new product into the database using the provided data.
     *
     * @param bodyPairs A map containing the data for the new product. The keys represent the column names in the 'product' table.
     * @return true if the product was successfully inserted, false otherwise.
     */
    public static boolean insertNewProduct(Map<String, Object> bodyPairs) {
        boolean success = false;
        try {
            // Get the category ID from the category table or insert a new category if it doesn't exist
            int categoryId = getCategoryIdFromCategory(bodyPairs);

            // Construct the SQL query for inserting a new product
            String queryString = new QueryBuilder()
                    .insertInto("product")
                    .values(
                            bodyPairs.get("Title"),
                            bodyPairs.get("Price"),
                            categoryId,
                            bodyPairs.get("SerialNumber")
                    )
                    .toString();

            // Execute the SQL query
            Statement statement = getStatementForWrite();
            success = statement.executeUpdate(queryString) > 0;

            // Commit the transaction and close the connection
            instance.connection.commit();
            instance.connection.close();
        } catch (Exception e) {
            // Rollback the transaction and handle the exception
            System.out.println("Error during transaction. Rollback initiated." + e);
        }
        return success;
    }
    /**
     * Retrieves the category ID from the database based on the category name. If the category does not exist in the database,
     * it inserts the category and returns the newly generated category ID.
     *
     * @param bodyPairs A map containing the category name.
     * @return The category ID associated with the provided category name.
     * @throws SQLException                  If an SQL exception occurs while querying or updating the database.
     * @throws ClassNotFoundException        If the database driver class is not found.
     * @throws InstantiationException        If an instance of the database driver class cannot be created.
     * @throws IllegalAccessException        If access to the database driver class is denied.
     */
    private static int getCategoryIdFromCategory(Map<String, Object> bodyPairs) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        int categoryId = getCategoryId(bodyPairs);
        if (categoryId == 0) {
            String query = new QueryBuilder()
                    .insertInto("category")
                    .values(bodyPairs.get("Category"))
                    .toString();
            Statement statement = getStatementForWrite();
            statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            instance.connection.commit();
            ResultSet resSet = statement.getGeneratedKeys();
            resSet.next();
            categoryId = resSet.getInt(1);
        }
        return categoryId;
    }


    /**
     * Retrieves the category ID from the database based on the category name.
     *
     * @param bodyPairs A map containing the category name.
     * @return The category ID associated with the provided category name.
     * @throws SQLException                  If an SQL exception occurs while querying the database.
     * @throws ClassNotFoundException        If the database driver class is not found.
     * @throws InstantiationException        If an instance of the database driver class cannot be created.
     * @throws IllegalAccessException        If access to the database driver class is denied.
     */
    private static int getCategoryId(Map<String, Object> bodyPairs) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ResultSet resultSet;
        int result = 0;
        resultSet = queryDataBase("SELECT Id FROM category WHERE Name = '" + bodyPairs.get("Category") + "'");
        try {
            resultSet.next();
            result = resultSet.getInt("Id");
        } catch (Exception ignored) {}
        instance.connection.close();
        return result;
    }
}
