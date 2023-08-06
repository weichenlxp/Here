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
import edu.hebut.here.adapter.SettingContainerAdapter;

import static edu.hebut.here.data.MyContentResolver.createContainer;
import static edu.hebut.here.data.MyContentResolver.queryContainer;

public class ContainerActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int userID;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_container);
        fab.setOnClickListener(v -> {
            ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_container, null);
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建容器").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_container);
                String temp = editText.getText().toString();
                Cursor cursor = queryContainer(this, new String[]{"containerID"}, "containerName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount() == 0) {
                    createContainer(getApplicationContext(), temp, userID);
                    recyclerView.setAdapter(new SettingContainerAdapter(this, this));
                    Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "创建失败，容器不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        recyclerView = findViewById(R.id.add_container_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        recyclerView.setAdapter(new SettingContainerAdapter(this, this));
        //设置列表中子项的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SettingContainerAdapter(this, this));
    }
}