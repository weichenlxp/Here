package edu.hebut.here.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hebut.here.R;
import edu.hebut.here.data.MyContentResolver;
import edu.hebut.here.utils.AddRoomAdapter;

public class AddRoomActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int houseID;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        houseID = sharedPreferences.getInt("houseID", -1);
        FloatingActionButton fab = findViewById(R.id.add_room);
        fab.setOnClickListener(v -> {
            TableLayout dialogLayout = (TableLayout) getLayoutInflater().inflate(R.layout.dialog_edit_account, null);
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建房间").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_account);
                String temp = editText.getText().toString();
                Cursor cursor = MyContentResolver.queryRoomIDByRoomNameHouseID(this, temp, houseID);
                if (cursor.getCount()==0){
                    MyContentResolver.createRoom(getApplicationContext(),temp, houseID);
                    mRecyclerView.setAdapter(new AddRoomAdapter(this));
                    Toast.makeText(this,"创建成功！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"创建失败，房间不允许重名",Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        mRecyclerView = findViewById(R.id.add_room_list);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        mRecyclerView.setAdapter(new AddRoomAdapter(this));
        //设置列表中子项的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}