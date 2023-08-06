package edu.hebut.here.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hebut.here.R;
import edu.hebut.here.adapter.PackGoodsAdapter;
import edu.hebut.here.adapter.SettingHouseAdapter;

import static edu.hebut.here.data.MyContentResolver.createAccount;
import static edu.hebut.here.data.MyContentResolver.createFurniture;
import static edu.hebut.here.data.MyContentResolver.createHouse;
import static edu.hebut.here.data.MyContentResolver.createRoom;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class HouseActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int userID, houseID;
    RecyclerView recyclerView;
    String[] accountName = {"宽带", "电表", "水表", "燃气", "有线电视", "加油卡"};
    String[] roomName = {"客厅", "主卧", "次卧", "厨房", "卫生间"};
    String[] furnitureName1 = {"茶几", "电视柜"};
    String[] furnitureName2 = {"床头柜", "衣柜"};
    String[] furnitureName3 = {"床头柜", "衣柜"};
    String[] furnitureName4 = {"橱柜1", "橱柜2"};
    String[] furnitureName5 = {"洗手台", "置物架"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_house);
        fab.setOnClickListener(v -> {
            ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_house, null);
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建住所").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_house);
                String temp = editText.getText().toString();
                Cursor cursor = queryHouse(this, new String[]{"houseID"}, "houseName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount() == 0) {
                    createHouse(getApplicationContext(), temp, userID);
                    recyclerView.setAdapter(new SettingHouseAdapter(this, this));
                    cursor = queryHouse(this, new String[]{"houseID"}, "houseName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                    while (cursor.moveToNext()) {
                        houseID = cursor.getInt(0);
                    }
                    for (String account : accountName) {
                        createAccount(getApplicationContext(), account, houseID);
                    }

                    for (String room : roomName) {
                        createRoom(getApplicationContext(), room, houseID, userID);
                    }
                    //建家具
                    Cursor roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"客厅", String.valueOf(houseID)}, null);
                    int roomID = -1;
                    while (roomCursor.moveToNext()) {
                        roomID = roomCursor.getInt(0);
                    }

                    for (String furniture : furnitureName1) {
                        createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                    }
                    roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"主卧", String.valueOf(houseID)}, null);
                    while (roomCursor.moveToNext()) {
                        roomID = roomCursor.getInt(0);
                    }
                    for (String furniture : furnitureName2) {
                        createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                    }
                    roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"次卧", String.valueOf(houseID)}, null);
                    while (roomCursor.moveToNext()) {
                        roomID = roomCursor.getInt(0);
                    }
                    for (String furniture : furnitureName3) {
                        createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                    }
                    roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"厨房", String.valueOf(houseID)}, null);
                    while (roomCursor.moveToNext()) {
                        roomID = roomCursor.getInt(0);
                    }
                    for (String furniture : furnitureName4) {
                        createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                    }
                    roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"卫生间", String.valueOf(houseID)}, null);
                    while (roomCursor.moveToNext()) {
                        roomID = roomCursor.getInt(0);
                    }
                    for (String furniture : furnitureName5) {
                        createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                    }
                    Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "创建失败，住所不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        recyclerView = findViewById(R.id.add_house_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        recyclerView.setAdapter(new SettingHouseAdapter(this, this));
        //设置列表中子项的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SettingHouseAdapter(this, this));
    }
}