package app.pavel.coindata;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CoinInfoActivity extends AppCompatActivity {

    int start = 100;

    DecimalFormat df3 = new DecimalFormat("#.##");

    String data = "data";
    String symbol = "symbol";
    String name = "name";
    String id = "id";

    String rank = "rank";
    String volume24 = "volume24";
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

    Handler handler;

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

        /**
         Toast.makeText(this, "CoinId " + CoinId + "CoinName " + CoinName + "CoinSymbol " + CoinSymbol
         + "CoinRank " + CoinRank + "CoinPrice " + CoinPrice + "CoinPriceBTC " + CoinPriceBTC + "CoinP1H " + CoinP1H
         + "CoinP24H " + CoinP24H + "CoinP7D " + CoinP7D + "CoinMCAP " + CoinMCAP + "CoinV24H " + CoinV24H
         + "CoinCS " + CoinCS + "CoinMS " + CoinMS + "CoinUsage " + CoinUsage, Toast.LENGTH_LONG).show();
         */

        setInformationAboutCoin();
        getCoinInfo();
        getCoinInfoTicker();
        getCoinImage();
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

            if (!CoinMS.equals("") && CoinMS != null && !CoinMS.equals("0") && !CoinMS.equals("null")
                    && Double.parseDouble(CoinCS) < Double.parseDouble(CoinMS)) {
                BigDecimal B = new BigDecimal(CoinMS.replaceAll(" ", ""));
                B = new BigDecimal(B.toPlainString());
                String ms = B.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
                String MS = String.format("%,d", Long.parseLong(ms.replace(".0", "")));
                tvTotalSupplyData.setText(MS.replaceAll(",", " "));
            } else tvTotalSupplyData.setText(R.string.data_not_found);

        } else tvCirculatingSupplyData.setText(R.string.data_not_found);



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
        // Toast.makeText(this, "CoinUsage " + CoinUsage, Toast.LENGTH_LONG).show();
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
                text = "<html><body><p align=\"justify\">";
                text += getResources().getString(R.string.data_not_found);
                text+= "</p></body></html>";
                tvDescriptionData.loadData(text, "text/html", "utf-8");
            } else {
                TextView tvDescription = MainContainer.findViewById(R.id.tvDescription);
                tvDescription.setVisibility(View.VISIBLE);

                tvCategoryData.setText(obj.substring(1, obj.length() - 1)
                        .replaceAll("\",\"", "\", \""));

                text = "<html><body><p align=\"justify\">";
                text += obj1.replaceAll("\\r\\n", " ")
                        .replaceAll("—", "-")
                        .replaceAll("”", "\"")
                        .replaceAll("“", "\"")
                        .replaceAll("’", "'")
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
                    tvMarkets.setVisibility(View.VISIBLE);
                    TextView tvCol1 = MainContainer.findViewById(R.id.tvColumn1);
                    TextView tvCol2 = MainContainer.findViewById(R.id.tvColumn2);
                    TextView tvCol3 = MainContainer.findViewById(R.id.tvColumn3);
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
                            Toast.makeText(CoinInfoActivity.this,
                                    getString(R.string.data_not_found),
                                    Toast.LENGTH_LONG).show();
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
                    .into(imageViewCoin);
        } catch (Exception e) {
            Log.e("error image", e.toString());
        }
    }
}

