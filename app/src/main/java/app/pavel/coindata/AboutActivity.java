package app.pavel.coindata;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onCopyAddress(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        switch (view.getId()) {
            case R.id.BTC_COPY:
                ClipData clip1 = ClipData.newPlainText("label", getResources().getString(R.string.BTC_Address));
                clipboardManager.setPrimaryClip(clip1);
                Toast.makeText(this, "BTC address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ETH_COPY:
                ClipData clip2 = ClipData.newPlainText("label", getResources().getString(R.string.ETH_Address));
                clipboardManager.setPrimaryClip(clip2);
                Toast.makeText(this, "ETH address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.USDT_COPY:
                ClipData clip3 = ClipData.newPlainText("label", getResources().getString(R.string.USDT_Address));
                clipboardManager.setPrimaryClip(clip3);
                Toast.makeText(this, "USDT address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.LTC_COPY:
                ClipData clip4 = ClipData.newPlainText("label", getResources().getString(R.string.LTC_Address));
                clipboardManager.setPrimaryClip(clip4);
                Toast.makeText(this, "LTC address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.DASH_COPY:
                ClipData clip5 = ClipData.newPlainText("label", getResources().getString(R.string.DASH_Address));
                clipboardManager.setPrimaryClip(clip5);
                Toast.makeText(this, "DASH address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.NEO_COPY:
                ClipData clip6 = ClipData.newPlainText("label", getResources().getString(R.string.NEO_Address));
                clipboardManager.setPrimaryClip(clip6);
                Toast.makeText(this, "NEO address successfully copied",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ZCASH_COPY:
                ClipData clip7 = ClipData.newPlainText("label", getResources().getString(R.string.ZCASH_Address));
                clipboardManager.setPrimaryClip(clip7);
                Toast.makeText(this, "ZEC address successfully copied",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
