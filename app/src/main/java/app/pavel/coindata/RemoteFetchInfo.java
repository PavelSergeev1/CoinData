package app.pavel.coindata;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetchInfo {

    private static String OPEN_COIN_API;

    static void setString(String CoinId) {
        OPEN_COIN_API = "https://api.coingecko.com/api/v3/coins/" + CoinId;
    }

    public static JSONObject getJSONinfo(String CoinId) {
        try {
            setString(CoinId);

            URL url = new URL(String.format(OPEN_COIN_API));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(8192);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
