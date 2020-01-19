package app.pavel.coindata;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RemoteFetchInfoTicker {
    private static String OPEN_COIN_API;

    private static void setString(String CoinId) {
        OPEN_COIN_API = "https://api.cryptonator.com/api/full/" + CoinId + "-usd";
    }

    static JSONObject getJSONinfo(String CoinId) {
        try {
            setString(CoinId);

            URL url = new URL(OPEN_COIN_API);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            return new JSONObject(json.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
