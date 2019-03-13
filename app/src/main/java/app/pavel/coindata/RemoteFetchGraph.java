package app.pavel.coindata;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RemoteFetchGraph {

    static String OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

    public static JSONArray getJSON(String CoinSymbol, String period) {
        try {

            OPEN_PRICE_API += CoinSymbol;
            if (CoinSymbol.equals("XRP")) OPEN_PRICE_API += "USDT?period=";
            else OPEN_PRICE_API += "USD?period=";
            OPEN_PRICE_API += period;

            Log.i("url url url", OPEN_PRICE_API);

            URL url = new URL(String.format(OPEN_PRICE_API));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1096);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            Scanner scan = new Scanner(url.openStream());
            String str = new String();
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();

            JSONArray data = new JSONArray(str);

            return data;
        } catch (Exception e) {
            return null;
        }
    }
}