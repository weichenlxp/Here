package edu.hebut.here.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.ui.setting.RoomActivity;
import edu.hebut.here.utils.WindowUtils;

import static edu.hebut.here.data.MyContentResolver.deleteRoom;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;
import static edu.hebut.here.data.MyContentResolver.updateRoomName;

public class SettingRoomAdapter extends RecyclerView.Adapter<SettingRoomAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    RoomActivity activity;
    int userID, houseID, roomID;
    String[] houseNameList;
    String houseName;
    private Context mContext;//定义上下文
    private LayoutInflater inflater;

    //集合
    private List<RoomEntity> listRoomEntity = new ArrayList<>();

    public SettingRoomAdapter(Context mContext, RoomActivity activity) {
        this.mContext = mContext;
        this.activity = activity;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        Cursor roomCursor = queryRoom(this.mContext, new String[]{"roomName", "houseID"}, "userID=?", new String[]{String.valueOf(userID)}, "houseID ASC");
        String[] temp1 = new String[roomCursor.getCount()];
        int[] tempt2 = new int[roomCursor.getCount()];
        String[] temp2 = new String[roomCursor.getCount()];
        for (int i = 0; roomCursor.moveToNext(); i++) {
            temp1[i] = roomCursor.getString(0);
            tempt2[i] = roomCursor.getInt(1);
        }
        for (int i = 0; i < tempt2.length; i++) {
            Cursor cursor1 = queryHouse(mContext, new String[]{"houseName"}, "houseID=?", new String[]{String.valueOf(tempt2[i])});
            while (cursor1.moveToNext()) {
                temp2[i] = cursor1.getString(0);
            }
        }
        for (int i = 1; i < temp1.length; i++) {
            RoomEntity roomEntity = new RoomEntity(temp1[i], tempt2[i], temp2[i]);
            listRoomEntity.add(roomEntity);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting_room, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.roomName.setText(listRoomEntity.get(position).roomName);
        holder.houseName.setText(listRoomEntity.get(position).houseName);
        holder.layout_content0.setOnClickListener(v -> {
            Cursor roomTemp = queryRoom(mContext, new String[]{"roomID"}, "roomName=? AND userID=?", new String[]{listRoomEntity.get(position).roomName, String.valueOf(userID)}, null);
            while (roomTemp.moveToNext()) {
                roomID = roomTemp.getInt(0);
            }
            inflater = LayoutInflater.from(mContext);
            ConstraintLayout dialogLayout = (ConstraintLayout) inflater.inflate(R.layout.dialog_edit_room, null);
            Spinner house = dialogLayout.findViewById(R.id.house);

            Cursor houseCursorTemp = queryHouse(mContext, new String[]{"houseID", "houseName"}, "userID=?", new String[]{String.valueOf(userID)});
            String[] tempHouseName = new String[houseCursorTemp.getCount()];
            int[] tempHouseID = new int[houseCursorTemp.getCount()];
            for (int i = 0; houseCursorTemp.moveToNext(); i++) {
                tempHouseID[i] = houseCursorTemp.getInt(0);
                tempHouseName[i] = houseCursorTemp.getString(1);
            }
            houseNameList = tempHouseName;
            ArrayAdapter<String> houseNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, houseNameList);
            houseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            house.setAdapter(houseNameAdapter);
            house.setSelection(houseNameAdapter.getPosition(listRoomEntity.get(position).houseName), true);
            houseID = tempHouseID[houseNameAdapter.getPosition(listRoomEntity.get(position).houseName)];
            houseName = tempHouseName[houseNameAdapter.getPosition(listRoomEntity.get(position).houseName)];
            house.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    houseName = houseNameList[pos];
                    houseID = tempHouseID[pos];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
            new android.app.AlertDialog.Builder(mContext).setIcon(R.drawable.ic_edit).setTitle("编辑房间").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_room);
                String temp = editText.getText().toString();
                Cursor cursor = queryRoom(mContext, new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{temp, String.valueOf(houseID)}, null);
                if (cursor.getCount() == 0) {
                    updateRoomName(mContext, temp, houseID, roomID);
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.mContext, "修改失败，房间不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });
        //设置内容宽度为屏幕的宽度
        holder.layout_content0.getLayoutParams().width = WindowUtils.getScreenWidth(mContext) / 2;
        holder.layout_content1.getLayoutParams().width = WindowUtils.getScreenWidth(mContext) / 2;

        //删除按钮的方法
        holder.btn_delete.setOnClickListener(v -> {
            if (getItemCount() > 1) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this.mContext);
                alterDialog.setIcon(R.drawable.ic_close_overdue);//图标
                alterDialog.setTitle("确认删除");//文字
                alterDialog.setMessage("删除后，与该房间所关联的账号、房间、家具以及物品都会被删除！");
                alterDialog.setPositiveButton("确认", (dialog, which) -> {
                    deleteRoom(mContext, "roomName=? AND userID=?", new String[]{listRoomEntity.get(position).roomName, String.valueOf(userID)});
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                });
                alterDialog.setNegativeButton("取消", (dialog, which) -> {
                });
                alterDialog.show();
            } else {
                Toast.makeText(this.mContext, "删除失败，需至少保留一个房间", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listRoomEntity.size();
    }

    public static class RoomEntity {
        String roomName;
        int houseID;
        String houseName;

        public RoomEntity(String roomName, int houseID, String houseName) {
            this.roomName = roomName;
            this.houseID = houseID;
            this.houseName = houseName;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getHouseID() {
            return houseID;
        }

        public void setHouseID(int houseID) {
            this.houseID = houseID;
        }

        public String getHouseName() {
            return houseName;
        }

        public void setHouseName(String houseName) {
            this.houseName = houseName;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_delete;
        public TextView roomName;//名字
        public TextView houseName;//名字
        public ViewGroup layout_content0;//图标与信息布局
        public ViewGroup layout_content1;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            houseName = itemView.findViewById(R.id.houseName);
            layout_content0 = itemView.findViewById(R.id.layout_content0);
            layout_content1 = itemView.findViewById(R.id.layout_content1);
            btn_delete = itemView.findViewById(R.id.delete_button);
        }
    }
}
