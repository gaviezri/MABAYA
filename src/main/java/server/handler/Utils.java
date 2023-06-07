package server.handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String,Object> createMapFromBody(InputStream inputStream){
        Map<String,Object> result = null;
        try (BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
            String stringStream = convertStreamToString(reader);
            Gson gson = new Gson();
            result = gson.fromJson(stringStream, HashMap.class);
            if(result.containsKey("SerialNumber")){
//              convert serial number to int over gson default to double
                result.put("SerialNumber", ((Double)result.get("SerialNumber")).intValue());
            }
            if (result.containsKey("Products")){
                List products = (List) result.get("Products");
                List new_products;
                new_products = (List) products.stream().map(x -> ((Double)x).intValue()).collect(Collectors.toList());
                result.put("Products", new_products);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String convertStreamToString(BufferedReader reader) throws IOException {
        String line;
        StringBuilder body = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
}
