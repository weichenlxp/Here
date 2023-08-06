package edu.hebut.here;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.king.view.splitedittext.SplitEditText;

public class LoginPasswordActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID;
    String password;
    SplitEditText splitEditText;
    TextView step, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        password = sharedPreferences.getString("password", null);
        editor = sharedPreferences.edit();
        getSupportActionBar().setTitle("验证密码");
        setContentView(R.layout.activity_password);
        step = findViewById(R.id.step);
        label = findViewById(R.id.label_set_password);
        step.setVisibility(View.INVISIBLE);
        label.setText("请输入密码");
        splitEditText = findViewById(R.id.set_password);
        splitEditText.setOnTextInputListener(new SplitEditText.OnTextInputListener() {
            @Override
            public void onTextInputChanged(String text, int length) {
                //TODO 文本输入改变
            }

            @Override
            public void onTextInputCompleted(String text) {
                String loginPassword = String.valueOf(splitEditText.getText());
                if (sharedPreferences.getInt("chance", 5) <= 0) {
                    finish();
                } else {
                    if (!password.equals(loginPassword)) {
                        editor.putInt("chance", sharedPreferences.getInt("chance", 5) - 1);
                        editor.apply();
                        label.setText("密码错误！您还有" + sharedPreferences.getInt("chance", 5) + "次机会");
                        label.setTextColor(Color.RED);
                    } else {
                        editor.putInt("chance", 5);
                        editor.apply();
                        Intent intent = new Intent(LoginPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginPasswordActivity.this.finish();
                    }
                }

            }
        });
        if (sharedPreferences.getInt("chance", 5) <= 0) {
            label.setText("密码错误次数已达上限，请到系统设置中清除本应用所有数据后再使用！");
            splitEditText.setEnabled(false);
        }
    }
}
