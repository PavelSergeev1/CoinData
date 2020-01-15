package app.pavel.coindata;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RemoteFetchInfo {

    private static String OPEN_COIN_API;

    private static void setString(String CoinId) {
        OPEN_COIN_API = "https://api.coingecko.com/api/v3/coins/" + CoinId;
    }

    static JSONObject getJSONinfo(String CoinId) {
        try {

            if (CoinId.equals("binance-coin"))
                setString("binancecoin");
            else
                setString(CoinId);

            URL url = new URL(OPEN_COIN_API);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(8192);
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
