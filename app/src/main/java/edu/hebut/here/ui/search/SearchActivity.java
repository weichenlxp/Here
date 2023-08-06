package edu.hebut.here.ui.search;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.hebut.here.R;
import edu.hebut.here.adapter.SearchGoodsAdapter;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    Switch aSwitch;
    RecyclerView recyclerView;
    String temp;
    boolean isAll = false;
    Context context;
    boolean first = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        recyclerView = findViewById(R.id.goodsRV);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        aSwitch = findViewById(R.id.isAll);
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isAll = true;
                aSwitch.setText("所有住所");
                recyclerView.setAdapter(new SearchGoodsAdapter(context, SearchActivity.this, temp, isAll));
            }
            else {
                isAll = false;
                aSwitch.setText("当前住所");
                recyclerView.setAdapter(new SearchGoodsAdapter(context, SearchActivity.this, temp, isAll));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.e("onQueryTextChange","start");
                temp = queryText;
                recyclerView.setAdapter(new SearchGoodsAdapter(context, SearchActivity.this, temp, isAll));
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                if (searchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法                    }
                        searchView.clearFocus(); // 不获取焦点                }
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return false;
            }
        });
    }
    @Override
    protected void onResume() {
        if (first) {
            super.onResume();
            first = false;
        }
        else {
            recyclerView.setAdapter(new SearchGoodsAdapter(context, this, temp, true));
            super.onResume();
        }
    }

    public void reSetAdapter() {
        recyclerView.setAdapter(new SearchGoodsAdapter(context, this, temp, true));
    }
}