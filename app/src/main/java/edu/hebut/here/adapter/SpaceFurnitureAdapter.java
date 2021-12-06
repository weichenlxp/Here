package edu.hebut.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.ui.space.SpaceContent;

import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class SpaceFurnitureAdapter extends RecyclerView.Adapter<SpaceFurnitureAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    int userID, houseID;
    SpaceContent spaceContent;
    private Context mContext;//定义上下文
    //集合
    private List<FurnitureEntity> listFurnitureEntity = new ArrayList<>();

    public SpaceFurnitureAdapter(Context mContext, String id, SpaceContent spaceContent) {
        this.mContext = mContext;
        this.spaceContent = spaceContent;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
        Cursor roomCursor = queryRoom(this.mContext, new String[]{"roomID"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
        roomCursor.moveToPosition(Integer.parseInt(id));
        int roomID = roomCursor.getInt(0);
        Cursor furnitureCursor = queryFurniture(this.mContext, new String[]{"furnitureID", "furnitureName"}, "userID=? AND roomID=?", new String[]{String.valueOf(userID), String.valueOf(roomID)});
        int[] temp1 = new int[furnitureCursor.getCount()];
        String[] temp2 = new String[furnitureCursor.getCount()];
        int[] temp3 = new int[furnitureCursor.getCount()];

        for (int i = 0; furnitureCursor.moveToNext(); i++) {
            temp1[i] = furnitureCursor.getInt(0);
            temp2[i] = furnitureCursor.getString(1);
            Cursor temp = queryGoods(mContext, new String[]{"goodsID"}, "furnitureID=? AND userID=? AND packed=?", new String[]{String.valueOf(temp1[i]), String.valueOf(userID), String.valueOf(0)});
            temp3[i] = temp.getCount();
            FurnitureEntity furnitureEntity = new FurnitureEntity(temp1[i], temp2[i], temp3[i]);
            listFurnitureEntity.add(furnitureEntity);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_space_furniture, parent, false);
        return new MyViewHolder(view);
    }


    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.furnitureName.setText(listFurnitureEntity.get(position).getFurnitureName());
        holder.goodsSum.setText(String.valueOf(listFurnitureEntity.get(position).getGoodsSum()));
        holder.layout_content.setOnClickListener(v -> {
            //打开家具
            Intent intent = new Intent(mContext, edu.hebut.here.ui.space.SpaceGoodsCommon.class);
            intent.setAction("one");
            intent.putExtra("furnitureID", listFurnitureEntity.get(position).getFurnitureID());
            intent.putExtra("furnitureName", listFurnitureEntity.get(position).getFurnitureName());
            intent.putExtra("goodsSum", listFurnitureEntity.get(position).getGoodsSum());
            mContext.startActivity(intent);
        });

    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listFurnitureEntity.size();
    }

    public static class FurnitureEntity {
        int furnitureID;
        String furnitureName;
        int goodsSum;

        public FurnitureEntity(int furnitureID, String furnitureName, int goodsSum) {
            this.furnitureID = furnitureID;
            this.furnitureName = furnitureName;
            this.goodsSum = goodsSum;
        }

        public int getFurnitureID() {
            return furnitureID;
        }

        public void setFurnitureID(int furnitureID) {
            this.furnitureID = furnitureID;
        }

        public String getFurnitureName() {
            return furnitureName;
        }

        public void setFurnitureName(String furnitureName) {
            this.furnitureName = furnitureName;
        }

        public int getGoodsSum() {
            return goodsSum;
        }

        public void setGoodsSum(int goodsSum) {
            this.goodsSum = goodsSum;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView furnitureName;//名字
        public TextView goodsSum;
        public ViewGroup layout_content;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            furnitureName = itemView.findViewById(R.id.furnitureName);
            goodsSum = itemView.findViewById(R.id.goodsSum);
            layout_content = itemView.findViewById(R.id.layout_content);
        }
    }
}
