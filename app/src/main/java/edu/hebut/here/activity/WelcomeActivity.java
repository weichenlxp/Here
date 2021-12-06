package edu.hebut.here.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import edu.hebut.here.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        Intent service = new Intent(this, LongRunningService.class);
//        startService(service);

        /**
         * 延迟3秒进入主界面
         */
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }, 1500);
    }
}

