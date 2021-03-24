package edu.hebut.here;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        /**
         * 延迟3秒进入主界面
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(edu.hebut.here.WelcomeActivity.this, edu.hebut.here.LoginActivity.class);
                startActivity(intent);
                edu.hebut.here.WelcomeActivity.this.finish();
            }
        }, 1500);
    }
}

