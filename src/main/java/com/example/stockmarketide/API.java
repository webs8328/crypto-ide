package com.example.stockmarketide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class API {
    //A call to fetch will return an updated HashMap of HashMap object specific to each coin in the input array
    //Use to update state of local coin data
    public static HashMap<String, HashMap<String, String>> fetch(String[] coins) {
        HashMap<String, HashMap<String, String>> results = new HashMap<String, HashMap<String, String>>();

        try {
            //prepare coin string for api url
           for (int i = 0; i < coins.length; i++) {
               coins[i] = coins[i].toLowerCase();
           }
           String coinString = String.join(",", coins);

           //pass created string into url and create http connection
           URL url = new URL("https://api.coincap.io/v2/assets?ids=" + coinString);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           conn.connect();

           //Retrieve our response code. If 200 we are connected, otherwise something went wrong so error
           int responseCode = conn.getResponseCode();
           if (responseCode == 200) {
               //Essentially copy over what the above url returned to us and save it to informationString
               StringBuilder informationString = new StringBuilder();
               Scanner scanner = new Scanner(url.openStream());
               while (scanner.hasNext()) {
                   informationString.append(scanner.nextLine());
               }
               scanner.close();

               //Parse the information string to turn it into a JSON Object
               JSONParser parse = new JSONParser();
               JSONObject res = (JSONObject) parse.parse(String.valueOf(informationString));
               //in the actual object, this "data" field is an array of JSONS for each requested coin, so we extract this
               JSONArray resData = (JSONArray) res.get("data");
               System.out.println(resData);

               //Iterate to fill in our HashMaps we want to return, with data from the resData JSON (slow but don't want to use external libs)
               for (int i = 0; i < resData.size(); i++) {
                   //internal hashmap, filled with a coins data.
                   //In the main hashmap, the key is the coin name and the value is this data hashmap.
                   JSONObject toCopy = (JSONObject) resData.get(i);
                   HashMap<String, String> coinData = new HashMap<>();

                   //fill data hashmap first
                   coinData.put("supply", (String) toCopy.get("supply"));
                   coinData.put("maxSupply", (String) toCopy.get("maxSupply"));
                   coinData.put("marketCapUsd", (String) toCopy.get("marketCapUsd"));
                   coinData.put("volumeUsd24Hr", (String) toCopy.get("volumeUsd24Hr"));
                   coinData.put("priceUsd", (String) toCopy.get("priceUsd"));
                   coinData.put("changePercent24Hr", (String) toCopy.get("changePercent24Hr"));
                   coinData.put("vwap24Hr", (String) toCopy.get("vwap24Hr"));

                   //Assign the name of the coin to its copied data in the main hashmap.
                   results.put((String) toCopy.get("id"), coinData);
               }
           } else {
               //In the future this can be a pop up or something
               throw new RuntimeException("Response Code: " + responseCode);
           }
        //handle this catch later
        } catch(Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
