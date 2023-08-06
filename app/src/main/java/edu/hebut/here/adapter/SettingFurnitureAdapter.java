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
import edu.hebut.here.ui.setting.FurnitureActivity;
import edu.hebut.here.utils.WindowUtils;

import static edu.hebut.here.data.MyContentResolver.deleteFurniture;
import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;
import static edu.hebut.here.data.MyContentResolver.updateFurnitureName;

public class SettingFurnitureAdapter extends RecyclerView.Adapter<SettingFurnitureAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    FurnitureActivity activity;
    int userID, houseID, roomID, furnitureID;
    String[] houseNameList;
    String houseName;
    String[] roomNameList;
    String roomName;
    private Context mContext;//定义上下文
    private LayoutInflater inflater;

    //集合
    private List<FurnitureEntity> listFurnitureEntity = new ArrayList<>();

    public SettingFurnitureAdapter(Context mContext, FurnitureActivity activity) {
        this.mContext = mContext;
        this.activity = activity;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        Cursor furnitureCursor = queryFurniture(this.mContext, new String[]{"furnitureName", "houseID", "roomID"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] temp1 = new String[furnitureCursor.getCount()];
        int[] temp2 = new int[furnitureCursor.getCount()];
        int[] temp3 = new int[furnitureCursor.getCount()];
        String[] tempt2 = new String[furnitureCursor.getCount()];
        String[] tempt3 = new String[furnitureCursor.getCount()];
        for (int i = 0; furnitureCursor.moveToNext(); i++) {
            temp1[i] = furnitureCursor.getString(0);
            temp2[i] = furnitureCursor.getInt(1);
            temp3[i] = furnitureCursor.getInt(2);
        }
        for (int i = 0; i < temp2.length; i++) {
            Cursor cursor1 = queryHouse(mContext, new String[]{"houseName"}, "houseID=?", new String[]{String.valueOf(temp2[i])});
            Cursor cursor2 = queryRoom(mContext, new String[]{"roomName"}, "roomID=?", new String[]{String.valueOf(temp3[i])}, null);
            while (cursor1.moveToNext()) {
                tempt2[i] = cursor1.getString(0);
            }
            while (cursor2.moveToNext()) {
                tempt3[i] = cursor2.getString(0);
            }
        }

        for (int i = 1; i < temp1.length; i++) {
            FurnitureEntity furnitureEntity = new FurnitureEntity(temp1[i], temp3[i], tempt3[i], temp2[i], tempt2[i]);
            listFurnitureEntity.add(furnitureEntity);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting_furniture, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.furnitureName.setText(listFurnitureEntity.get(position).furnitureName);
        holder.houseName.setText(listFurnitureEntity.get(position).houseName);
        holder.roomName.setText(listFurnitureEntity.get(position).roomName);
        holder.layout_content0.setOnClickListener(v -> {
            Cursor furnitureTemp = queryFurniture(mContext, new String[]{"furnitureID"}, "furnitureName=? AND userID=?", new String[]{listFurnitureEntity.get(position).furnitureName, String.valueOf(userID)});
            while (furnitureTemp.moveToNext()) {
                furnitureID = furnitureTemp.getInt(0);
            }
            inflater = LayoutInflater.from(mContext);
            ConstraintLayout dialogLayout = (ConstraintLayout) inflater.inflate(R.layout.dialog_edit_furniture, null);
            Spinner house = dialogLayout.findViewById(R.id.house);
            Spinner room = dialogLayout.findViewById(R.id.room);

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
            house.setSelection(houseNameAdapter.getPosition(listFurnitureEntity.get(position).houseName), true);
            houseID = tempHouseID[houseNameAdapter.getPosition(listFurnitureEntity.get(position).houseName)];
            houseName = tempHouseName[houseNameAdapter.getPosition(listFurnitureEntity.get(position).houseName)];
            Cursor roomCursorTemp = queryRoom(mContext, new String[]{"roomID", "roomName"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
            String[] tempRoomName = new String[roomCursorTemp.getCount()];
            int[] tempRoomID = new int[roomCursorTemp.getCount()];
            for (int i = 0; roomCursorTemp.moveToNext(); i++) {
                tempRoomID[i] = roomCursorTemp.getInt(0);
                tempRoomName[i] = roomCursorTemp.getString(1);
            }
            roomNameList = tempRoomName;
            ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, roomNameList);
            roomNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            room.setAdapter(roomNameAdapter);
            room.setSelection(roomNameAdapter.getPosition(listFurnitureEntity.get(position).roomName));
            roomID = tempRoomID[roomNameAdapter.getPosition(listFurnitureEntity.get(position).roomName)];
            roomName = tempRoomName[roomNameAdapter.getPosition(listFurnitureEntity.get(position).roomName)];
            house.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    houseName = houseNameList[pos];
                    houseID = tempHouseID[pos];

                    Cursor roomCursorTemp = queryRoom(mContext, new String[]{"roomID", "roomName"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
                    String[] tempRoomName = new String[roomCursorTemp.getCount()];
                    int[] tempRoomID = new int[roomCursorTemp.getCount()];
                    for (int i = 0; roomCursorTemp.moveToNext(); i++) {
                        tempRoomID[i] = roomCursorTemp.getInt(0);
                        tempRoomName[i] = roomCursorTemp.getString(1);
                    }
                    roomNameList = tempRoomName;
                    ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, roomNameList);
                    roomNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    room.setAdapter(roomNameAdapter);
                    room.setSelection(roomNameAdapter.getPosition(listFurnitureEntity.get(position).roomName));
                    room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int pos, long id) {
                            roomName = roomNameList[pos];
                            roomID = tempRoomID[pos];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Another interface callback
                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

            new android.app.AlertDialog.Builder(mContext).setIcon(R.drawable.ic_edit).setTitle("编辑住所").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_furniture);
                String temp = editText.getText().toString();
                Cursor cursor = queryHouse(mContext, new String[]{"houseID"}, "houseName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount() == 0) {
                    updateFurnitureName(mContext, temp, houseID, roomID, furnitureID);
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.mContext, "修改失败，住所不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });
        //设置内容宽度为屏幕的宽度
        holder.layout_content0.getLayoutParams().width = WindowUtils.getScreenWidth(mContext) / 3;
        holder.layout_content1.getLayoutParams().width = WindowUtils.getScreenWidth(mContext) / 3;
        holder.layout_content2.getLayoutParams().width = WindowUtils.getScreenWidth(mContext) / 3;

        //删除按钮的方法
        holder.btn_delete.setOnClickListener(v -> {
            if (getItemCount() > 1) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this.mContext);
                alterDialog.setIcon(R.drawable.ic_close_overdue);//图标
                alterDialog.setTitle("确认删除");//文字
                alterDialog.setMessage("删除后，与该住所所关联的账号、房间、家具以及物品都会被删除！");
                alterDialog.setPositiveButton("确认", (dialog, which) -> {
                    int n = holder.getLayoutPosition();//获取要删除行的位置
                    deleteFurniture(mContext, "furnitureName=? AND userID=? AND roomID=? AND houseID=?", new String[]{listFurnitureEntity.get(position).furnitureName, String.valueOf(userID), String.valueOf(listFurnitureEntity.get(position).getRoomID()), String.valueOf(listFurnitureEntity.get(position).getHouseID())});
                    activity.reSetAdapter();
                    Toast.makeText(this.mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                });
                alterDialog.setNegativeButton("取消", (dialog, which) -> {
                });
                alterDialog.show();
            } else {
                Toast.makeText(this.mContext, "删除失败，需至少保留一个家具", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listFurnitureEntity.size();
    }

    public static class FurnitureEntity {
        String furnitureName;
        int roomID;
        String roomName;
        int houseID;
        String houseName;

        public FurnitureEntity(String furnitureName, int roomID, String roomName, int houseID, String houseName) {
            this.furnitureName = furnitureName;
            this.roomID = roomID;
            this.roomName = roomName;
            this.houseID = houseID;
            this.houseName = houseName;
        }

        public String getFurnitureName() {
            return furnitureName;
        }

        public void setFurnitureName(String furnitureName) {
            this.furnitureName = furnitureName;
        }

        public int getRoomID() {
            return roomID;
        }

        public void setRoomID(int roomID) {
            this.roomID = roomID;
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
        public TextView furnitureName;//名字
        public TextView houseName;
        public TextView roomName;
        public ViewGroup layout_content0;//图标与信息布局
        public ViewGroup layout_content1;//图标与信息布局
        public ViewGroup layout_content2;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            furnitureName = itemView.findViewById(R.id.furnitureName);
            houseName = itemView.findViewById(R.id.houseName);
            roomName = itemView.findViewById(R.id.roomName);
            layout_content0 = itemView.findViewById(R.id.layout_content0);
            layout_content1 = itemView.findViewById(R.id.layout_content1);
            layout_content2 = itemView.findViewById(R.id.layout_content2);
            btn_delete = itemView.findViewById(R.id.delete_button);
        }
    }
}
