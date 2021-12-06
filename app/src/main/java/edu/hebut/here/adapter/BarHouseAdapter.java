package edu.hebut.here.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.activity.WelcomeActivity;
import edu.hebut.here.entity.House;
import edu.hebut.here.entity.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.hebut.here.activity.AppContext.httpURL;
import static edu.hebut.here.data.MyContentResolver.queryHouse;

public class BarHouseAdapter extends RecyclerView.Adapter<BarHouseAdapter.MyViewHolder> {
    String servletBarHouseList = "HousePopupWindowServlet";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID;
    String[] houseName;
    private Context mContext;//定义上下文
    private Activity activity;
    //集合
    private List<House> listHouse = new ArrayList<>();

    public BarHouseAdapter(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        initTitleBarHouseName(userID);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bar_house, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.houseName.setText(listHouse.get(position).getHouseName());
        holder.houseName.setOnClickListener(v -> {
            int n = holder.getLayoutPosition();//获取要删除行的位置
            int houseID = -1;
            editor = sharedPreferences.edit();

            Cursor cursor = queryHouse(mContext, new String[]{"houseID"}, "houseName=? AND userID=?", new String[]{houseName[n], String.valueOf(userID)});
            while (cursor.moveToNext()) {
                houseID = cursor.getInt(0);
            }
            editor.putInt("houseID", houseID);
            editor.apply();
            Intent intent = new Intent(mContext, WelcomeActivity.class);
            mContext.startActivity(intent);
            activity.finish();
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listHouse.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView houseName;//名字

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            houseName = itemView.findViewById(R.id.houseName);
        }
    }

    public void initTitleBarHouseName(int userID) {
        OkHttpClient client = new OkHttpClient();
        User user = new User();
        user.setUserID(userID);
        //将对象转换为json字符串
        String json = JSON.toJSONString(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servletBarHouseList)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(activity, "获取住所列表失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("res----", res);
                activity.runOnUiThread(() -> {
                    List<House> houseRes = JSON.parseArray(res, House.class);
                    listHouse = houseRes;
                });
            }
        });
    }
}
