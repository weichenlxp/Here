package edu.hebut.here.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.ui.setting.HouseActivity;
import edu.hebut.here.utils.WindowUtils;

import static edu.hebut.here.data.MyContentResolver.deleteHouse;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.updateHouseName;

public class SettingHouseAdapter extends RecyclerView.Adapter<SettingHouseAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    HouseActivity activity;
    int userID, houseID;
    String[] houseName;
    private Context mContext;//定义上下文
    private LayoutInflater inflater;

    //集合
    private List<String> listHouse = new ArrayList<>();

    public SettingHouseAdapter(Context mContext, HouseActivity activity) {
        this.mContext = mContext;
        this.activity = activity;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting_house, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.houseName.setText(listHouse.get(position));
        holder.layout_content.setOnClickListener(v -> {
            inflater = LayoutInflater.from(mContext);
            ConstraintLayout dialogLayout = (ConstraintLayout) inflater.inflate(R.layout.dialog_edit_account, null);

            new android.app.AlertDialog.Builder(mContext).setIcon(R.drawable.ic_edit).setTitle("编辑住所").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_account);
                String temp = editText.getText().toString();
                Cursor cursor = queryHouse(mContext, new String[]{"houseID"}, "houseName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount() == 0) {
                    updateHouseName(mContext, temp, listHouse.get(position), userID);
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.mContext, "修改失败，住所不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });
        //设置内容宽度为屏幕的宽度
        holder.layout_content.getLayoutParams().width = WindowUtils.getScreenWidth(mContext);

        //删除按钮的方法
        holder.btn_delete.setOnClickListener(v -> {
            int houseIDCheck = -1;
            Cursor houseCursor = queryHouse(this.mContext, new String[]{"houseID"}, "userID=? AND houseName=?", new String[]{String.valueOf(userID), listHouse.get(position)});
            while (houseCursor.moveToNext()) {
                houseIDCheck = houseCursor.getInt(0);
            }
            if (getItemCount() > 1 && houseID!=houseIDCheck) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this.mContext);
                alterDialog.setIcon(R.drawable.ic_close_overdue);//图标
                alterDialog.setTitle("确认删除");//文字
                alterDialog.setMessage("删除后，与该住所所关联的账号、房间、家具以及物品都会被删除！");
                alterDialog.setPositiveButton("确认", (dialog, which) -> {
                    int n = holder.getLayoutPosition();//获取要删除行的位置
                    deleteHouse(mContext, "houseName=? AND userID=?", new String[]{houseName[n], String.valueOf(userID)});
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                });
                alterDialog.setNegativeButton("取消", (dialog, which) -> {
                });
                alterDialog.show();
            } else {
                if (houseID==houseIDCheck){
                    Toast.makeText(this.mContext, "删除失败，不能删除当前住所，请切换到其他住所再删除", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this.mContext, "删除失败，需至少保留一个住所", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listHouse.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_delete;
        public TextView houseName;//名字
        public ViewGroup layout_content;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            houseName = itemView.findViewById(R.id.houseName);
            layout_content = itemView.findViewById(R.id.layout_content);
            btn_delete = itemView.findViewById(R.id.delete_button);
        }
    }
}
