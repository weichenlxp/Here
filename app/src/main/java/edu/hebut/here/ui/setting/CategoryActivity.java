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
import edu.hebut.here.adapter.SettingCategoryAdapter;

import static edu.hebut.here.data.MyContentResolver.createCategory;
import static edu.hebut.here.data.MyContentResolver.queryCategory;

public class CategoryActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int userID;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        FloatingActionButton fab = findViewById(R.id.add_category);
        fab.setOnClickListener(v -> {
            ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_category, null);
            new android.app.AlertDialog.Builder(this).setIcon(R.drawable.ic_edit).setTitle("创建类别").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                EditText editText = dialogLayout.findViewById(R.id.edit_category);
                String temp = editText.getText().toString();
                Cursor cursor = queryCategory(this, new String[]{"categoryID"}, "categoryName=? AND userID=?", new String[]{temp, String.valueOf(userID)});
                if (cursor.getCount() == 0) {
                    createCategory(getApplicationContext(), temp, userID);
                    recyclerView.setAdapter(new SettingCategoryAdapter(this, this));
                    Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "创建失败，类别不允许重名", Toast.LENGTH_SHORT).show();
                }

            })).setNegativeButton("取消", (dialog, which) -> {
            }).create().show();
        });

        //获取列表控件
        recyclerView = findViewById(R.id.add_category_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置适配器
        recyclerView.setAdapter(new SettingCategoryAdapter(this, this));
        //设置列表中子项的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SettingCategoryAdapter(this, this));
    }
}