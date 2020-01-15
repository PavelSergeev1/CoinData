package app.pavel.coindata;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class RemoteFetchGraph {

    private static String OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

    static JSONArray getJSON(String CoinSymbol, String period) {
        try {

            OPEN_PRICE_API += CoinSymbol;
            if (CoinSymbol.equals("XRP"))
                OPEN_PRICE_API += "USDT?period=";
            else OPEN_PRICE_API += "USD?period=";
                OPEN_PRICE_API += period;

            URL url = new URL(OPEN_PRICE_API);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1096);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            Scanner scan = new Scanner(url.openStream());
            StringBuilder str = new StringBuilder();
            while (scan.hasNext())
                str.append(scan.nextLine());
            scan.close();

            return new JSONArray(str.toString());
        } catch (Exception e) {
            return null;
        }
    }
}