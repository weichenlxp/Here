package edu.hebut.here.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import edu.hebut.here.activity.LoginActivity;
import edu.hebut.here.entity.LoginUser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.hebut.here.activity.AppContext.httpURL;
import static edu.hebut.here.activity.AppContext.loginMsg;

public class HttpUtils {
    public static void check(Context context, String username, String password, String servlet) {
        ProgressDialog progressDialog = new ProgressDialog(context);
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
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
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

    public static void sendMessage(String result, int what) {
        Message message=Message.obtain();
        message.what= what;
        message.obj=result;
        handler.sendMessage(message);
    }
}
