package app.pavel.coindata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinInfoActivity extends AppCompatActivity {

    DecimalFormat df3 = new DecimalFormat("#.##");

    String categories = "categories";
    String description = "description";
    String ticker = "ticker";
    String en = "en";
    String markets = "markets";
    String market = "market";
    String price = "price";
    String volume = "volume";

    String CoinId = "";
    String CoinName = "";
    String CoinSymbol = "";
    String CoinRank = "";
    String CoinPrice = "";
    String CoinPriceBTC = "";
    String CoinP1H = "";
    String CoinP24H = "";
    String CoinP7D = "";
    String CoinMCAP = "";
    String CoinV24H = "";
    String CoinCS = "";
    String CoinMS = "";
    String CoinUsage = "";

    String RAW = "RAW";
    String USD = "USD";
    String IMAGEURL = "IMAGEURL";

    String M15 = "M15";
    String M30 = "M30";
    String H1 = "H1";
    String H4 = "H4";
    String D1 = "D1";

    String graph_title = "price $ on hitbtc.com";

    Handler handler;

    public CoinInfoActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info);

        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        Intent intent = getIntent();

        CoinId = intent.getStringExtra("COIN_ID");
        CoinName = intent.getStringExtra("COIN_NAME");
        CoinSymbol = intent.getStringExtra("COIN_SYMBOL");
        CoinRank = intent.getStringExtra("COIN_RANK");
        CoinPrice = intent.getStringExtra("COIN_PRICE");
        CoinPriceBTC = intent.getStringExtra("COIN_PRICEBTC");
        CoinP1H = intent.getStringExtra("COIN_P1H");
        CoinP24H = intent.getStringExtra("COIN_P24H");
        CoinP7D = intent.getStringExtra("COIN_P7D");
        CoinMCAP = intent.getStringExtra("COIN_MCAP");
        CoinV24H = intent.getStringExtra("COIN_V24H");
        CoinCS = intent.getStringExtra("COIN_CS");
        CoinMS = intent.getStringExtra("COIN_MS");
        CoinUsage = intent.getStringExtra("COIN_USAGE");

        setInformationAboutCoin();
        getCoinInfo();
        getCoinInfoTicker();
        getCoinImage();

        getPriceInfo(CoinSymbol, M15, false, false);
    }

    public void setInformationAboutCoin() {
        LinearLayout MainContainer = findViewById(R.id.CoinContainer);

        TextView tvRank = MainContainer.findViewById(R.id.tvRank);
        TextView tvCategory = MainContainer.findViewById(R.id.tvCategory);
        TextView tvCoinPRICE = MainContainer.findViewById(R.id.tvCoinPRICE);
        TextView tvCoinPriceBTC = MainContainer.findViewById(R.id.tvCoinPriceBTC);
        TextView tvPriceChange = MainContainer.findViewById(R.id.tvPriceChange);
        TextView tvPercentOneHour = MainContainer.findViewById(R.id.tvPercentOneHour);
        TextView tvPercentTwentyFourHour = MainContainer.findViewById(R.id.tvPercentTwentyFourHour);
        TextView tvPercentSevenDays = MainContainer.findViewById(R.id.tvPercentSevenDays);
        TextView tvMarketCap = MainContainer.findViewById(R.id.tvMarketCap);
        TextView tvVolume24 = MainContainer.findViewById(R.id.tvVolume24);
        TextView tvCirculatingSupply = MainContainer.findViewById(R.id.tvCirculatingSupply);
        TextView tvTotalSupply = MainContainer.findViewById(R.id.tvTotalSupply);

        TextView tvMarketNumber = MainContainer.findViewById(R.id.tvRankData);
        TextView tvCoin = MainContainer.findViewById(R.id.tvCoinSymbolData);
        TextView tvCoinName = MainContainer.findViewById(R.id.tvCoinNAMEData);
        TextView tvCoinPrice = findViewById(R.id.tvCoinPRICEData);
        TextView tvCoinPriceBTCData = findViewById(R.id.tvCoinPriceBTCData);
        TextView tvPercentOneHourData = findViewById(R.id.tvPercentOneHourData);
        TextView tvPercentTwentyFourHourData = findViewById(R.id.tvPercentTwentyFourHourData);
        TextView tvPercentSevenDaysData = findViewById(R.id.tvPercentSevenDaysData);
        TextView tvMarketCapData = findViewById(R.id.tvMarketCapData);
        TextView tvVolume24Data = findViewById(R.id.tvVolume24Data);
        TextView tvCirculatingSupplyData = findViewById(R.id.tvCirculatingSupplyData);
        TextView tvTotalSupplyData = findViewById(R.id.tvTotalSupplyData);
        TextView tvUsage = findViewById(R.id.tvUsage);
        TextView tvEmission = findViewById(R.id.tvEmission);

        tvRank.setText(getResources().getString(R.string.Rank));
        tvCategory.setText(getResources().getString(R.string.Category));
        tvCoinPRICE.setText(getResources().getString(R.string.Price));
        tvCoinPriceBTC.setText(getResources().getString(R.string.PriceBTC));
        tvPriceChange.setText(getResources().getString(R.string.PriceChange));
        tvPercentOneHour.setText(getResources().getString(R.string.PercentOneHour));
        tvPercentTwentyFourHour.setText(getResources().getString(R.string.PercentTwentyFourHour));
        tvPercentSevenDays.setText(getResources().getString(R.string.PercentSevenDays));
        tvMarketCap.setText(getResources().getString(R.string.MarketCapitalization));
        tvVolume24.setText(getResources().getString(R.string.Volume24Hours));
        tvCirculatingSupply.setText(getResources().getString(R.string.CirculatingSupply));
        tvTotalSupply.setText(getResources().getString(R.string.TotalSupply));

        tvPercentOneHourData.setText(CoinP1H);
        tvPercentTwentyFourHourData.setText(CoinP24H);
        tvPercentSevenDaysData.setText(CoinP7D);

        if (Float.valueOf(CoinP1H.substring(0, CoinP1H.length() - 1).replaceAll("%", "")) > 0) {
            tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (Float.valueOf(CoinP1H.substring(0, CoinP1H.length() - 1).replaceAll("%", "")) < 0) {
            tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorRed));
        }
        if (Float.valueOf(CoinP24H.substring(0, CoinP24H.length() - 1).replaceAll("%", "")) > 0) {
            tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (Float.valueOf(CoinP24H.substring(0, CoinP24H.length() - 1).replaceAll("%", "")) < 0) {
            tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorRed));
        }
        if (Float.valueOf(CoinP7D.substring(0, CoinP7D.length() - 1).replaceAll("%", "")) > 0) {
            tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (Float.valueOf(CoinP7D.substring(0, CoinP7D.length() - 1).replaceAll("%", "")) < 0) {
            tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorRed));
        }

        tvMarketNumber.setText(CoinRank);
        tvCoin.setText(CoinSymbol);
        tvCoinName.setText(CoinName);
        tvCoinPrice.setText(CoinPrice);

        Float coinpricebtc = Float.parseFloat(CoinPriceBTC);
        DecimalFormat form = new DecimalFormat("#0.00000000");
        tvCoinPriceBTCData.setText(form.format(coinpricebtc));

        tvMarketCapData.setText(CoinMCAP);
        tvVolume24Data.setText(CoinV24H);

        if (!CoinCS.equals("") && CoinCS != null && !CoinCS.equals("0") && !CoinCS.equals("null")) {
            BigDecimal BD = new BigDecimal(CoinCS);
            BD = new BigDecimal(BD.toPlainString());
            String cs = BD.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
            String CS = String.format("%,d", Long.parseLong(cs.replace(".0", "")));
            tvCirculatingSupplyData.setText(CS.replaceAll(",", " "));

            if (!CoinMS.equals("") && CoinMS != null && !CoinMS.equals("0") && !CoinMS.equals("null")) {
                BigDecimal B = new BigDecimal(CoinMS.replaceAll(" ", ""));
                B = new BigDecimal(B.toPlainString());
                String ms = B.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
                String MS = String.format("%,d", Long.parseLong(ms.replace(".0", "")));
                tvTotalSupplyData.setText(MS.replaceAll(",", " "));

            } else {
                tvTotalSupplyData.setText(R.string.data_not_found);
                tvTotalSupplyData.setTextColor(getResources().getColor(R.color.Gray1));
            }

        } else {
            tvCirculatingSupplyData.setText(R.string.data_not_found);
            tvCirculatingSupplyData.setTextColor(getResources().getColor(R.color.Gray1));
        }



        if (!CoinCS.equals("") && CoinCS != null && !CoinCS.equals("0") && !CoinCS.equals("null") &&
                !CoinMS.equals("") && CoinMS != null && !CoinMS.equals("0") &&
                !CoinMS.equals("null")) {
            String emission = df3.format(Double.valueOf(Double.valueOf(CoinCS) /
                    Double.valueOf(CoinMS) * 100)) + getResources().getString(R.string.percent) +
                    " emission";
            tvEmission.setText(emission);
        }

        CoinUsage = Double.valueOf(Double.valueOf(CoinUsage) * 100).toString();
        CoinUsage = df3.format(Double.valueOf(CoinUsage));
        CoinUsage += getResources().getString(R.string.percent) + " of cap.";
        tvUsage.setText(CoinUsage);
    }

    private void getCoinInfo(){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchInfo.getJSONinfo(CoinId.toLowerCase());
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setCoinInfo(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void setCoinInfo(JSONObject json) {
        LinearLayout MainContainer = findViewById(R.id.CoinContainer);

        try {
            JSONArray DataArray = json.getJSONArray(categories);
            String obj = DataArray.toString();

            JSONObject DataArray1 = json.getJSONObject(description);
            String obj1 = DataArray1.getString(en);

            TextView tvCategoryData = MainContainer.findViewById(R.id.tvCategoryData);

            WebView tvDescriptionData = MainContainer.findViewById(R.id.tvDescriptionData);
            String text;

            if (obj.equals("[]") && obj1.equals("[]")) {
                tvCategoryData.setText(R.string.data_not_found);
                tvCategoryData.setTextColor(getResources().getColor(R.color.Gray1));
                text = "<html><body><p align=\"justify\">";
                text += getResources().getString(R.string.data_not_found);
                text+= "</p></body></html>";
                tvDescriptionData.loadData(text, "text/html", "utf-8");
            } else {
                TextView tvDescription = MainContainer.findViewById(R.id.tvDescription);
                tvDescription.setVisibility(View.VISIBLE);

                tvCategoryData.setText(obj.substring(1, obj.length() - 1)
                        .replaceAll(",",", ")
                        .replaceAll("\"", "")
                        .replaceAll("\",\"", "\", \""));

                text = "<html><body><p align=\"justify\">";
                text += obj1.replaceAll("\\r\\n", " ")
                        .replaceAll("—", "-")
                        .replaceAll("”", "\"")
                        .replaceAll("“", "\"")
                        .replaceAll("’", "'")
                        .replaceAll("‘", "'")
                        .replaceAll("–", "-");
                text+= "</p></body></html>";
                tvDescriptionData.loadData(text, "text/html", "utf-8");
            }
        } catch (Exception e) {
        }
    }

    private void getCoinInfoTicker(){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchInfoTicker.getJSONinfo(CoinSymbol.toLowerCase());
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setCoinInfoTicker(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void setCoinInfoTicker(JSONObject json) {
        LinearLayout MainContainer = findViewById(R.id.CoinContainer);
        TableLayout tlMarkets = MainContainer.findViewById(R.id.tlMarkets);
        tlMarkets.setStretchAllColumns(true);
        try {
            JSONObject DataArray1 = json.getJSONObject(ticker);
            JSONArray arr = DataArray1.getJSONArray(markets);
            if (!arr.toString().equals("[]") && !arr.toString().equals("")
                    && !arr.toString().equals("{\"success\":false,\"error\":\"Pair not found\"}")) {

                if (arr.length() != 0) {
                    TextView tvMarkets = MainContainer.findViewById(R.id.tvMarkets);
                    tvMarkets.setText(getResources().getString(R.string.Markets));
                    tvMarkets.setVisibility(View.VISIBLE);

                    TextView tvCol1 = MainContainer.findViewById(R.id.tvColumn1);
                    TextView tvCol2 = MainContainer.findViewById(R.id.tvColumn2);
                    TextView tvCol3 = MainContainer.findViewById(R.id.tvColumn3);
                    tvCol1.setText(getResources().getString(R.string.Exchange));
                    tvCol2.setText(getResources().getString(R.string.PriceDollar));
                    tvCol3.setText(getResources().getString(R.string.Volume24));
                    tvCol1.setVisibility(View.VISIBLE);
                    tvCol2.setVisibility(View.VISIBLE);
                    tvCol3.setVisibility(View.VISIBLE);
                }

                for (int i = -1; i < arr.length(); i++) {
                    TableRow tableRow = new TableRow(this);
                    if (i >= 0) {
                        TextView tv1 = new TextView(this);
                        TextView tv2 = new TextView(this);
                        TextView tv3 = new TextView(this);
                        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        tv1.setText(arr.getJSONObject(i).getString(market));
                        String p = arr.getJSONObject(i).getString(price);
                        String pr = p;
                        boolean scale = false;
                        while (scale) {
                            if (Double.valueOf(p) > 1) {
                                p = df3.format(Double.valueOf(p));
                                scale = true;
                            } else {
                                for (int j = 0; j < 10; j++) {
                                    pr = pr.substring(0, pr.length() - j);
                                    if (Double.valueOf(pr) == 0) {
                                        p = p.substring(0, p.length() - j + 2);
                                        scale = true;
                                    }
                                }
                            }
                        }
                        tv2.setText(p);
                        String vol = arr.getJSONObject(i).getString(volume);
                        vol = df3.format(Double.valueOf(vol));
                        tv3.setText(vol);

                        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        tableRow.addView(tv1, 0);
                        tableRow.addView(tv2, 1);
                        tableRow.addView(tv3, 2);
                    }
                    tlMarkets.addView(tableRow);
                }
            } else {
            }


        } catch (Exception e) {
        }
    }

    private void getCoinImage(){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchCoinImage.getJSONinfo(CoinSymbol);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setCoinImage(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void setCoinImage(JSONObject js) {
        try {
            String IMAGE_URL = "https://www.cryptocompare.com";

            JSONObject Obj1 = js.getJSONObject(RAW);
            JSONObject Obj2 = Obj1.getJSONObject(CoinSymbol);
            JSONObject Obj3 = Obj2.getJSONObject(USD);
            String Obj4 = Obj3.getString(IMAGEURL);

            IMAGE_URL += Obj4;

            ImageView imageViewCoin = findViewById(R.id.imageViewCoin);
            Glide.with(this)
                    .load(IMAGE_URL)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewCoin);
        } catch (Exception e) {
        }
    }

    public void getGraph(View view) {
        LinearLayout graph_linear_layout = findViewById(R.id.graph_linear_layout);
        graph_linear_layout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        final View item = inflater.inflate(R.layout.item_graph, graph_linear_layout, false);

        graph_linear_layout.addView(item);

        GraphView graph = item.findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.setVisibility(View.INVISIBLE);

        switch (view.getId()) {
            case R.id.btnM15:
                getPriceInfo(CoinSymbol, M15, false, false);
                break;
            case R.id.btnM30:
                getPriceInfo(CoinSymbol, M30, false, false);
                break;
            case R.id.btnH1:
                getPriceInfo(CoinSymbol, H1, false, false);
                break;
            case R.id.btnH4:
                getPriceInfo(CoinSymbol, H4, true, false);
                break;
            case R.id.btnD1:
                getPriceInfo(CoinSymbol, D1, false, true);
                break;
        }
    }

    private void getPriceInfo(final String CoinSymbol, final String period, final boolean h4, final boolean d1){
        new Thread() {
            public void run() {
                final JSONArray json = RemoteFetchGraph.getJSON(CoinSymbol, period);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onDrawGraph(json, h4, d1, period);
                        }
                    });
                }
            }
        }.start();
    }

    public void onDrawGraph(JSONArray json, boolean h4, boolean d1, String period){

        LinearLayout graph_linear_layout = findViewById(R.id.graph_linear_layout);
        graph_linear_layout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        final View item = inflater.inflate(R.layout.item_graph, graph_linear_layout, false);

        graph_linear_layout.addView(item);

        GraphView graph = item.findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.setVisibility(View.INVISIBLE);

        graph.removeAllSeries();
        graph.setVisibility(View.INVISIBLE);

        int i = json.length();
        DataPoint[] dp = new DataPoint[i];

        String dt = "";
        int data_num = 0;

        for (int o = 0; o < i; o++){
            try {
                String a = json.getJSONObject(o).getString("timestamp");
                a = a.substring(5, 10);
                if (o == 0 && !h4 && !d1) {
                    dt = a;
                    data_num += 1;
                }
                if(!dt.equals(a) && !h4 && !d1) {
                    data_num += 1;
                    dt = a;
                }
                if(!dt.equals(a) && h4 && !d1 && ( o % 25 == 0) ) {
                    data_num += 1;
                    dt = a;
                }
                if(!dt.equals(a) && !h4 && d1 && ( o % 25 == 0) ) {
                    data_num += 1;
                    dt = a;
                }
                if (o == i - 1 && ( h4 || d1 )) {
                    dt = a;
                    data_num += 1;
                }
            } catch (JSONException er) {
                er.printStackTrace();
            }
        }

        if (data_num > 5) data_num = 5;

        String[] dates = new String[data_num];
        int e = 0;
        String a_curr = "";
        for (int x = 0; x < i; x++) {
            try {
                String a = json.getJSONObject(x).getString("timestamp");
                a = a.substring(5, 10);
                if (x == 0 && !h4 && !d1) {
                    a_curr = a;
                    dates[e] = a;
                    e++;
                }
                if (!a_curr.equals(a) && !h4 && !d1 && e <= data_num - 1) {
                    a_curr = a;
                    dates[e] = a;
                    e++;
                }
                if (!a_curr.equals(a) && h4 && !d1 && (x % 25 == 0) && e < data_num - 1) {
                    a_curr = a;
                    dates[e] = a;
                    e++;
                }
                if (!a_curr.equals(a) && !h4 && d1 && (x % 25 == 0) && e < data_num - 1) {
                    a_curr = a;
                    dates[e] = a;
                    e++;
                }
                if (x == i - 1 && ( h4 || d1 )) {
                    a_curr = a;
                    dates[e] = a;
                    e++;
                }
                String b = json.getJSONObject(x).getString("close");
                dp[x] = new DataPoint(x, Double.parseDouble(b));

            } catch (JSONException er) {
                er.printStackTrace();
                dp[x] = new DataPoint(x, 1);
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
        series.setThickness(8);
        series.setColor(Color.parseColor("#75a478"));
        series.setDrawBackground(true);
        graph.addSeries(series);
        graph.setTitle(graph_title);
        graph.setTitleTextSize(40);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dates);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.setVisibility(View.VISIBLE);

        Button m15 = findViewById(R.id.btnM15);
        Button m30 = findViewById(R.id.btnM30);
        Button h1 = findViewById(R.id.btnH1);
        Button h44 = findViewById(R.id.btnH4);
        Button d11 = findViewById(R.id.btnD1);

        if (period.equals(M15)) {
            m15.setBackgroundColor(Color.parseColor("#75a478"));
            m30.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h1.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h44.setBackgroundColor(Color.parseColor("#a5d6a7"));
            d11.setBackgroundColor(Color.parseColor("#a5d6a7"));
        } else if (period.equals(M30)) {
            m15.setBackgroundColor(Color.parseColor("#a5d6a7"));
            m30.setBackgroundColor(Color.parseColor("#75a478"));
            h1.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h44.setBackgroundColor(Color.parseColor("#a5d6a7"));
            d11.setBackgroundColor(Color.parseColor("#a5d6a7"));
        } else if (period.equals(H1)) {
            m15.setBackgroundColor(Color.parseColor("#a5d6a7"));
            m30.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h1.setBackgroundColor(Color.parseColor("#75a478"));
            h44.setBackgroundColor(Color.parseColor("#a5d6a7"));
            d11.setBackgroundColor(Color.parseColor("#a5d6a7"));
        } else if (period.equals(H4)) {
            m15.setBackgroundColor(Color.parseColor("#a5d6a7"));
            m30.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h1.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h44.setBackgroundColor(Color.parseColor("#75a478"));
            d11.setBackgroundColor(Color.parseColor("#a5d6a7"));
        } else if (period.equals(D1)) {
            m15.setBackgroundColor(Color.parseColor("#a5d6a7"));
            m30.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h1.setBackgroundColor(Color.parseColor("#a5d6a7"));
            h44.setBackgroundColor(Color.parseColor("#a5d6a7"));
            d11.setBackgroundColor(Color.parseColor("#75a478"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}