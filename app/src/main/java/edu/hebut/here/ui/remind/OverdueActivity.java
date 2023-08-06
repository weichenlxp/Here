package edu.hebut.here.ui.remind;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.hebut.here.R;
import edu.hebut.here.adapter.RemindGoodsAdapter;
import edu.hebut.here.adapter.SpaceGoodsAdapter;

import static edu.hebut.here.data.MyContentResolver.queryGoods;

public class OverdueActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String isOverdue;
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals("overdue")) {
            isOverdue = intent.getStringExtra("isOverdue");
            setTitle(isOverdue.equals("1") ? "过期物品" : "临期物品");
            num = intent.getStringExtra("num");
            if (num.equals("0")) {
                setContentView(R.layout.none);
            }
            else {
                recyclerView = findViewById(R.id.goodsRV);
                GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new RemindGoodsAdapter(this, this, isOverdue));
            }
        }
    }

    @Override
    protected void onResume() {
        if (!num.equals("0")) {
            recyclerView.setAdapter(new RemindGoodsAdapter(this, this, isOverdue));
        }
        super.onResume();
    }

    public void reSetAdapter() {
        Cursor cursor;
        if (isOverdue.equals("1")) {
            cursor = queryGoods(this, null, "isOvertime=?", new String[]{"1"});
        }
        else {
            cursor = queryGoods(this, null, "isCloseOvertime=?", new String[]{"1"});
        }
        num = String.valueOf(cursor.getCount());
        if (!num.equals("0")) {
            recyclerView.setAdapter(new RemindGoodsAdapter(this, this, isOverdue));
        }
        else {
            setContentView(R.layout.none);
        }
    }
}