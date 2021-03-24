package edu.hebut.here;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.hebut.here.data.MyContentObserver;
import edu.hebut.here.data.MyContentResolver;
import edu.hebut.here.utils.*;

import static edu.hebut.here.data.MyContentResolver.*;
import static edu.hebut.here.data.MyContentResolver.queryHouseIDByUserID;
import static edu.hebut.here.data.MyContentResolver.queryUserIDByUsername;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView loginUsername;
    TextView loginPassword;
    TextView tipLoginUsername;
    TextView tipLoginPassword;
    Button login;
    TextView signin;
    String username = null;
    String password = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyContentObserver myContentObserver = new MyContentObserver(new Handler(), this);
        this.getContentResolver().registerContentObserver(Uri.parse("content://edu.hebut.here.data/user"), true, myContentObserver);
        //在加载布局文件前判断是否登录过
        sharedPreferences= getSharedPreferences("here", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(sharedPreferences.getBoolean("main",false)){
            Intent intent=new Intent(edu.hebut.here.LoginActivity.this, edu.hebut.here.MainActivity.class);
            startActivity(intent);
            edu.hebut.here.LoginActivity.this.finish();
        }
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        tipLoginUsername = findViewById(R.id.tip_login_username);
        tipLoginPassword = findViewById(R.id.tip_login_password);
        login= (Button) findViewById(R.id.btn_login);
        //这里只是简单用按键模拟登录功能
        login.setOnClickListener(v -> {
            username = loginUsername.getText().toString();
            password = loginPassword.getText().toString();
            if (!JudgeUtils.judgeExist(getApplicationContext(), MyContentResolver.uri_user, new String[]{"_id"}, "username=? and password=?", new String[]{username,password},null)){
                tipLoginUsername.setText("用户名错误或密码错误");
            }
            else {
                Cursor userCursor = queryUserIDByUsername(this, username);
                int userID = -1;
                while (userCursor.moveToNext()) {
                    userID = userCursor.getInt(0);
                }
                Cursor houseCursor = queryHouseIDByUserID(this, userID);
                int houseID = -1;
                if (houseCursor.moveToNext()) {
                    houseID = houseCursor.getInt(0);
                }
                Log.e("LoginHouseID", String.valueOf(houseID));
                Intent intent=new Intent(edu.hebut.here.LoginActivity.this, edu.hebut.here.MainActivity.class);
                editor.putBoolean("main", true);
                editor.putInt("userID", userID);
                editor.putInt("houseID", houseID);
                editor.apply();
                startActivity(intent);
                edu.hebut.here.LoginActivity.this.finish();
            }
        });

        signin = findViewById(R.id.btn_signin);
        signin.setOnClickListener(v -> {
            Intent intent=new Intent(edu.hebut.here.LoginActivity.this, edu.hebut.here.SignActivity.class);
            startActivity(intent);
            edu.hebut.here.LoginActivity.this.finish();
        });
    }
}
