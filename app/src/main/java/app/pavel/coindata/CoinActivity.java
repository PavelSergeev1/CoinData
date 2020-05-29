package app.pavel.coindata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CoinActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

    private static final int billion = Integer.parseInt("1000000000");
    private static final int million = Integer.parseInt("1000000");
    private static final int thousand = Integer.parseInt("1000");

    private static final String data = "data";
    private static final String rank = "rank";
    private static final String nameid = "nameid";
    private static final String percent_change_1h = "percent_change_1h";
    private static final String percent_change_24h = "percent_change_24h";
    private static final String percent_change_7d = "percent_change_7d";
    private static final String symbol = "symbol";
    private static final String name = "name";
    private static final String price_usd = "price_usd";
    private static final String price_btc = "price_btc";
    private static final String m_supply = "msupply";
    private static final String c_supply = "csupply";
    private static final String market_cap_usd = "market_cap_usd";
    private static final String volume24 = "volume24";

    private static final String symbol_K = "K";
    private static final String symbol_M = "M";
    private static final String symbol_B = "B";
    private static final String space_symbol = " ";
    private static final String dollar_symbol = "$";
    private static final String percent_symbol = "%";
    private static final String plus_symbol = "+";

    private static final String coinlore_url = "https://www.coinlore.com/img/";
    private static final String dot_png = ".png";

    private final int coin_image_size = 100;

    // to avoid double insertion when button Update was pressed ()
    private int update = 1;

    private int start = 50;

    private int colorGreen;
    private int colorRed;

    private boolean end_of_list = false;

    private boolean contentIsShown = false;

    private final DecimalFormat df2 = new DecimalFormat("#.##");

    private String time;

    private String CoinId = "";

    private final String[][] COIN = new String[2100][15];

    private SwipeRefreshLayout swipeRefreshLayout;

    private final Handler handler;

    public CoinActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_coin);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        colorGreen = getResources().getColor(R.color.colorGreen);
        colorRed = getResources().getColor(R.color.colorRed);

        if (isNetworkAvailable()) {
            clearConnectionErrorImage();
            if (savedInstanceState != null) {
                int array_coin_len = savedInstanceState.getInt("array_coin_len");
                start = array_coin_len;
                boolean end = false;
                for (int i = 1; i <= array_coin_len; i++) {
                    String ARRAY_NAME = "COIN_" + i;
                    String[] ARRAY_COIN;
                    ARRAY_COIN = savedInstanceState.getStringArray(ARRAY_NAME);
                    if (i == array_coin_len) end = true;
                    setCoinItemInList(ARRAY_COIN != null ? ARRAY_COIN : new String[0], i, end);
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
            } else {
                updatePrice();
            }
        } else {
            showConnectionError();
            showConnectionErrorImage();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuAbout :
                Intent intent = new Intent(CoinActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.menuSettings :
                Intent intent2 = new Intent(CoinActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.menuExit :
                this.finishAffinity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        onUpdate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (isNetworkAvailable() && contentIsShown) {
            int len = start;
            outState.putInt("array_coin_len", len);
            TextView updateTime = findViewById(R.id.updateTime);
            outState.putString("time", updateTime.getText().toString());

            String COIN_NAME, COIN_SYMBOL, COIN_RANK, COIN_PRICE, COIN_PRICEBTC, COIN_P1H, COIN_P24H,
                    COIN_P7D, COIN_MCAP, COIN_V24H, COIN_CS, COIN_MS, COIN_ID, COIN_USAGE;

            for (int i = 1; i <= len; i++) {
                if (COIN[i][1] != null)
                    COIN_NAME = COIN[i][1];
                else COIN_NAME = "";
                if (COIN[i][2] != null)
                    COIN_SYMBOL = COIN[i][2];
                else COIN_SYMBOL = "";
                if (COIN[i][3] != null)
                    COIN_RANK = COIN[i][3];
                else COIN_RANK = "";
                if (COIN[i][4] != null)
                    COIN_PRICE = COIN[i][4];
                else COIN_PRICE = "";
                if (COIN[i][5] != null)
                    COIN_PRICEBTC = COIN[i][5];
                else COIN_PRICEBTC = "";
                if (COIN[i][6] != null)
                    COIN_P1H = COIN[i][6];
                else COIN_P1H = "";
                if (COIN[i][7] != null)
                    COIN_P24H = COIN[i][7];
                else COIN_P24H = "";
                if (COIN[i][8] != null)
                    COIN_P7D = COIN[i][8];
                else COIN_P7D = "";
                if (COIN[i][9] != null)
                    COIN_MCAP = COIN[i][9];
                else COIN_MCAP = "";
                if (COIN[i][10] != null)
                    COIN_V24H = COIN[i][10];
                else COIN_V24H = "";
                if (COIN[i][11] != null)
                    COIN_CS = COIN[i][11].replace(".00", "");
                else COIN_CS = "";
                if (COIN[i][12] != null)
                    COIN_MS = COIN[i][12].replace(".00", "");
                else COIN_MS = "";
                if (COIN[i][13] != null)
                    COIN_ID = COIN[i][13];
                else COIN_ID = "";
                if (COIN[i][14] != null)
                    COIN_USAGE = COIN[i][14];
                else COIN_USAGE = "";

                String[] ARRAY_COIN = new String[]{"",
                        COIN_NAME, COIN_SYMBOL, COIN_RANK, COIN_PRICE, COIN_PRICEBTC, COIN_P1H,
                        COIN_P24H, COIN_P7D, COIN_MCAP, COIN_V24H, COIN_CS, COIN_MS, COIN_ID,
                        COIN_USAGE
                };
                String ARRAY_NAME = "COIN_" + i;
                outState.putStringArray(ARRAY_NAME, ARRAY_COIN);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showConnectionError() {
        Toast.makeText(CoinActivity.this,
                getString(R.string.internet_error),
                Toast.LENGTH_SHORT).show();
    }

    private void showConnectionErrorImage() {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        LayoutInflater inflater = getLayoutInflater();
        View item = inflater.inflate(R.layout.connection_error_image, MainContainer, false);
        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        MainContainer.addView(item);
    }

    private void clearConnectionErrorImage() {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        try {
            View loading = MainContainer.findViewById(R.id.connectionErrorLayout);
            MainContainer.removeView(loading);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (update == 1) {

            swipeRefreshLayout.setRefreshing(true);

            if (isNetworkAvailable())
                clearConnectionErrorImage();
        }

        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(start);
                if (json == null) {
                    swipeRefreshLayout.setRefreshing(false);

                    showConnectionError();
                    showConnectionErrorImage();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 50; i++) {
                                if (i == 49) {
                                    setPrise(json, i, true);
                                } else setPrise(json, i, false);
                            }

                            contentIsShown = true;
                        }
                    });
                }
            }
        }.start();
    }

    private void setPrise(JSONObject json, int i, boolean end){
        double m_cap = 0, v_24 = 0;

        int r = 0;

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

            r = Integer.parseInt(obj.getString(rank));

            CoinId = obj.getString(nameid).toLowerCase();

            COIN[r][1] = obj.getString(name);
            COIN[r][2] = obj.getString(symbol);
            COIN[r][3] = obj.getString(rank);
            COIN[r][4] = dollar_symbol + space_symbol + obj.getString(price_usd);
            COIN[r][5] = obj.getString(price_btc);
            COIN[r][6] = obj.getString(percent_change_1h);
            COIN[r][7] = obj.getString(percent_change_24h);
            COIN[r][8] = obj.getString(percent_change_7d);

            m_cap = Double.parseDouble(obj.getString(market_cap_usd));

            v_24 = Double.parseDouble(obj.getString(volume24));

            COIN[r][11] = obj.getString(c_supply);
            COIN[r][12] = obj.getString(m_supply);
            COIN[r][13] = obj.getString(nameid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvCoinName.setText(COIN[r][1]);
        tvCoin.setText(COIN[r][2]);
        tvMarketNumber.setText(COIN[r][3]);
        tvCoinPrice.setText(COIN[r][4]);

        formatPercentValues(r, 6, tvPercentOneHourData);
        formatPercentValues(r, 7, tvPercentTwentyFourHourData);
        formatPercentValues(r, 8, tvPercentSevenDaysData);

        formatBigDigits(m_cap, tvMarketCapData);
        formatBigDigits(v_24, tvVolume24Data);

        COIN[r][9] = tvMarketCapData.getText().toString();
        COIN[r][10] = tvVolume24Data.getText().toString();

        String usage = String.valueOf((1 / m_cap) * v_24);

        COIN[r][14] = usage;

        findCoinImage(item);

        findCoinGraph(item, r);

        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.setTag(CoinId);
        MainContainer.addView(item);

        item.setOnClickListener(itemOnClickListener);

        if (end) {
            if (update == 1) {

                swipeRefreshLayout.setRefreshing(false);
            }
            showMoreCoins();
            showCurrentTime();
            end_of_list = true;
        }
    }

    private void formatPercentValues(int rank, int number, TextView textViewPercent) {
        if (Float.parseFloat(COIN[rank][number]) > 0) {
            textViewPercent.append(plus_symbol);
            textViewPercent.setTextColor(colorGreen);
        } else if (Float.parseFloat(COIN[rank][number]) < 0) {
            textViewPercent.setTextColor(colorRed);
        }

        textViewPercent.append(COIN[rank][number] + percent_symbol);
    }

    private void formatBigDigits(Double value, TextView textView) {
        String formatted_value;
        if (value > billion) {
            value = value / billion;
            formatted_value = dollar_symbol + df2.format(value) + space_symbol + symbol_B;
        } else if (value > million) {
            value = value / million;
            formatted_value = dollar_symbol + df2.format(value) + space_symbol + symbol_M;
        } else if (value > thousand) {
            value = value / thousand;
            formatted_value = dollar_symbol + df2.format(value) + space_symbol + symbol_K;
        } else {
            formatted_value = String.valueOf(value);
        }

        textView.setText(formatted_value);
    }

    private void findCoinImage(View item) {
        ImageView imageView = item.findViewById(R.id.imageViewList);
        ProgressBar progressBar = item.findViewById(R.id.progressBarListImage);

        setCoinListImage(CoinId, imageView, progressBar, this, coin_image_size);
    }

    public static void setCoinListImage(final String coin_id, final ImageView imageView,
                                         final ProgressBar progressBar, Activity activity,
                                        final int size) {
        try {

            String IMAGE_URL = coinlore_url + coin_id + dot_png;

            RequestOptions requestOptions = new RequestOptions()
                    .override(size)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .signature(new ObjectKey(coin_id));

            Glide.with(activity)
                    .load(IMAGE_URL)
                    .apply(requestOptions)
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target,
                                                    boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target,
                                                       DataSource dataSource,
                                                       boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);

                            return false;
                        }
                    }).into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void findCoinGraph(View item, int r) {
        LinearLayout item_graph_layout =
                item.findViewById(R.id.item_coin_graph_linear_layout);
        LayoutInflater inflater_coin = getLayoutInflater();
        View item_coin = inflater_coin.inflate(R.layout.item_coin_graph,
                item_graph_layout, false);

        item_graph_layout.addView(item_coin);

        ProgressBar progressBar = item.findViewById(R.id.progressBarGraph);

        GraphView graph = item_coin.findViewById(R.id.graph_item_coin);
        graph.setVisibility(View.INVISIBLE);

        getCoinGraph(graph, progressBar, COIN[r][2], "coin_item_graph");
    }

    private void getCoinGraph(final GraphView graph, final ProgressBar progressBar,
                              final String CoinSymbol, final String period){

        new Thread() {
            @Override
            public void run() {
                final JSONArray json = RemoteFetchGraph.getJSON(CoinSymbol, period);
                if (json != null) {

                    new Thread() {
                        @Override
                        public void run() {
                            onDrawGraph(graph, json);
                        }
                    }.start();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }.start();

    }

    private void onDrawGraph(final GraphView graph, JSONArray json){

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
        series.setThickness(3);
        series.setColor(Color.parseColor("#75a478"));
        graph.addSeries(series);

        graph.setTitle("7D");
        graph.setTitleTextSize(25);

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        graph.getViewport().setXAxisBoundsManual(true);

        try {
            graph.getViewport().setMinX(dp[0].getX());
            graph.getViewport().setMaxX(dp[i - 1].getX());
        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(() -> graph.setVisibility(View.VISIBLE));
    }

    private void onUpdate() {

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
                        && scrollView.getHeight() > 0 && end_of_list && start < 2100) {
                    if (update == 1) {
                        update ++;
                    } else if (update > 1) {
                        end_of_list = false;
                        Toast.makeText(CoinActivity.this,getString(R.string.loading),
                                Toast.LENGTH_SHORT).show();
                        onAddMoreCoins();
                    }
                }
                handler.postDelayed(this, 500);
            }
        });
    }

    private void showCurrentTime() {
        TextView updateTime = findViewById(R.id.updateTime);
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        time = ft.format(date);
        updateTime.setText(time);
    }

    private void setCoinItemInList(String[] ARRAY_COIN, int i, boolean end) {
        LinearLayout MainContainer = findViewById(R.id.MainContainer);
        LayoutInflater inflater = getLayoutInflater();

        final View item = inflater.inflate(R.layout.item_coin, MainContainer, false);

        restoreCoinArray(i, ARRAY_COIN);

        TextView tvMarketNumber = item.findViewById(R.id.tvMarketNumber);
        TextView tvCoin = item.findViewById(R.id.tvCoin);
        TextView tvCoinName = item.findViewById(R.id.tvCoinName);
        TextView tvCoinPrice = item.findViewById(R.id.tvCoinPrice);
        TextView tvPercentOneHourData = item.findViewById(R.id.tvPercentOneHourData);
        TextView tvPercentTwentyFourHourData = item.findViewById(R.id.tvPercentTwentyFourHourData);
        TextView tvPercentSevenDaysData = item.findViewById(R.id.tvPercentSevenDaysData);
        TextView tvMarketCapData = item.findViewById(R.id.tvMarketCapData);
        TextView tvVolume24Data = item.findViewById(R.id.tvVolume24Data);

        tvCoinName.setText(ARRAY_COIN[1]);
        tvCoin.setText(ARRAY_COIN[2]);
        tvMarketNumber.setText(ARRAY_COIN[3]);
        tvCoinPrice.setText(ARRAY_COIN[4]);

        String p1h, p24h, p7d;
        p1h = ARRAY_COIN[6] + percent_symbol;
        p24h = ARRAY_COIN[7] + percent_symbol;
        p7d = ARRAY_COIN[8] + percent_symbol;

        tvPercentOneHourData.setText(p1h);
        formatPercentValuesRestored(6, ARRAY_COIN, tvPercentOneHourData);

        tvPercentTwentyFourHourData.setText(p24h);
        formatPercentValuesRestored(7, ARRAY_COIN, tvPercentTwentyFourHourData);

        tvPercentSevenDaysData.setText(p7d);
        formatPercentValuesRestored(8, ARRAY_COIN, tvPercentSevenDaysData);

        tvMarketCapData.setText(ARRAY_COIN[9]);
        tvVolume24Data.setText(ARRAY_COIN[10]);

        CoinId = ARRAY_COIN[13];

        findCoinImage(item);
        findCoinGraph(item, i);

        item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item.setTag(CoinId);
        MainContainer.addView(item);

        item.setOnClickListener(itemOnClickListener);

        if (end) {
            end_of_list = false;
            showMoreCoins();
            contentIsShown = true;
        }
    }

    private void formatPercentValuesRestored(int r, String[] ARRAY_COIN, TextView textView) {
        try {
            if (!ARRAY_COIN[r].equals("") && !ARRAY_COIN[r].equals("null")) {
                if (Float.parseFloat(ARRAY_COIN[r].substring(0, ARRAY_COIN[r].length() - 1)
                        .replaceAll(percent_symbol, "")) > 0) {
                    textView.setTextColor(colorGreen);
                } else if (Float.parseFloat(ARRAY_COIN[r].substring(0, ARRAY_COIN[r].length() - 1)
                        .replaceAll(percent_symbol, "")) < 0) {
                    textView.setTextColor(colorRed);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreCoinArray(int i, String[] ARRAY_COIN) {
        System.arraycopy(ARRAY_COIN, 1, COIN[i], 1, 14);
    }

    private View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String COIN_ID = String.valueOf(v.getTag());
            for (String[] strings : COIN) {
                if (COIN_ID.equals(strings[13])) {
                    Intent intent = new Intent(CoinActivity.this, CoinInfoActivity.class);
                    intent.putExtra("COIN_ID", COIN_ID);
                    intent.putExtra("COIN_NAME", strings[1]);
                    intent.putExtra("COIN_SYMBOL", strings[2]);
                    intent.putExtra("COIN_RANK", strings[3]);
                    intent.putExtra("COIN_PRICE", strings[4]);
                    intent.putExtra("COIN_PRICEBTC", strings[5]);
                    intent.putExtra("COIN_P1H", strings[6]);
                    intent.putExtra("COIN_P24H", strings[7]);
                    intent.putExtra("COIN_P7D", strings[8]);
                    intent.putExtra("COIN_MCAP", strings[9]);
                    intent.putExtra("COIN_V24H", strings[10]);
                    intent.putExtra("COIN_CS", strings[11].substring(0, strings[11].length() - 3));
                    intent.putExtra("COIN_MS", strings[12]);
                    intent.putExtra("COIN_USAGE", strings[14]);
                    startActivity(intent);
                }
            }
        }
    };

}