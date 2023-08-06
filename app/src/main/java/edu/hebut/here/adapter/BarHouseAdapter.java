package edu.hebut.here.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.WelcomeActivity;

import static edu.hebut.here.data.MyContentResolver.queryHouse;

public class BarHouseAdapter extends RecyclerView.Adapter<BarHouseAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID;
    String[] houseName;
    private Context mContext;//定义上下文
    private Activity activity;
    //集合
    private List<String> listHouse = new ArrayList<>();

    public BarHouseAdapter(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        Cursor houseCursor = queryHouse(this.mContext, new String[]{"houseName"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] temp = new String[houseCursor.getCount()];
        for (int i = 0; houseCursor.moveToNext(); i++) {
            temp[i] = houseCursor.getString(0);
        }
        houseName = temp;
        //设置菜单行数与行内图标、名称、信息
        Collections.addAll(listHouse, houseName);
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
        holder.houseName.setText(listHouse.get(position));
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
}
