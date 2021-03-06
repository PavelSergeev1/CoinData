package app.pavel.coindata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RemoteFetch {

    private static String OPEN_PRICE_API = "https://api.coinlore.com/api/tickers/";

    static JSONObject getJSON(int startpoint) {
        try {
            OPEN_PRICE_API += "?start=" + (startpoint - 50) + "&limit=50";

            URL url = new URL(OPEN_PRICE_API);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(4096);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            OPEN_PRICE_API = "https://api.coinlore.com/api/tickers/";

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public void setCoinItem(JSONObject data, int i) throws JSONException {
        JSONArray DataArray = data.getJSONArray("data");
        JSONObject obj = DataArray.getJSONObject(i);
    }
}
