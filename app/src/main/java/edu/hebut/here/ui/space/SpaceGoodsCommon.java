package edu.hebut.here.ui.space;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.hebut.here.R;
import edu.hebut.here.adapter.SpaceGoodsAdapter;

public class SpaceGoodsCommon extends AppCompatActivity {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    int userID, furnitureID, goodSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_goods_common);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals("one")) {
            furnitureID = intent.getIntExtra("furnitureID", -1);
            goodSum = intent.getIntExtra("goodsSum", -1);
            String furnitureName = intent.getStringExtra("furnitureName");
            setTitle(furnitureName);
            if (goodSum == 0) {
                setContentView(R.layout.none);
            }
            else {
                recyclerView = findViewById(R.id.goodsRV);
                GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new SpaceGoodsAdapter(this, this, furnitureID));
            }
        }
    }

    @Override
    protected void onResume() {
        if (goodSum != 0) {
            recyclerView.setAdapter(new SpaceGoodsAdapter(this, this, furnitureID));
        }
        else {
            setContentView(R.layout.none);
        }
        super.onResume();
    }

    public void reSetAdapter(int count) {
        if (count != 0) {
            recyclerView.setAdapter(new SpaceGoodsAdapter(this, this, furnitureID));
        }
        else {
            setContentView(R.layout.none);
        }
    }
}