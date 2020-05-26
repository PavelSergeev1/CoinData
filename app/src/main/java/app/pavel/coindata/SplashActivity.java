package app.pavel.coindata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    public static final String THEME = "pref_theme";

    public static String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentStart = getIntent();

        boolean isFirstLaunch = intentStart.getBooleanExtra("first_run", false);

        if (isFirstLaunch) {
            //currentTheme = "dark";
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        }
        else {
            Log.i("Is first launch? - ", String.valueOf(isFirstLaunch));
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            currentTheme = prefs.getString(THEME, null);
            if (currentTheme.equals("dark")) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
        finish();
    }
}
