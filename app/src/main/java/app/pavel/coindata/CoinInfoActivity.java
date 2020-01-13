package app.pavel.coindata;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
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
import org.xmlpull.v1.XmlPullParser;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinInfoActivity extends AppCompatActivity {

    Typeface roboto_light;

    DecimalFormat df3 = new DecimalFormat("#.##");

    private static final String categories = "categories";
    private static final String description = "description";
    private static final String ticker = "ticker";
    private static final String en = "en";
    private static final String markets = "markets";
    private static final String market = "market";
    private static final String price = "price";
    private static final String volume = "volume";

    private String CoinId = "";
    private String CoinName = "";
    private String CoinSymbol = "";
    private String CoinRank = "";
    private String CoinPrice = "";
    private String CoinPriceBTC = "";
    private String CoinP1H = "";
    private String CoinP24H = "";
    private String CoinP7D = "";
    private String CoinMCAP = "";
    private String CoinV24H = "";
    private String CoinCS = "";
    private String CoinMS = "";
    private String CoinUsage = "";

    private static final String RAW = "RAW";
    private static final String USD = "USD";
    String IMAGEURL = "IMAGEURL";

    private static final String M15 = "M15";
    private static final String M30 = "M30";
    private static final String H1 = "H1";
    private static final String H4 = "H4";
    private static final String D1 = "D1";

    private static final String graph_title = "price $ on hitbtc.com";

    Handler handler;

    public CoinInfoActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info);

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

        getCoinImage();

        getPriceInfo(CoinSymbol, M15, false, false);

        setInformationAboutCoin();
        getCoinInfo();
        getCoinInfoTicker();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            roboto_light = getResources().getFont(R.font.roboto_light);
        } else {
            roboto_light = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_light);
        }
    }

    private void setInformationAboutCoin() {
        LinearLayout MainContainer = findViewById(R.id.CoinContainer);

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

        Float coin_price_in_btc = Float.parseFloat(CoinPriceBTC);
        DecimalFormat form = new DecimalFormat("#0.00000000");
        tvCoinPriceBTCData.setText(form.format(coin_price_in_btc));

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
            e.printStackTrace();
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

                for (int i = -1; i < arr.length(); i++) {
                    TableRow tableRow = new TableRow(this);
                    if (i < 0) {
                        TextView tvCol1 = new TextView(this);
                        TextView tvCol2 = new TextView(this);
                        TextView tvCol3 = new TextView(this);
                        tvCol1.setText(getResources().getString(R.string.Exchange));
                        tvCol2.setText(getResources().getString(R.string.PriceDollar));
                        tvCol3.setText(getResources().getString(R.string.Volume24));
                        setTableTextView(tvCol1);
                        setTableTextView(tvCol2);
                        setTableTextView(tvCol3);

                        tableRow.addView(tvCol1, 0);
                        tableRow.addView(tvCol2, 1);
                        tableRow.addView(tvCol3, 2);
                    }
                    if (i >= 0) {
                        TextView tv1 = new TextView(this);
                        TextView tv2 = new TextView(this);
                        TextView tv3 = new TextView(this);
                        setTableTextView(tv1);
                        setTableTextView(tv2);
                        setTableTextView(tv3);

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

                        tableRow.addView(tv1, 0);
                        tableRow.addView(tv2, 1);
                        tableRow.addView(tv3, 2);
                    }
                    tlMarkets.addView(tableRow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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

        startLoadingAnimation();

        new Thread() {
            public void run() {
                final JSONArray json = RemoteFetchGraph.getJSON(CoinSymbol, period);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            stopLoadingAnimation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //startLoadingAnimation();
                            onDrawGraph(json, h4, d1, period);
                        }
                    });
                }
            }
        }.start();
    }

    private void onDrawGraph(JSONArray json, boolean h4, boolean d1, String period){

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
        series.setThickness(5);
        series.setColor(Color.parseColor("#75a478"));
        series.setDrawBackground(true);
        graph.addSeries(series);
        graph.setTitle(graph_title);
        graph.setTitleTextSize(40);

        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dates);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.setVisibility(View.VISIBLE);

        Button m15 = findViewById(R.id.btnM15);
        Button m30 = findViewById(R.id.btnM30);
        Button h1 = findViewById(R.id.btnH1);
        Button h44 = findViewById(R.id.btnH4);
        Button d11 = findViewById(R.id.btnD1);

        //m15.setTextColor();

        switch (period) {
            case M15:
                m15.setTextColor(getResources().getColor(R.color.Black));
                m30.setTextColor(getResources().getColor(R.color.colorWhite));
                h1.setTextColor(getResources().getColor(R.color.colorWhite));
                h44.setTextColor(getResources().getColor(R.color.colorWhite));
                d11.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case M30:
                m15.setTextColor(getResources().getColor(R.color.colorWhite));
                m30.setTextColor(getResources().getColor(R.color.Black));
                h1.setTextColor(getResources().getColor(R.color.colorWhite));
                h44.setTextColor(getResources().getColor(R.color.colorWhite));
                d11.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case H1:
                m15.setTextColor(getResources().getColor(R.color.colorWhite));
                m30.setTextColor(getResources().getColor(R.color.colorWhite));
                h1.setTextColor(getResources().getColor(R.color.Black));
                h44.setTextColor(getResources().getColor(R.color.colorWhite));
                d11.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case H4:
                m15.setTextColor(getResources().getColor(R.color.colorWhite));
                m30.setTextColor(getResources().getColor(R.color.colorWhite));
                h1.setTextColor(getResources().getColor(R.color.colorWhite));
                h44.setTextColor(getResources().getColor(R.color.Black));
                d11.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case D1:
                m15.setTextColor(getResources().getColor(R.color.colorWhite));
                m30.setTextColor(getResources().getColor(R.color.colorWhite));
                h1.setTextColor(getResources().getColor(R.color.colorWhite));
                h44.setTextColor(getResources().getColor(R.color.colorWhite));
                d11.setTextColor(getResources().getColor(R.color.Black));
                break;
        }

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        stopLoadingAnimation();
    }

    private void startLoadingAnimation() {
        LinearLayout graphContainer = findViewById(R.id.graph_linear_layout);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.progress_bar, graphContainer, false);
        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        graphContainer.addView(item);
    }

    private void stopLoadingAnimation() {
        LinearLayout graphContainer = findViewById(R.id.graph_linear_layout);
        View loading = graphContainer.findViewById(R.id.progress_bar_linear_layout);
        graphContainer.removeView(loading);
    }

    private void setTableTextView(TextView tv) {
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(roboto_light);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        finish();
    }
}