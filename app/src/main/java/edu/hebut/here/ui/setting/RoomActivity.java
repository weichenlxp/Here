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
import edu.hebut.here.adapter.SettingRoomAdapter;

import static edu.hebut.here.data.MyContentResolver.createRoom;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class RoomActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int houseID, userID;
    String[] houseNameList;
    String houseName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_room);
        fab.setOnClickListener(v -> {
            ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_room, null);
            Spinner house = dialogLayout.findViewById(R.id.house);
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
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建房间").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_room);
                String temp = editText.getText().toString();
                Cursor cursor = queryRoom(this, new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{temp, String.valueOf(houseID)}, null);
                if (cursor.getCount() == 0) {
                    createRoom(getApplicationContext(), temp, houseID, userID);
                    recyclerView.setAdapter(new SettingRoomAdapter(this, this));
                    Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "创建失败，房间不允许重名", Toast.LENGTH_SHORT).show();
                }
            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        recyclerView = findViewById(R.id.add_room_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        recyclerView.setAdapter(new SettingRoomAdapter(this, this));
        //设置列表中子项的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SettingRoomAdapter(this, this));
    }
}