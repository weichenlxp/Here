package edu.hebut.here.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hebut.here.R;
import edu.hebut.here.adapter.SettingFurnitureAdapter;

import static edu.hebut.here.data.MyContentResolver.createFurniture;
import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class FurnitureActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int userID, houseID, roomID;
    String[] houseNameList;
    String houseName;
    String[] roomNameList;
    String roomName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_furniture);
        fab.setOnClickListener(v -> {
            ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_furniture, null);
            Spinner house = dialogLayout.findViewById(R.id.house);
            Spinner room = dialogLayout.findViewById(R.id.room);
            Cursor houseCursorTemp = queryHouse(getApplicationContext(), new String[]{"houseID", "houseName"}, "userID=?", new String[]{String.valueOf(userID)});
            String[] tempHouseName = new String[houseCursorTemp.getCount()];
            int[] tempHouseID = new int[houseCursorTemp.getCount()];
            for (int i = 0; houseCursorTemp.moveToNext(); i++) {
                tempHouseID[i] = houseCursorTemp.getInt(0);
                tempHouseName[i] = houseCursorTemp.getString(1);
            }
            houseNameList = tempHouseName;
            ArrayAdapter<String> houseNameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, houseNameList);
            houseNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            house.setAdapter(houseNameAdapter);
            house.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    houseName = houseNameList[pos];
                    houseID = tempHouseID[pos];

                    Cursor roomCursorTemp = queryRoom(getApplicationContext(), new String[]{"roomID", "roomName"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
                    String[] tempRoomName = new String[roomCursorTemp.getCount()];
                    int[] tempRoomID = new int[roomCursorTemp.getCount()];
                    for (int i = 0; roomCursorTemp.moveToNext(); i++) {
                        tempRoomID[i] = roomCursorTemp.getInt(0);
                        tempRoomName[i] = roomCursorTemp.getString(1);
                    }
                    roomNameList = tempRoomName;
                    ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, roomNameList);
                    roomNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    room.setAdapter(roomNameAdapter);
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

            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建住所").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_furniture);
                String temp = editText.getText().toString();
                Cursor cursor = queryFurniture(getApplicationContext(), new String[]{"furnitureID"}, "furnitureName=? AND userID=? AND houseID=? AND roomID=?", new String[]{temp, String.valueOf(userID), String.valueOf(houseID), String.valueOf(roomID)});
                if (cursor.getCount() == 0) {
                    createFurniture(getApplicationContext(), temp, userID, houseID, roomID);
                    recyclerView.setAdapter(new SettingFurnitureAdapter(this, this));
                    Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "创建失败，住所不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        recyclerView = findViewById(R.id.add_furniture_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        recyclerView.setAdapter(new SettingFurnitureAdapter(this, this));
        //设置列表中子项的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SettingFurnitureAdapter(this, this));
    }
}