package edu.hebut.here.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.hebut.here.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity extends AppCompatActivity {
    public static final int REGS=3;//注册成功标识码
    public static final int REGN=4;//注册失败标识码
    TextView idnumber;
    TextView pwdnumber;
    Button regerest;
    OkHttpClient client = new OkHttpClient();
    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REGS:
                    Toast.makeText(HttpActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    break;
                case REGN:
                    Toast.makeText(HttpActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        initall();
    }

    private void initall() {
        Log.e("initall", "start");
        idnumber=findViewById(R.id.idnumber);
        pwdnumber=findViewById(R.id.pwdnumber);
        regerest=findViewById(R.id.regerest);

        regerest.setOnClickListener(view -> {
            getData();
            Log.e("initall", "end");
        });
    }
    public void getData(){
        Log.e("getData", "start");
        final String url="http://192.168.0.100:8080/HereService_Web_exploded/MyFirstServlet";
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String path=url+"?logname="+idnumber.getText()+"&password="+pwdnumber.getText();
                    Log.e("path", path);
                    String result=get(path);
                    Log.e("result", result);
                    if (result.equals("ttt")){
                        Message message=Message.obtain();
                        message.what= REGN;
                       // message.obj=result;

                        handler.sendMessage(message);
                    }
                    else {
                        Message message=Message.obtain();
                        message.what= REGS;
                        message.obj=result;

                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Log.e("response", response.toString());
            return response.body().string();
        }
    }


}
