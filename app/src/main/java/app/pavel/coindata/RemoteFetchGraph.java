package app.pavel.coindata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class RemoteFetchGraph {

    private final static long oneHourInMillis = 3600000L;
    private final static long oneDayInMillis = 86400000L;
    private final static long threeMonthsInMillis = 7889400000L;
    private final static long oneYearInMillis = 31557600000L;
    private final static long sixYearsInMillis = 189345600000L;

    private static String OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

    static JSONArray getJSON(String coinSymbol, String period) {
        try {

            OPEN_PRICE_API += coinSymbol + "USD";

            if (period.equals("coin_item_graph")) {
                long from_time = System.currentTimeMillis() - 604800000;
                long till_time = System.currentTimeMillis();

                switch (coinSymbol) {
                    case "XRP": case "VET":
                        OPEN_PRICE_API += "T";
                        break;
                    case "TUSD" :
                        OPEN_PRICE_API += "C";
                        break;
                    case "PAX":
                        OPEN_PRICE_API = OPEN_PRICE_API.substring(0, OPEN_PRICE_API.length() - 6)
                                + "USDPAX";
                        break;
                    case "RVN": case "MIN":
                        OPEN_PRICE_API = OPEN_PRICE_API.substring(0, OPEN_PRICE_API.length() - 3) +
                                "BTC";
                        break;
                    case "NEM":
                        OPEN_PRICE_API = OPEN_PRICE_API.substring(0, OPEN_PRICE_API.length() - 6)
                                + "XEMUSD";
                        break;
                    case "USDC":
                        OPEN_PRICE_API = OPEN_PRICE_API.substring(0, OPEN_PRICE_API.length() - 4)
                                + "USDC";
                }

                OPEN_PRICE_API += "?period=H4&from=" + from_time + "&till=" + till_time;
            } else {
                if (coinSymbol.equals("XRP"))
                    OPEN_PRICE_API += "T?period=";
                else OPEN_PRICE_API += "?period=";

                long from_time;
                long till_time = System.currentTimeMillis();

                switch (period) {
                    case "H1" :
                        from_time = till_time - oneHourInMillis;
                        OPEN_PRICE_API += "M1" + "&from=" + from_time + "&till=" + till_time;
                        break;
                    case "D1" :
                        from_time = till_time - oneDayInMillis;
                        OPEN_PRICE_API += "M15" + "&from=" +  from_time + "&till=" + till_time;
                        break;
                    case "M3" :
                        from_time = till_time - threeMonthsInMillis;
                        OPEN_PRICE_API += "D1" + "&from=" +  from_time + "&till=" + till_time;
                        break;
                    case "Y1" :
                        from_time = till_time - oneYearInMillis;
                        OPEN_PRICE_API += "D7" + "&from=" +  from_time + "&till=" + till_time;
                        break;
                    case "Y6" :
                        from_time = till_time - sixYearsInMillis;
                        OPEN_PRICE_API += "1M" + "&from=" +  from_time + "&till=" + till_time;
                        break;
                }
            }

            URL url = new URL(OPEN_PRICE_API);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            OPEN_PRICE_API = "https://api.hitbtc.com/api/2/public/candles/";

            BufferedReader reader;

            try {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }

            StringBuilder json = new StringBuilder(1096);
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
            e.printStackTrace();

            return null;
        }
    }
}

// https://api.hitbtc.com/api/2/public/candles/BTCUSD?period=1M&from=1302813555839&till=1586983155839

// 221356800000 - 7 years in millis
//  31622400000 - 1 year in millis
//   7905600000 - 3 month in millis
//   2635200000 - 1 month (30,5 days) in millis
//    604800000 - 7 days in milliseconds
//     86400000 - 1 day in milliseconds
//      3600000 - 1 hour in milliseconds

// for 7 days, now in millis
// https://api.hitbtc.com/api/2/public/candles/BTCUSD?period=H1&from= (now - 604800000) &till= (now)