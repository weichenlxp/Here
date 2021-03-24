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
import edu.hebut.here.utils.AddHouseAdapter;

public class AddFurnitureActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int userID;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_house);
        fab.setOnClickListener(v -> {
            TableLayout dialogLayout = (TableLayout) getLayoutInflater().inflate(R.layout.dialog_edit_account, null);
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建住所").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_account);
                String temp = editText.getText().toString();
                Cursor cursor = MyContentResolver.queryHouseIDByHouseNameUserID(this, temp, userID);
                if (cursor.getCount()==0){
                    MyContentResolver.createHouse(getApplicationContext(),temp, userID);
                    mRecyclerView.setAdapter(new AddHouseAdapter(this));
                    Toast.makeText(this,"创建成功！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"创建失败，住所不允许重名",Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        mRecyclerView = findViewById(R.id.add_house_list);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        mRecyclerView.setAdapter(new AddHouseAdapter(this));
        //设置列表中子项的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}