package edu.hebut.here.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.ui.space.SpaceGoodsCommon;
import edu.hebut.here.ui.space.SpaceGoodsDialog;

import static edu.hebut.here.data.MyContentResolver.deleteGoods;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.utils.BitmapUtils.byteArrayToBitmap;

public class SpaceGoodsAdapter extends RecyclerView.Adapter<SpaceGoodsAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    int userID, furnitureID;
    private Context mContext;//定义上下文
    private SpaceGoodsCommon activity;

    //集合
    private List<GoodsEntity> listGoodsEntity = new ArrayList<>();

    public SpaceGoodsAdapter(Context mContext, SpaceGoodsCommon activity, int furnitureID) {
        this.mContext = mContext;
        this.activity = activity;
        this.furnitureID = furnitureID;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);

        Cursor goodsCursor = queryGoods(this.mContext, new String[]{"goodsID", "goodsName", "goodsPhoto1", "isOvertime", "isCloseOvertime"}, "userID=? AND furnitureID=? AND packed=?", new String[]{String.valueOf(userID), String.valueOf(furnitureID), String.valueOf(0)});
        int[] temp1 = new int[goodsCursor.getCount()];
        String[] temp2 = new String[goodsCursor.getCount()];
        byte[][] temp3 = new byte[goodsCursor.getCount()][];
        short[] temp4 = new short[goodsCursor.getCount()];
        short[] temp5 = new short[goodsCursor.getCount()];
        int overdue;
        for (int i = 0; goodsCursor.moveToNext(); i++) {
            temp1[i] = goodsCursor.getInt(0);
            temp2[i] = goodsCursor.getString(1);
            temp3[i] = goodsCursor.getBlob(2);
            temp4[i] = goodsCursor.getShort(3);
            temp5[i] = goodsCursor.getShort(4);
            if (temp4[i] == 1) {
                overdue = mContext.getResources().getIdentifier("ic_overdue", "drawable", mContext.getPackageName());
            } else if (temp5[i] == 1) {
                overdue = mContext.getResources().getIdentifier("ic_close_overdue", "drawable", mContext.getPackageName());
            } else {
                overdue = mContext.getResources().getIdentifier("ic_check", "drawable", mContext.getPackageName());
            }
            GoodsEntity goodsEntity = new GoodsEntity(temp1[i], temp2[i], temp3[i], overdue);
            listGoodsEntity.add(goodsEntity);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        return new MyViewHolder(view);
    }


    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置名称
        holder.goodsName.setText(listGoodsEntity.get(position).goodsName);
        Bitmap bitmap = byteArrayToBitmap(listGoodsEntity.get(position).goodsPhoto);
        holder.goodsPhoto.setImageBitmap(bitmap);
        holder.overdue.setImageResource(listGoodsEntity.get(position).overdue);
        holder.layout_content.setOnClickListener(v -> {
            SpaceGoodsDialog spaceGoodsDialog = new SpaceGoodsDialog();
            spaceGoodsDialog.showBottomSheetDialog(mContext, activity, listGoodsEntity.get(position).goodsID, this, holder, position);
        });

    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listGoodsEntity.size();
    }

    public void delete(MyViewHolder holder, int position) {
        deleteGoods(mContext, "goodsID=?", new String[]{String.valueOf(listGoodsEntity.get(position).goodsID)});
        Cursor goods = queryGoods(this.mContext, null, "userID=? AND furnitureID=? AND packed=?", new String[]{String.valueOf(userID), String.valueOf(furnitureID), String.valueOf(0)});
        activity.reSetAdapter(goods.getCount());
        Toast.makeText(this.mContext, "删除成功！", Toast.LENGTH_SHORT).show();
    }

    public static class GoodsEntity {
        int goodsID;
        String goodsName;
        byte[] goodsPhoto;
        int overdue;

        public GoodsEntity(int goodsID, String goodsName, byte[] goodsPhoto, int overdue) {
            this.goodsID = goodsID;
            this.goodsName = goodsName;
            this.goodsPhoto = goodsPhoto;
            this.overdue = overdue;
        }

        public int getGoodsID() {
            return goodsID;
        }

        public void setGoodsID(int goodsID) {
            this.goodsID = goodsID;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public byte[] getGoodsPhoto() {
            return goodsPhoto;
        }

        public void setGoodsPhoto(byte[] goodsPhoto) {
            this.goodsPhoto = goodsPhoto;
        }

        public int isOverdue() {
            return overdue;
        }

        public void setOverdue(int overdue) {
            this.overdue = overdue;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView goodsName;//名字
        public ImageView goodsPhoto;
        public ImageView overdue;
        public ViewGroup layout_content;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            goodsName = itemView.findViewById(R.id.goodsName);
            goodsPhoto = itemView.findViewById(R.id.goodsPhoto);
            overdue = itemView.findViewById(R.id.overdue);
            layout_content = itemView.findViewById(R.id.layout_content);
        }
    }
}
