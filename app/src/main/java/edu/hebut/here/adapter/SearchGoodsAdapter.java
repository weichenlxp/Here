package edu.hebut.here.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
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
import edu.hebut.here.ui.search.SearchActivity;
import edu.hebut.here.ui.search.SearchGoodsDialog;

import static edu.hebut.here.data.MyContentResolver.deleteGoods;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.utils.BitmapUtils.byteArrayToBitmap;

public class SearchGoodsAdapter extends RecyclerView.Adapter<SearchGoodsAdapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    int userID, houseID;
    String goodsName;
    Cursor goodsCursorHome, goodsCursorContainer;
    boolean isAll;
    private Context mContext;//定义上下文
    private SearchActivity activity;
    //集合
    private List<GoodsEntity> listGoodsEntity = new ArrayList<>();

    public SearchGoodsAdapter(Context mContext, SearchActivity activity, String goodsName, boolean selectAll) {
        Log.e("SearchGoodsAdapter","start");
        this.mContext = mContext;
        this.activity = activity;
        this.goodsName = goodsName;
        goodsName = "goodsName LIKE '%" + goodsName + "%' ";
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
        if (selectAll) {
            isAll=true;
            goodsCursorHome = queryGoods(this.mContext, new String[]{"goodsID", "goodsName", "goodsPhoto1", "isOvertime", "isCloseOvertime"}, "userID=? AND "+goodsName, new String[]{String.valueOf(userID)});
        }
        else {
            isAll=false;
            goodsCursorHome = queryGoods(this.mContext, new String[]{"goodsID", "goodsName", "goodsPhoto1", "isOvertime", "isCloseOvertime"}, "userID=? AND houseID=? AND packed=0 AND "+goodsName, new String[]{String.valueOf(userID), String.valueOf(houseID)});
            goodsCursorContainer = queryGoods(this.mContext, new String[]{"goodsID", "goodsName", "goodsPhoto1", "isOvertime", "isCloseOvertime"}, "userID=? AND packed=1 AND "+goodsName, new String[]{String.valueOf(userID)});
        }
        int length;
        if (!isAll) {
            length = goodsCursorHome.getCount()+goodsCursorContainer.getCount();
        }
        else {
            length = goodsCursorHome.getCount();
        }
        int[] temp1 = new int[length];
        String[] temp2 = new String[length];
        byte[][] temp3 = new byte[length][];
        short[] temp4 = new short[length];
        short[] temp5 = new short[length];
        int overdue;
        for (int i = 0; goodsCursorHome.moveToNext(); i++) {
            Log.e("for",goodsCursorHome.getString(1));
            temp1[i] = goodsCursorHome.getInt(0);
            temp2[i] = goodsCursorHome.getString(1);
            temp3[i] = goodsCursorHome.getBlob(2);
            temp4[i] = goodsCursorHome.getShort(3);
            temp5[i] = goodsCursorHome.getShort(4);
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
        if (!isAll) {
            for (int i = 0; goodsCursorContainer.moveToNext(); i++) {
                Log.e("for",goodsCursorContainer.getString(1));
                temp1[i] = goodsCursorContainer.getInt(0);
                temp2[i] = goodsCursorContainer.getString(1);
                temp3[i] = goodsCursorContainer.getBlob(2);
                temp4[i] = goodsCursorContainer.getShort(3);
                temp5[i] = goodsCursorContainer.getShort(4);
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
        Log.e("onBindViewHolder","start");
        holder.goodsName.setText(listGoodsEntity.get(position).goodsName);
        Bitmap bitmap = byteArrayToBitmap(listGoodsEntity.get(position).goodsPhoto);
        holder.goodsPhoto.setImageBitmap(bitmap);
        holder.overdue.setImageResource(listGoodsEntity.get(position).overdue);
        holder.layout_content.setOnClickListener(v -> {
            SearchGoodsDialog searchGoodsDialog = new SearchGoodsDialog();
            searchGoodsDialog.showBottomSheetDialog(mContext, activity, listGoodsEntity.get(position).goodsID, this, holder, position);
        });

    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listGoodsEntity.size();
    }

    public void delete(MyViewHolder holder, int position) {
        int n = holder.getLayoutPosition();//获取要删除行的位置
        deleteGoods(mContext, "goodsID=?", new String[]{String.valueOf(listGoodsEntity.get(position).goodsID)});
        activity.reSetAdapter();
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
