package app.pavel.coindata;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CoinActivity extends AppCompatActivity {

    private int array_coin_len = 0;
    private int start = 50;
    private static final int billion = Integer.parseInt("1000000000");
    private static final int million = Integer.parseInt("1000000");
    private static final int thousand = Integer.parseInt("1000");

    // to avoid double insertion when button Update was pressed ()
    private int update = 1;

    private boolean end_of_list = false;

    DecimalFormat df2 = new DecimalFormat("#.##");

    String data = "data";
    String symbol = "symbol";
    String nameid = "nameid";
    String name = "name";
    String id = "id";
    String rank = "rank";
    String price_usd = "price_usd";
    String price_btc = "price_btc";
    String percent_change_1h = "percent_change_1h";
    String percent_change_24h = "percent_change_24h";
    String percent_change_7d = "percent_change_7d";
    String market_cap_usd = "market_cap_usd";
    String volume24 = "volume24";
    String csupply = "csupply";
    String msupply = "msupply";
    String usage;
    String time;

    String RAW = "RAW";
    String USD = "USD";
    String IMAGEURL = "IMAGEURL";

    String CoinId = "";

    String[][] COIN = new String[2100][15];

    Handler handler;

    public CoinActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setHeader();
        setButton();

        if (savedInstanceState != null) {
            int array_coin_len = savedInstanceState.getInt("array_coin_len");
            start = array_coin_len;
            boolean end = false;
            for (int i = 1; i <= array_coin_len; i++) {
                String ARRAY_NAME = "COIN_" + i;
                String[] ARRAY_COIN;
                ARRAY_COIN = savedInstanceState.getStringArray(ARRAY_NAME);
                if (i == array_coin_len) end = true;
                setCoinInList(ARRAY_COIN != null ? ARRAY_COIN : new String[0], i, end);
            }
            String t = savedInstanceState.getString("time");
            time = t;
            LinearLayout TimeUpdateContainer = findViewById(R.id.TimeUpdateContainer);
            TextView updateTime = TimeUpdateContainer.findViewById(R.id.updateTime);
            if (!Objects.equals(t, null)) {
                updateTime.setText(t);
            } else {
                updateTime.setText("");
            }
        }

        if (savedInstanceState == null) {
            updatePrice();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int len = start;
        array_coin_len = len;
        outState.putInt("array_coin_len", array_coin_len);
        TextView updateTime = findViewById(R.id.updateTime);
        outState.putString("time", updateTime.getText().toString());

        String COIN_NAME, COIN_SYMBOL, COIN_RANK, COIN_PRICE, COIN_PRICEBTC, COIN_P1H, COIN_P24H,
                COIN_P7D, COIN_MCAP, COIN_V24H, COIN_CS, COIN_MS, COIN_ID, COIN_USAGE;

        for (int i = 1; i <= len; i++) {
            if (COIN[i][1] != null) { COIN_NAME = COIN[i][1]; }  else COIN_NAME = "";
            if (COIN[i][2] != null) { COIN_SYMBOL = COIN[i][2]; }  else COIN_SYMBOL = "";
            if (COIN[i][3] != null) { COIN_RANK = COIN[i][3]; }  else COIN_RANK = "";
            if (COIN[i][4] != null) { COIN_PRICE = COIN[i][4]; }  else COIN_PRICE = "";
            if (COIN[i][5] != null) { COIN_PRICEBTC = COIN[i][5]; }  else COIN_PRICEBTC = "";
            if (COIN[i][6] != null) { COIN_P1H = COIN[i][6]; }  else COIN_P1H = "";
            if (COIN[i][7] != null) { COIN_P24H = COIN[i][7]; }  else COIN_P24H = "";
            if (COIN[i][8] != null) { COIN_P7D = COIN[i][8]; }  else COIN_P7D = "";
            if (COIN[i][9] != null) { COIN_MCAP = COIN[i][9]; }  else COIN_MCAP = "";
            if (COIN[i][10] != null) { COIN_V24H = COIN[i][10]; }  else COIN_V24H = "";
            if (COIN[i][11] != null) { COIN_CS = COIN[i][11].replace(".00", ""); }  else COIN_CS = "";
            if (COIN[i][12] != null) { COIN_MS = COIN[i][12].replace(".00", ""); }  else COIN_MS = "";
            if (COIN[i][13] != null) { COIN_ID = COIN[i][13]; }  else COIN_ID = "";
            if (COIN[i][14] != null) { COIN_USAGE = COIN[i][14]; }  else COIN_USAGE = "";

            String[] ARRAY_COIN = new String[]{ "",
                    COIN_NAME, COIN_SYMBOL, COIN_RANK, COIN_PRICE, COIN_PRICEBTC, COIN_P1H,
                    COIN_P24H, COIN_P7D, COIN_MCAP, COIN_V24H, COIN_CS, COIN_MS, COIN_ID,
                    COIN_USAGE
            };
            String ARRAY_NAME = "COIN_" + i;
            outState.putStringArray(ARRAY_NAME, ARRAY_COIN);
        }
    }

    private void setHeader(){
        FrameLayout HeaderContainer = findViewById(R.id.HeaderContainer);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.item_header, HeaderContainer, false);
        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        HeaderContainer.addView(item);
    }

    private void startAnimation() {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.progress_bar, MainContainer, false);
        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        MainContainer.addView(item);
    }

    private void stopAnimation() {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        View loading = MainContainer.findViewById(R.id.progress_bar_linear_layout);
        MainContainer.removeView(loading);
    }

    private void setButton() {
        FrameLayout ButtonContainer = findViewById(R.id.ButtonContainer);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.item_button_update, ButtonContainer, false);
        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        ButtonContainer.addView(item);
    }

    private void clearPrice() {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        MainContainer.removeAllViews();
    }

    private void onAddMoreCoins() {
        start += 50;
        updatePrice();
    }

    private void updatePrice(){
        if (update == 1)
            startAnimation();
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(start);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CoinActivity.this,
                                    getString(R.string.internet_error),
                                    Toast.LENGTH_LONG).show();
                            stopAnimation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 50; i++) {
                                if (i == 49) {
                                    setPrise(json, i, true);
                                } else setPrise(json, i, false);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void setPrise(JSONObject json, int i, boolean end){
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        LayoutInflater inflater = getLayoutInflater();

        final View item = inflater.inflate(R.layout.item_coin, MainContainer, false);

        TextView tvMarketNumber = item.findViewById(R.id.tvMarketNumber);
        TextView tvCoin = item.findViewById(R.id.tvCoin);
        TextView tvCoinName = item.findViewById(R.id.tvCoinName);
        TextView tvCoinPrice = item.findViewById(R.id.tvCoinPrice);
        TextView tvPercentOneHourData = item.findViewById(R.id.tvPercentOneHourData);
        TextView tvPercentTwentyFourHourData = item.findViewById(R.id.tvPercentTwentyFourHourData);
        TextView tvPercentSevenDaysData = item.findViewById(R.id.tvPercentSevenDaysData);
        TextView tvMarketCapData = item.findViewById(R.id.tvMarketCapData);
        TextView tvVolume24Data = item.findViewById(R.id.tvVolume24Data);

        try {
            JSONArray DataArray = json.getJSONArray(data);
            JSONObject obj = DataArray.getJSONObject(i);

            int r = Integer.valueOf(obj.getString(rank));

            CoinId = obj.getString(nameid).toLowerCase();

            String p1h = obj.getString(percent_change_1h) + getResources().getString(R.string.percent);
            String p24h = obj.getString(percent_change_24h) + getResources().getString(R.string.percent);
            String p7d = obj.getString(percent_change_7d) + getResources().getString(R.string.percent);
            tvPercentOneHourData.setText(p1h);
            tvPercentTwentyFourHourData.setText(p24h);
            tvPercentSevenDaysData.setText(p7d);
            COIN[r][6] = p1h;
            COIN[r][7] = p24h;
            COIN[r][8] = p7d;

            if (Float.valueOf(obj.getString(percent_change_1h)) > 0) {
                tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorGreen));
            } else if (Float.valueOf(obj.getString(percent_change_1h)) < 0) {
                tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorRed));
            }
            if (Float.valueOf(obj.getString(percent_change_24h)) > 0) {
                tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorGreen));
            } else if (Float.valueOf(obj.getString(percent_change_24h)) < 0) {
                tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorRed));
            }
            if (Float.valueOf(obj.getString(percent_change_7d)) > 0) {
                tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorGreen));
            } else if (Float.valueOf(obj.getString(percent_change_7d)) < 0) {
                tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorRed));
            }

            tvMarketNumber.setText(obj.getString(rank));
            COIN[r][3] = obj.getString(rank);

            tvCoin.setText(obj.getString(symbol));
            COIN[r][2] = obj.getString(symbol);
            tvCoinName.setText(obj.getString(name));
            COIN[r][1] = obj.getString(name);

            String coinprice = getResources().getString(R.string.dollar) + " " + obj.getString(price_usd);
            tvCoinPrice.setText(coinprice);
            COIN[r][4] = coinprice;
            COIN[r][5] = obj.getString(price_btc);

            COIN[r][13] = obj.getString(nameid);
            COIN[r][12] = obj.getString(msupply);
            COIN[r][11] = obj.getString(csupply);

            String qwerty = obj.getString(market_cap_usd);
            double mcap = Double.parseDouble(qwerty);
            usage = String.valueOf(1 / mcap);
            if (mcap > billion) {
                String marketcap;
                mcap = mcap / billion;
                String marcap = df2.format(mcap);
                marketcap = marcap + " " + "B" + " " + getResources().getString(R.string.dollar);
                tvMarketCapData.setText(marketcap);
                COIN[r][9] = marketcap;
            } else if (mcap > million) {
                String marketcap;
                mcap = mcap / million;
                String marcap = df2.format(mcap);
                marketcap = marcap + " " + "M" + " " + getResources().getString(R.string.dollar);
                tvMarketCapData.setText(marketcap);
                COIN[r][9] = marketcap;
            } else {
                String marketcap;
                marketcap = String.valueOf(mcap);
                tvMarketCapData.setText(marketcap);
                COIN[r][9] = marketcap;
            }

            String qwerty1 = obj.getString(volume24);
            double mcap1 = Double.parseDouble(qwerty1);
            usage = String.valueOf(Double.valueOf(usage)*mcap1);
            if (mcap1 > billion) {
                String marketcap1;
                mcap1 = mcap1 / billion;
                String marcap1 = df2.format(mcap1);
                marketcap1 = marcap1 + " " + "B" + " " + getResources().getString(R.string.dollar);
                tvVolume24Data.setText(marketcap1);
                COIN[r][10] = marketcap1;
            } else if (mcap1 > million) {
                String marketcap1;
                mcap1 = mcap1 / million;
                String marcap1 = df2.format(mcap1);
                marketcap1 = marcap1 + " " + "M" + " " + getResources().getString(R.string.dollar);
                tvVolume24Data.setText(marketcap1);
                COIN[r][10] = marketcap1;
            } else if (mcap1 > thousand){
                String marketcap1;
                mcap1 = mcap1 / thousand;
                String marcap1 = df2.format(mcap1);
                marketcap1 = marcap1 + " " + "K" + " " + getResources().getString(R.string.dollar);
                tvVolume24Data.setText(marketcap1);
                COIN[r][10] = marketcap1;
            }

            COIN[r][14] = usage;

            getCoinListImage(COIN[r][2], item);

        } catch (Exception e) {
            e.printStackTrace();
        }


        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.setTag(CoinId);
        MainContainer.addView(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String COIN_ID = String.valueOf(item.getTag());
                int len = COIN.length;
                for (int i = 0; i < len; i++) {
                    if (COIN_ID.equals(COIN[i][13])) {
                        Intent intent = new Intent(CoinActivity.this, CoinInfoActivity.class);
                        intent.putExtra("COIN_ID", COIN_ID);
                        intent.putExtra("COIN_NAME", COIN[i][1]);
                        intent.putExtra("COIN_SYMBOL", COIN[i][2]);
                        intent.putExtra("COIN_RANK", COIN[i][3]);
                        intent.putExtra("COIN_PRICE", COIN[i][4]);
                        intent.putExtra("COIN_PRICEBTC", COIN[i][5]);
                        intent.putExtra("COIN_P1H", COIN[i][6]);
                        intent.putExtra("COIN_P24H", COIN[i][7]);
                        intent.putExtra("COIN_P7D", COIN[i][8]);
                        intent.putExtra("COIN_MCAP", COIN[i][9]);
                        intent.putExtra("COIN_V24H", COIN[i][10]);
                        intent.putExtra("COIN_CS", COIN[i][11].substring(0, COIN[i][11].length() - 3));
                        intent.putExtra("COIN_MS", COIN[i][12]);
                        intent.putExtra("COIN_USAGE", COIN[i][14]);
                        startActivity(intent);
                    }
                }
            }
        });

        if (end) {
            if (update == 1)
                stopAnimation();
            showMoreCoins();
            showCurrentTime();
            end_of_list = false;
        }
    }

    private void getCoinListImage(final String symbol, final View item){
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetchCoinImage.getJSONinfo(symbol);
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
                            setCoinListImage(json, item, symbol);
                        }
                    });
                }
            }
        }.start();
    }

    private void setCoinListImage(JSONObject js, View item, String symbol) {
        try {
            String IMAGE_URL = "https://www.cryptocompare.com";

            JSONObject Obj1 = js.getJSONObject(RAW);
            JSONObject Obj2 = Obj1.getJSONObject(symbol);
            JSONObject Obj3 = Obj2.getJSONObject(USD);
            String Obj4 = Obj3.getString(IMAGEURL);

            IMAGE_URL += Obj4;

            ImageView imageViewList = item.findViewById(R.id.imageViewList);
            Glide.with(this)
                    .load(IMAGE_URL)
                    .apply(new RequestOptions()
                            .override(100, 100)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickUpdate(View view) {
        clearPrice();
        start = 50;
        update = 1;
        updatePrice();
    }

    private void showMoreCoins() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ScrollView scrollView = findViewById(R.id.scrollView);
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                if (view.getBottom() == scrollView.getScrollY() + scrollView.getHeight()
                        && !end_of_list && start < 2100) {
                    if (update == 1) {
                        update ++;
                    } else if (update > 1) {
                        end_of_list = true;
                        Toast.makeText(CoinActivity.this,getString(R.string.loading),
                                Toast.LENGTH_SHORT).show();
                        onAddMoreCoins();
                    }
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    private void showCurrentTime(){
        TextView updateTime = findViewById(R.id.updateTime);
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        time = ft.format(date);
        updateTime.setText(time);
    }

    private void setCoinInList(String[] ARRAY_COIN, int i, boolean end) {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        LayoutInflater inflater = getLayoutInflater();

        final View item = inflater.inflate(R.layout.item_coin, MainContainer, false);

        TextView tvMarketNumber = item.findViewById(R.id.tvMarketNumber);
        TextView tvCoin = item.findViewById(R.id.tvCoin);
        TextView tvCoinName = item.findViewById(R.id.tvCoinName);
        TextView tvCoinPrice = item.findViewById(R.id.tvCoinPrice);
        TextView tvPercentOneHourData = item.findViewById(R.id.tvPercentOneHourData);
        TextView tvPercentTwentyFourHourData = item.findViewById(R.id.tvPercentTwentyFourHourData);
        TextView tvPercentSevenDaysData = item.findViewById(R.id.tvPercentSevenDaysData);
        TextView tvMarketCapData = item.findViewById(R.id.tvMarketCapData);
        TextView tvVolume24Data = item.findViewById(R.id.tvVolume24Data);

        tvPercentOneHourData.setText(ARRAY_COIN[6]);
        tvPercentTwentyFourHourData.setText(ARRAY_COIN[7]);
        tvPercentSevenDaysData.setText(ARRAY_COIN[8]);
        COIN[i][6] = ARRAY_COIN[6];
        COIN[i][7] = ARRAY_COIN[7];
        COIN[i][8] = ARRAY_COIN[8];

        try {
            if (!ARRAY_COIN[6].equals("") && ARRAY_COIN[6] != null && !ARRAY_COIN[6].equals("null")) {
                if (Float.valueOf(ARRAY_COIN[6].substring(0, ARRAY_COIN[6].length() - 1).replaceAll("%", "")) > 0) {
                    tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorGreen));
                } else if (Float.valueOf(ARRAY_COIN[6].substring(0, ARRAY_COIN[6].length() - 1).replaceAll("%", "")) < 0) {
                    tvPercentOneHourData.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
            if (!ARRAY_COIN[7].equals("") && ARRAY_COIN[7] != null && !ARRAY_COIN[7].equals("null")) {
                if (Float.valueOf(ARRAY_COIN[7].substring(0, ARRAY_COIN[7].length() - 1).replaceAll("%", "")) > 0) {
                    tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorGreen));
                } else if (Float.valueOf(ARRAY_COIN[7].substring(0, ARRAY_COIN[7].length() - 1).replaceAll("%", "")) < 0) {
                    tvPercentTwentyFourHourData.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
            if (!ARRAY_COIN[8].equals("") && ARRAY_COIN[8] != null && !ARRAY_COIN[8].equals("null")) {
                if (Float.valueOf(ARRAY_COIN[8].substring(0, ARRAY_COIN[8].length() - 1).replaceAll("%", "")) > 0) {
                    tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorGreen));
                } else if (Float.valueOf(ARRAY_COIN[8].substring(0, ARRAY_COIN[8].length() - 1).replaceAll("%", "")) < 0) {
                    tvPercentSevenDaysData.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvMarketNumber.setText(ARRAY_COIN[3]);
        tvMarketNumber.setTextColor(getResources().getColor(R.color.Gray0));
        COIN[i][3] = ARRAY_COIN[3];

        tvCoin.setText(ARRAY_COIN[2]);
        COIN[i][2] = ARRAY_COIN[2];
        tvCoinName.setText(ARRAY_COIN[1]);
        COIN[i][1] = ARRAY_COIN[1];

        tvCoinPrice.setText(ARRAY_COIN[4]);
        COIN[i][4] = ARRAY_COIN[4];
        COIN[i][5] = ARRAY_COIN[5];

        String CoinId = ARRAY_COIN[13];
        COIN[i][13] = ARRAY_COIN[13];
        COIN[i][12] = ARRAY_COIN[12];
        COIN[i][11] = ARRAY_COIN[11];

        tvMarketCapData.setText(ARRAY_COIN[9]);
        COIN[i][9] = ARRAY_COIN[9];

        tvVolume24Data.setText(ARRAY_COIN[10]);
        COIN[i][10] = ARRAY_COIN[10];

        COIN[i][14] = ARRAY_COIN[14];

        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.setTag(CoinId);
        MainContainer.addView(item);

        getCoinListImage(COIN[i][2], item);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String COIN_ID = String.valueOf(item.getTag());
                int len = COIN.length;
                for (int i = 0; i < len; i++) {
                    if (COIN_ID.equals(COIN[i][13])) {
                        Intent intent = new Intent(CoinActivity.this, CoinInfoActivity.class);
                        intent.putExtra("COIN_NAME", COIN[i][1]);
                        intent.putExtra("COIN_SYMBOL", COIN[i][2]);
                        intent.putExtra("COIN_RANK", COIN[i][3]);
                        intent.putExtra("COIN_PRICE", COIN[i][4]);
                        intent.putExtra("COIN_PRICEBTC", COIN[i][5]);
                        intent.putExtra("COIN_P1H", COIN[i][6]);
                        intent.putExtra("COIN_P24H", COIN[i][7]);
                        intent.putExtra("COIN_P7D", COIN[i][8]);
                        intent.putExtra("COIN_MCAP", COIN[i][9]);
                        intent.putExtra("COIN_V24H", COIN[i][10]);
                        intent.putExtra("COIN_CS", COIN[i][11]);
                        intent.putExtra("COIN_MS", COIN[i][12]);
                        intent.putExtra("COIN_ID", COIN[i][13]);
                        intent.putExtra("COIN_USAGE", COIN[i][14]);
                        startActivity(intent);
                    }
                }
            }
        });

        if (end) {
            end_of_list = false;
            showMoreCoins();
        }
    }

     public void onClickAbout(View view){
        Intent intent = new Intent(CoinActivity.this, AboutActivity.class);
        startActivity(intent);

         overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}