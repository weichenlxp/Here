package edu.hebut.here.ui.space;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import edu.hebut.here.R;
import edu.hebut.here.adapter.SpaceGoodsAdapter;
import edu.hebut.here.ui.ChangeGoodsActivity;
import edu.hebut.here.utils.BitmapUtils;

import static edu.hebut.here.data.MyContentResolver.queryCategory;
import static edu.hebut.here.data.MyContentResolver.queryContainer;
import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class SpaceGoodsDialog {
    public TextView delete;
    Context context;
    SpaceGoodsCommon activity;
    SpaceGoodsAdapter spaceGoodsAdapter;
    SharedPreferences sharedPreferences;
    TextView change;
    ImageView addGoodsGoodsPhoto1;
    ImageView addGoodsGoodsPhoto2;
    ImageView addGoodsGoodsPhoto3;
    TextView addGoodsGoodsName;
    TextView addGoodsRoomName;
    TextView addGoodsFurnitureName;
    TextView addGoodsCategoryName;
    TextView addGoodsContainerName;
    TextView addGoodsGoodsNum;
    TextView addGoodsBuyTime;
    TextView addGoodsManufactureDate;
    TextView addGoodsQualityGuaranteePeriod;
    TextView addGoodsRemark;
    int userID;
    Bitmap goodsPhoto1;
    Bitmap goodsPhoto2;
    Bitmap goodsPhoto3;
    String goodsName;
    String roomName;
    int roomID = -1;
    String furnitureName;
    int furnitureID = -1;
    String categoryName;
    short packed;
    int categoryID = -1;
    String containerName;
    int containerID = -1;
    int goodsNum;
    String buyTime;
    String manufactureDate;
    String qualityGuaranteePeriod;
    String qualityGuaranteePeriodType = null;
    String remark;
    private SpaceGoodsAdapter.MyViewHolder holder;

    public void showBottomSheetDialog(Context mContext, SpaceGoodsCommon activity, int goodsID, SpaceGoodsAdapter spaceGoodsAdapter, SpaceGoodsAdapter.MyViewHolder holder, int position) {
        this.context = mContext;
        this.activity = activity;
        this.spaceGoodsAdapter = spaceGoodsAdapter;
        this.holder = holder;
        sharedPreferences = mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_bottomsheet_goods, null);
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setDismissWithAnimation(true);
        dialog.show();
        WindowManager wm = activity.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight((int) (0.5 * height));

        Cursor goodsCursor = queryGoods(mContext, new String[]{"goodsName", "roomID", "furnitureID", "categoryID", "containerID", "goodsNum", "goodsPhoto1", "goodsPhoto2", "goodsPhoto3", "buyTime", "manufactureDate", "qualityGuaranteePeriod", "qualityGuaranteePeriodType", "packed", "remark"}, "goodsID=?", new String[]{String.valueOf(goodsID)});
        goodsCursor.moveToNext();

        change = view.findViewById(R.id.add_goods_change);
        delete = view.findViewById(R.id.add_goods_delete);
        addGoodsGoodsPhoto1 = view.findViewById(R.id.add_goods_goodsPhoto1);
        addGoodsGoodsPhoto2 = view.findViewById(R.id.add_goods_goodsPhoto2);
        addGoodsGoodsPhoto3 = view.findViewById(R.id.add_goods_goodsPhoto3);
        addGoodsGoodsName = view.findViewById(R.id.add_goods_goodsName);
        addGoodsRoomName = view.findViewById(R.id.add_goods_roomName);
        addGoodsFurnitureName = view.findViewById(R.id.add_goods_furnitureName);
        addGoodsCategoryName = view.findViewById(R.id.add_goods_categoryName);
        addGoodsContainerName = view.findViewById(R.id.add_goods_containerName);
        addGoodsGoodsNum = view.findViewById(R.id.add_goods_goodsNum);
        addGoodsBuyTime = view.findViewById(R.id.add_goods_buyTime);
        addGoodsManufactureDate = view.findViewById(R.id.add_goods_manufactureDate);
        addGoodsQualityGuaranteePeriod = view.findViewById(R.id.add_goods_qualityGuaranteePeriod);
        addGoodsRemark = view.findViewById(R.id.add_goods_remark);
//"goodsName", "roomID", "furnitureID", "categoryID", "containerID",
//"goodsNum", "goodsPhoto1", "goodsPhoto2", "goodsPhoto3", "buyTime",
//"manufactureDate", "qualityGuaranteePeriod", "qualityGuaranteePeriodType", "packed", "remark"
        goodsName = goodsCursor.getString(0);
        roomID = goodsCursor.getInt(1);
        furnitureID = goodsCursor.getInt(2);
        categoryID = goodsCursor.getInt(3);
        containerID = goodsCursor.getInt(4);
        goodsNum = goodsCursor.getInt(5);
        goodsPhoto1 = BitmapUtils.byteArrayToBitmap(goodsCursor.getBlob(6));
        goodsPhoto2 = BitmapUtils.byteArrayToBitmap(goodsCursor.getBlob(7));
        goodsPhoto3 = BitmapUtils.byteArrayToBitmap(goodsCursor.getBlob(8));
        buyTime = goodsCursor.getString(9);
        if (goodsCursor.getString(10) != null) {
            manufactureDate = goodsCursor.getString(10);
        } else {
            manufactureDate = "空";
        }
        qualityGuaranteePeriodType = goodsCursor.getString(12);
        if (goodsCursor.getString(11) != null) {
            qualityGuaranteePeriod = goodsCursor.getString(11);
        } else {
            qualityGuaranteePeriod = "空";
            qualityGuaranteePeriodType = "";
        }
        packed = goodsCursor.getShort(13);
        if (goodsCursor.getString(14) != null) {
            remark = goodsCursor.getString(14);
        } else {
            remark = "空";
        }

        Cursor roomCursor = queryRoom(mContext, new String[]{"roomName"}, "roomID=?", new String[]{String.valueOf(roomID)}, null);
        while (roomCursor.moveToNext()) {
            roomName = roomCursor.getString(0);
        }
        Cursor furnitureCursor = queryFurniture(mContext, new String[]{"furnitureName"}, "furnitureID=?", new String[]{String.valueOf(furnitureID)});
        while (furnitureCursor.moveToNext()) {
            furnitureName = furnitureCursor.getString(0);
        }
        Cursor categoryCursor = queryCategory(mContext, new String[]{"categoryName"}, "categoryID=?", new String[]{String.valueOf(categoryID)});
        while (categoryCursor.moveToNext()) {
            categoryName = categoryCursor.getString(0);
        }
        Cursor containerCursor = queryContainer(mContext, new String[]{"containerName"}, "containerID=?", new String[]{String.valueOf(containerID)});
        while (containerCursor.moveToNext()) {
            containerName = containerCursor.getString(0);
        }

        addGoodsGoodsPhoto1.setImageBitmap(goodsPhoto1);
        addGoodsGoodsPhoto2.setImageBitmap(goodsPhoto2);
        addGoodsGoodsPhoto2.setImageBitmap(goodsPhoto3);
        addGoodsGoodsName.setText(goodsName);
        addGoodsRoomName.setText(roomName);
        addGoodsFurnitureName.setText(furnitureName);
        addGoodsCategoryName.setText(categoryName);
        if (packed == 0) {
            addGoodsContainerName.setText("无");
        } else {
            addGoodsContainerName.setText(containerName);
        }
        addGoodsGoodsNum.setText(String.valueOf(goodsNum));
        addGoodsBuyTime.setText(buyTime);
        addGoodsManufactureDate.setText(manufactureDate);
        addGoodsQualityGuaranteePeriod.setText(qualityGuaranteePeriod + qualityGuaranteePeriodType);
        addGoodsRemark.setText(remark);

        change.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(activity, ChangeGoodsActivity.class);
            intent.setAction("goods");
            intent.putExtra("goodsID", goodsID);
            activity.startActivity(intent);
        });

        delete.setOnClickListener(v -> {
            spaceGoodsAdapter.delete(holder, position);
            dialog.dismiss();
        });

    }
}
