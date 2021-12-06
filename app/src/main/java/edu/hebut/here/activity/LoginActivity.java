package edu.hebut.here.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import edu.hebut.here.R;
import edu.hebut.here.entity.LoginUser;
import edu.hebut.here.utils.JudgeUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.hebut.here.activity.AppContext.*;

public class LoginActivity extends AppCompatActivity {
    String servlet = "LoginServlet";
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

    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            LoginUser userRes = JSON.parseObject(String.valueOf(msg.obj), LoginUser.class);
            if (msg.what == loginMsg) {
                if (userRes.getUserID()==0){
                    tipLoginUsername.setText("用户名错误或密码错误");
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    editor.putBoolean("main", true);
                    editor.putInt("userID", userRes.getUserID());
                    editor.putInt("houseID", userRes.getHouseID());
                    editor.apply();
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在加载布局文件前判断是否登录过
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String passwordSP = sharedPreferences.getString("password", null);
        if (sharedPreferences.getBoolean("main", false)) {
            Intent intent;
            if (passwordSP != null) {
                intent = new Intent(LoginActivity.this, LoginPasswordActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, MainActivity.class);
            }
            startActivity(intent);
            LoginActivity.this.finish();
        }
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        tipLoginUsername = findViewById(R.id.tip_login_username);
        tipLoginPassword = findViewById(R.id.tip_login_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(v -> {
            username = loginUsername.getText().toString();
            password = loginPassword.getText().toString();
            if (!JudgeUtils.judgeUsername(username)) {
                tipLoginUsername.setText("用户名格式错误");
            }
            else if (!JudgeUtils.judgePassword(password)) {
                tipLoginPassword.setText("密码格式错误");
            }
            else {
                check();
            }
        });

        signin = findViewById(R.id.btn_signin);
        signin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        });
    }
    public void check() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        //设置进度条风格，风格为圆形，旋转的
        progressDialog.setProgressStyle(
                ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 标题
        progressDialog.setTitle("请稍后");
        //设置ProgressDialog 提示信息
        progressDialog.setMessage("这是一个圆形进度条对话框");
        //设置ProgressDialog 标题图标
        progressDialog.setIcon(android.R.drawable.btn_star);
        //设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 让ProgressDialog显示
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        LoginUser user = new LoginUser();
        user.setUsername(username);
        user.setPassword(password);
        //将对象转换为json字符串
        String json = JSON.toJSONString(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servlet)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        progressDialog.dismiss();
                        sendMessage(res, loginMsg);
                    }
                }.start();
            }
        });
    }

    public void sendMessage(String result, int what) {
        Message message=Message.obtain();
        message.what= what;
        message.obj=result;
        handler.sendMessage(message);
    }
}
