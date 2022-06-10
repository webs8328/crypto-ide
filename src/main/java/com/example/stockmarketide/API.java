package com.example.stockmarketide;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class API {

    //Returns JSONObject of all requested coins and their respective variables
        //  coins = [bitcoin, ethereum, etc.]
        //  variables = [price, supply, etc.]

    public static JSONObject fetch(String[] coins, String[] variables) {
        JSONObject results = new JSONObject();

        try {
            String coinString = String.join(",", coins);
            URL url = new URL("https://api.coincap.io/v2/assets?ids=" + coinString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parse = new JSONParser();
                JSONObject res = (JSONObject) parse.parse(String.valueOf(informationString));
                JSONArray resData = (JSONArray) res.get("data");

                for (int i = 0; i < coins.length; i++) {
                    JSONObject coin = new JSONObject();
                    JSONObject coinResData = (JSONObject) resData.get(i);

                    for (String variable : variables) {
                        String varData = null;
                        if (coinResData.containsKey(variable) && coinResData.get(variable) != null) {
                            varData = (String) coinResData.get(variable);
                        }
                        coin.put(variable, varData);
                    }
                    results.put(coins[i], coin);
                }
        //also handle this later
            } else {
                throw new RuntimeException("Response Code: " + responseCode);
            }
        //handle this catch later
        } catch(Exception e) {
            e.printStackTrace();
        }
        return results;
    }

}
