package app.pavel.coindata;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinInfoActivity extends AppCompatActivity {

    private Typeface roboto_light;

    private final DecimalFormat df3 = new DecimalFormat("#.##");

    private static final String categories = "categories";
    private static final String description = "description";
    private static final String hashing_algorithm = "hashing_algorithm";
    private static final String links = "links";
    private static final String homepage = "homepage";
    private static final String blockchain_site = "blockchain_site";
    private static final String official_forum_url = "official_forum_url";
    private static final String genesis_date = "genesis_date";
    private static final String ticker = "ticker";
    private static final String en = "en";
    private static final String markets = "markets";
    private static final String market = "market";
    private static final String price = "price";
    private static final String volume = "volume";

    private static final String H1 = "H1";
    private static final String D1 = "D1";
    private static final String M3 = "M3";
    private static final String Y1 = "Y1";
    private static final String Y6 = "Y6";

    private static final String graph_title = "price $ on hitbtc.com";

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

    private Button button1H;
    private Button button1D;
    private Button button3M;
    private Button button1Y;
    private Button button6Y;

    private final Handler handler;

    public CoinInfoActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_info);

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

        Toolbar toolbar = findViewById(R.id.toolbarCoinInfo);
        toolbar.setTitle(CoinName);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        button1H = findViewById(R.id.btn1H);
        button1D = findViewById(R.id.btn1D);
        button3M = findViewById(R.id.btn3M);
        button1Y = findViewById(R.id.btn1Y);
        button6Y = findViewById(R.id.btn6Y);

        ImageView toolbarImageView = findViewById(R.id.toolbarImageView);
        ProgressBar progressBarToolbarImage = findViewById(R.id.progressBarToolbarImage);

        CoinActivity.setCoinListImage(CoinId, toolbarImageView, progressBarToolbarImage,
                this, 150);

        getPriceInfo(CoinSymbol, H1);

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
        TextView tvSymbolData = MainContainer.findViewById(R.id.tvSymbolData);
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

        //tvPercentOneHourData.setText(CoinP1H);
        //tvPercentTwentyFourHourData.setText(CoinP24H);
        //tvPercentSevenDaysData.setText(CoinP7D);

        formatPercentValues(Float.parseFloat(CoinP1H), tvPercentOneHourData);
        formatPercentValues(Float.parseFloat(CoinP24H), tvPercentTwentyFourHourData);
        formatPercentValues(Float.parseFloat(CoinP7D), tvPercentSevenDaysData);

        tvMarketNumber.setText(CoinRank);
        tvSymbolData.setText(CoinSymbol);
        tvCoinPrice.setText(CoinPrice);

        Float coin_price_in_btc = Float.parseFloat(CoinPriceBTC);
        DecimalFormat form = new DecimalFormat("#0.00000000");
        tvCoinPriceBTCData.setText(form.format(coin_price_in_btc));

        tvMarketCapData.setText(CoinMCAP);
        tvVolume24Data.setText(CoinV24H);

        if (!CoinCS.equals("") && !CoinCS.equals("0") && !CoinCS.equals("null")) {
            BigDecimal BD = new BigDecimal(CoinCS);
            BD = new BigDecimal(BD.toPlainString());
            String cs = BD.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
            String CS = String.format("%,d", Long.parseLong(cs.replace(".0", "")));
            tvCirculatingSupplyData.setText(CS.replaceAll(",", " "));

            if (!CoinMS.equals("") && !CoinMS.equals("0") && !CoinMS.equals("null")) {
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

        if (!CoinCS.equals("") && !CoinCS.equals("0") && !CoinCS.equals("null") &&
                !CoinMS.equals("") && !CoinMS.equals("0") &&
                !CoinMS.equals("null")) {
            String emission = df3.format(Double.valueOf(Double.parseDouble(CoinCS) /
                    Double.parseDouble(CoinMS) * 100)) + getResources().getString(R.string.percent) +
                    " emission";
            tvEmission.setText(emission);
        }

        CoinUsage = Double.valueOf(Double.parseDouble(CoinUsage) * 100).toString();
        CoinUsage = df3.format(Double.valueOf(CoinUsage));
        CoinUsage += getResources().getString(R.string.percent) + " of cap.";
        tvUsage.setText(CoinUsage);
    }

    private void formatPercentValues(float number, TextView textViewPercent) {
        if (number > 0) {
            textViewPercent.append("+");
            textViewPercent.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (number < 0) {
            textViewPercent.setTextColor(getResources().getColor(R.color.colorRed));
        }

        textViewPercent.append(number + "%");
    }

    private void getCoinInfo(){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchInfo.getJSONinfo(CoinId.toLowerCase());
                if (json != null) {
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
            try {
                String hash_alg = json.getString(hashing_algorithm);

                TextView tvHashingAlgorithmData =
                        MainContainer.findViewById(R.id.tvHashingAlgorithmData);

                if (!hash_alg.equals("null")) {
                    tvHashingAlgorithmData.setText(hash_alg);
                } else {
                    tvHashingAlgorithmData.setTextColor(getResources().getColor(R.color.Gray1));
                    tvHashingAlgorithmData.setText(R.string.data_not_found);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject links_obj = json.getJSONObject(links);
                String homepage_str = links_obj.getString(homepage);

                TextView tvHomepageData =
                        MainContainer.findViewById(R.id.tvHomepageData);

                setUrlData(homepage_str, tvHomepageData);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject links_obj = json.getJSONObject(links);
                String blockchain_site_str = links_obj.getString(blockchain_site);

                TextView tvBlockchainSiteData =
                        MainContainer.findViewById(R.id.tvBlockchainSiteData);

                setUrlData(blockchain_site_str, tvBlockchainSiteData);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String gen_date = json.getString(genesis_date);

                TextView tvGenesisDateData =
                        MainContainer.findViewById(R.id.tvGenesisDateData);

                if (!gen_date.equals("null")) {
                    tvGenesisDateData.setText(gen_date);
                } else {
                    tvGenesisDateData.setTextColor(getResources().getColor(R.color.Gray1));
                    tvGenesisDateData.setText(R.string.data_not_found);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONArray DataArray = json.getJSONArray(categories);
            String obj = DataArray.toString();

            JSONObject DataArray1 = json.getJSONObject(description);
            String obj1 = DataArray1.getString(en);

            TextView tvCategoryData = MainContainer.findViewById(R.id.tvCategoryData);

            //WebView tvDescriptionData = MainContainer.findViewById(R.id.tvDescriptionData);
            TextView tvDescriptionData2 = findViewById(R.id.tvDescriptionData);

            String text;

            try {
                if (obj.equals("[]") ) {
                    tvCategoryData.setText(R.string.data_not_found);
                    tvCategoryData.setTextColor(getResources().getColor(R.color.Gray1));
                } else {
                    tvCategoryData.setText(obj.substring(1, obj.length() - 1)
                            .replaceAll(",",", ")
                            .replaceAll("\"", "")
                            .replaceAll("\",\"", "\", \""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (obj1.equals("[]")) {
                    text = "<html><body><p align=\"justify\">";
                    text += getResources().getString(R.string.data_not_found);
                    text+= "</p></body></html>";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvDescriptionData2.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvDescriptionData2.setText(Html.fromHtml(text));
                    }
                } else {
                    TextView tvDescription = MainContainer.findViewById(R.id.tvDescription);
                    tvDescription.setVisibility(View.VISIBLE);

                    text = "<html><body style=\"color:black;font-size:15px;\"><p align=\"justify\">";
                    text += obj1.replaceAll("\\r\\n", " ")
                            .replaceAll("—", "-")
                            .replaceAll("”", "\"")
                            .replaceAll("“", "\"")
                            .replaceAll("’", "'")
                            .replaceAll("‘", "'")
                            .replaceAll("–", "-");
                    text+= "</p></body></html>";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvDescriptionData2.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvDescriptionData2.setText(Html.fromHtml(text));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUrlData(String data, TextView textView) {
        if (!data.equals("null")) {

            data = data.replaceAll(",,", "");

            textView.setText(data
                    .replaceAll("\\p{Ps}", "")
                    .replaceAll("\\p{Pe}", "")
                    .replaceAll("\\\\","")
                    .replaceAll(",", "\n")
                    .replaceAll("\"",""));
        } else {
            textView.setTextColor(getResources().getColor(R.color.Gray1));
            textView.setText(R.string.data_not_found);
        }
    }

    private void getCoinInfoTicker(){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchInfoTicker.getJSONinfo(CoinSymbol.toLowerCase());
                if (json != null) {
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

    public void getGraph(View view) {

        switch (view.getId()) {
            case R.id.btn1H:
                ColorStateList m15List = button1H.getTextColors();
                int m15color = m15List.getDefaultColor();
                if (m15color == Color.WHITE) {
                    disableButtons(button1H, button1D, button3M, button1Y, button6Y);
                    getPriceInfo(CoinSymbol, H1);
                }
                break;
            case R.id.btn1D:
                ColorStateList m30List = button1D.getTextColors();
                int m30color = m30List.getDefaultColor();
                if (m30color == Color.WHITE) {
                    disableButtons(button1H, button1D, button3M, button1Y, button6Y);
                    getPriceInfo(CoinSymbol, D1);
                }
                break;
            case R.id.btn3M:
                ColorStateList h1List = button3M.getTextColors();
                int h1color = h1List.getDefaultColor();
                if (h1color == Color.WHITE) {
                    disableButtons(button1H, button1D, button3M, button1Y, button6Y);
                    getPriceInfo(CoinSymbol, M3);
                }
                break;
            case R.id.btn1Y:
                ColorStateList h4List = button1Y.getTextColors();
                int h4color = h4List.getDefaultColor();
                if (h4color == Color.WHITE) {
                    disableButtons(button1H, button1D, button3M, button1Y, button6Y);
                    getPriceInfo(CoinSymbol, Y1);
                }
                break;
            case R.id.btn6Y:
                ColorStateList d1List = button6Y.getTextColors();
                int d1color = d1List.getDefaultColor();
                if (d1color == Color.WHITE) {
                    disableButtons(button1H, button1D, button3M, button1Y, button6Y);
                    getPriceInfo(CoinSymbol, Y6);
                }
                break;
        }
    }

    private void disableButtons(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5) {
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        btn5.setEnabled(false);
    }

    private void enableButtons(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5) {
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn4.setEnabled(true);
        btn5.setEnabled(true);
    }

    private void getPriceInfo(final String CoinSymbol, final String period){

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
                            onDrawGraph(json, period);
                        }
                    });
                }
            }
        }.start();
    }

    private void onDrawGraph(JSONArray json, String period){

        LinearLayout graph_linear_layout = findViewById(R.id.graph_linear_layout);
        graph_linear_layout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        final View item = inflater.inflate(R.layout.item_graph, graph_linear_layout, false);

        graph_linear_layout.addView(item);

        GraphView graph = item.findViewById(R.id.graph);
        graph.removeAllSeries();

        graph.setVisibility(View.INVISIBLE);

        int i = json.length();
        DataPoint[] dp = new DataPoint[i];

        for (int j = 0; j < i; j++) {
            try {
                String close_price = json.getJSONObject(j).getString("close");
                dp[j] = new DataPoint(j, Double.parseDouble(close_price));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
        series.setThickness(5);
        series.setColor(Color.parseColor("#75a478"));
        series.setDrawBackground(true);
        graph.addSeries(series);
        graph.setTitle(graph_title);
        graph.setTitleTextSize(40);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        graph.setVisibility(View.VISIBLE);

        button1H = findViewById(R.id.btn1H);
        button1D = findViewById(R.id.btn1D);
        button3M = findViewById(R.id.btn3M);
        button1Y = findViewById(R.id.btn1Y);
        button6Y = findViewById(R.id.btn6Y);

        switch (period) {
            case H1:
                button1H.setTextColor(getResources().getColor(R.color.Black));
                button1D.setTextColor(getResources().getColor(R.color.colorWhite));
                button3M.setTextColor(getResources().getColor(R.color.colorWhite));
                button1Y.setTextColor(getResources().getColor(R.color.colorWhite));
                button6Y.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case D1:
                button1H.setTextColor(getResources().getColor(R.color.colorWhite));
                button1D.setTextColor(getResources().getColor(R.color.Black));
                button3M.setTextColor(getResources().getColor(R.color.colorWhite));
                button1Y.setTextColor(getResources().getColor(R.color.colorWhite));
                button6Y.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case M3:
                button1H.setTextColor(getResources().getColor(R.color.colorWhite));
                button1D.setTextColor(getResources().getColor(R.color.colorWhite));
                button3M.setTextColor(getResources().getColor(R.color.Black));
                button1Y.setTextColor(getResources().getColor(R.color.colorWhite));
                button6Y.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case Y1:
                button1H.setTextColor(getResources().getColor(R.color.colorWhite));
                button1D.setTextColor(getResources().getColor(R.color.colorWhite));
                button3M.setTextColor(getResources().getColor(R.color.colorWhite));
                button1Y.setTextColor(getResources().getColor(R.color.Black));
                button6Y.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case Y6:
                button1H.setTextColor(getResources().getColor(R.color.colorWhite));
                button1D.setTextColor(getResources().getColor(R.color.colorWhite));
                button3M.setTextColor(getResources().getColor(R.color.colorWhite));
                button1Y.setTextColor(getResources().getColor(R.color.colorWhite));
                button6Y.setTextColor(getResources().getColor(R.color.Black));
                break;
        }

        enableButtons(button1H, button1D, button3M, button1Y, button6Y);

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

        try {
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}