package edu.hebut.here.ui.remind;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import edu.hebut.here.R;
import edu.hebut.here.entity.Goods;
import edu.hebut.here.utils.BitmapUtils;
import edu.hebut.here.utils.DateUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.hebut.here.activity.AppContext.httpURL;
import static edu.hebut.here.activity.AppContext.remindGoodsCreate;
import static edu.hebut.here.data.MyContentResolver.queryCategory;
import static edu.hebut.here.data.MyContentResolver.queryContainer;
import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryRoom;
import static edu.hebut.here.data.MyContentResolver.queryUser;

public class AddGoodsActivity extends AppCompatActivity {
    String servlet = "GoodsCreateServlet";
    private static final int REQUEST_IMAGE_GET1 = 0;
    private static final int REQUEST_IMAGE_GET2 = 1;
    private static final int REQUEST_IMAGE_GET3 = 2;
    private static final int REQUEST_IMAGE_CAPTURE1 = 3;
    private static final int REQUEST_IMAGE_CAPTURE2 = 4;
    private static final int REQUEST_IMAGE_CAPTURE3 = 5;
    private static final int REQUEST_SMALL_IMAGE_CUTTING1 = 6;
    private static final int REQUEST_SMALL_IMAGE_CUTTING2 = 7;
    private static final int REQUEST_SMALL_IMAGE_CUTTING3 = 8;
    private static final int REQUEST_BIG_IMAGE_CUTTING1 = 9;
    private static final int REQUEST_BIG_IMAGE_CUTTING2 = 10;
    private static final int REQUEST_BIG_IMAGE_CUTTING3 = 11;
    private static final String IMAGE_FILE_NAME = "icon.jpg";
    SharedPreferences sharedPreferences;
    TextView cancel;
    TextView save;
    ImageView addGoodsGoodsPhoto1;
    ImageView addGoodsGoodsPhoto2;
    ImageView addGoodsGoodsPhoto3;
    EditText addGoodsGoodsName;
    Spinner addGoodsRoomName;
    Spinner addGoodsFurnitureName;
    Spinner addGoodsCategoryName;
    ImageView addGoodsContainerLabel;
    Switch addGoodsPacked;
    Spinner addGoodsContainerName;
    EditText addGoodsGoodsNum;
    TextView addGoodsBuyTime;
    TextView addGoodsManufactureDate;
    EditText addGoodsQualityGuaranteePeriod;
    Spinner addGoodsQualityGuaranteePeriodType;
    EditText addGoodsRemark;
    String[] roomNameList;
    String[] furnitureNameList;
    String[] categoryNameList;
    String[] containerNameList;
    String goodsName;
    String roomName;
    int roomID = -1;
    String furnitureName;
    int furnitureID = -1;
    String categoryName;
    int categoryID = -1;
    String containerName;
    int containerID = -1;
    String buyTime;
    String manufactureDate;
    String qualityGuaranteePeriod;
    String qualityGuaranteePeriodType = "天";
    String remark;
    Dialog dialog;
    private Uri mImageUri;

    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == remindGoodsCreate) {
                if (msg.obj.equals(true)){
                    Toast.makeText(AddGoodsActivity.this, "物品添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddGoodsActivity.this, "物品添加失败", Toast.LENGTH_SHORT).show();
                }
                AddGoodsActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        addGoodsGoodsPhoto1 = findViewById(R.id.add_goods_goodsPhoto1);
        addGoodsGoodsPhoto2 = findViewById(R.id.add_goods_goodsPhoto2);
        addGoodsGoodsPhoto3 = findViewById(R.id.add_goods_goodsPhoto3);
        addGoodsGoodsName = findViewById(R.id.add_goods_goodsName);
        addGoodsRoomName = findViewById(R.id.add_goods_roomName);
        addGoodsFurnitureName = findViewById(R.id.add_goods_furnitureName);
        addGoodsFurnitureName.setEnabled(false);
        addGoodsCategoryName = findViewById(R.id.add_goods_categoryName);
        addGoodsContainerLabel = findViewById(R.id.add_goods_img_tip_containerName);
        addGoodsPacked = findViewById(R.id.add_goods_packed);
        addGoodsContainerName = findViewById(R.id.add_goods_containerName);
        addGoodsGoodsNum = findViewById(R.id.add_goods_goodsNum);

        addGoodsGoodsPhoto1.setOnClickListener(v -> {
            showDialog("1");
        });
        addGoodsGoodsPhoto2.setOnClickListener(v -> {
            showDialog("2");
        });
        addGoodsGoodsPhoto3.setOnClickListener(v -> {
            showDialog("3");
        });

        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", -1);
        int houseID = sharedPreferences.getInt("houseID", -1);
        Cursor room = queryRoom(this, new String[]{"roomID", "roomName"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
        String[] tempRoomName = new String[room.getCount()];
        int[] tempRoomID = new int[room.getCount()];
        for (int i = 0; room.moveToNext(); i++) {
            tempRoomID[i] = room.getInt(0);
            tempRoomName[i] = room.getString(1);
        }
        roomNameList = tempRoomName;
        ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomNameList);
        roomNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addGoodsRoomName.setAdapter(roomNameAdapter);
        addGoodsRoomName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                roomName = roomNameList[pos];

                Cursor furniture = queryFurniture(getApplicationContext(), new String[]{"furnitureName"}, "userID=? AND roomID=?", new String[]{String.valueOf(userID), String.valueOf(tempRoomID[pos])});
                String[] tempFurnitureName = new String[furniture.getCount()];
                for (int i = 0; furniture.moveToNext(); i++) {
                    tempFurnitureName[i] = furniture.getString(0);
                }
                furnitureNameList = tempFurnitureName;
                ArrayAdapter<String> furnitureNameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, furnitureNameList);
                furnitureNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addGoodsFurnitureName.setAdapter(furnitureNameAdapter);
                addGoodsFurnitureName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        furnitureName = furnitureNameList[pos];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });
                addGoodsFurnitureName.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        Cursor category = queryCategory(getApplicationContext(), new String[]{"categoryName"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] tempCategoryName = new String[category.getCount()];
        for (int i = 0; category.moveToNext(); i++) {
            tempCategoryName[i] = category.getString(0);
        }
        categoryNameList = tempCategoryName;
        ArrayAdapter<String> categoryNameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryNameList);
        categoryNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addGoodsCategoryName.setAdapter(categoryNameAdapter);
        addGoodsCategoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                categoryName = categoryNameList[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        TextView textView = new TextView(this);
        textView.setText("打包开关为开时，此物品会显示在“打包”中，而不显示在“空间”中");
        final PopupWindow packTip = new PopupWindow(textView, 400, 400);//参数为1.View 2.宽度 3.高度
        packTip.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
        addGoodsContainerName.setEnabled(false);
        addGoodsContainerLabel.setOnClickListener(v -> {
            packTip.showAsDropDown(addGoodsContainerLabel);//设置popupWindow显示,并且告诉它显示在那个View下面
        });

        addGoodsPacked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addGoodsContainerName.setEnabled(isChecked);
            if (isChecked) {
                addGoodsPacked.setText("开");
            } else {
                addGoodsPacked.setText("关");
            }
        });

        Cursor container = queryContainer(getApplicationContext(), new String[]{"containerName"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] tempContainerName = new String[container.getCount()];
        for (int i = 0; container.moveToNext(); i++) {
            tempContainerName[i] = container.getString(0);
        }
        containerNameList = tempContainerName;
        ArrayAdapter<String> containerNameAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, containerNameList);
        containerNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addGoodsContainerName.setAdapter(containerNameAdapter);
        addGoodsContainerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                containerName = containerNameList[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        addGoodsGoodsNum.setText("1");
        addGoodsBuyTime = findViewById(R.id.add_goods_buyTime);
        addGoodsBuyTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(AddGoodsActivity.this, (dp, year, month, dayOfMonth) -> {
                addGoodsBuyTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        addGoodsManufactureDate = findViewById(R.id.add_goods_manufactureDate);
        addGoodsManufactureDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(AddGoodsActivity.this, (dp, year, month, dayOfMonth) -> {
                addGoodsManufactureDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        addGoodsQualityGuaranteePeriod = findViewById(R.id.add_goods_qualityGuaranteePeriod);
        addGoodsQualityGuaranteePeriodType = findViewById(R.id.add_goods_qualityGuaranteePeriodType);
        addGoodsQualityGuaranteePeriodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] SpinnerItem = getResources().getStringArray(R.array.SpinnerItem);
                qualityGuaranteePeriodType = SpinnerItem[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        addGoodsRemark = findViewById(R.id.add_goods_remark);
        cancel = findViewById(R.id.add_goods_cancel);
        cancel.setOnClickListener(v -> {
            AddGoodsActivity.this.finish();
        });
        save = findViewById(R.id.add_goods_save);
        save.setOnClickListener(v -> {
            Goods goods;
            goodsName = addGoodsGoodsName.getText().toString();
            String goodsNum = addGoodsGoodsNum.getText().toString();

            addGoodsGoodsPhoto1.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = addGoodsGoodsPhoto1.getDrawingCache();
            byte[] goodsPhoto1 = BitmapUtils.bitmapToByteArray(bitmap1);
            addGoodsGoodsPhoto1.setDrawingCacheEnabled(false);

            addGoodsGoodsPhoto2.setDrawingCacheEnabled(true);
            Bitmap bitmap2 = addGoodsGoodsPhoto2.getDrawingCache();
            byte[] goodsPhoto2 = BitmapUtils.bitmapToByteArray(bitmap2);
            addGoodsGoodsPhoto2.setDrawingCacheEnabled(false);

            addGoodsGoodsPhoto3.setDrawingCacheEnabled(true);
            Bitmap bitmap3 = addGoodsGoodsPhoto3.getDrawingCache();
            byte[] goodsPhoto3 = BitmapUtils.bitmapToByteArray(bitmap3);
            addGoodsGoodsPhoto3.setDrawingCacheEnabled(false);

            buyTime = addGoodsBuyTime.getText().toString();
            manufactureDate = addGoodsManufactureDate.getText().toString();
            qualityGuaranteePeriod = addGoodsQualityGuaranteePeriod.getText().toString();
            remark = addGoodsRemark.getText().toString();

            Date nowDate = new Date();
            String now = DateUtils.dateToString(nowDate);
            boolean flag1 = DateUtils.compareDate(buyTime, now) == -1;
            boolean flag2 = DateUtils.compareDate(manufactureDate, now) == -1;
            boolean flag3 = DateUtils.compareDate(manufactureDate, buyTime) == -1;

            if (buyTime.equals("选择日期")) {
                flag1 = true;
            }
            if (manufactureDate.equals("选择日期")) {
                flag2 = true;
            }
            if (buyTime.equals("选择日期") && manufactureDate.equals("选择日期")) {
                flag3 = true;
            }
            if (goodsName.equals("")) {
                Toast.makeText(this, "物品名称不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
            } else if (!flag1) {
                Toast.makeText(this, "购买日期不能晚于当前日期，请重新选择！", Toast.LENGTH_SHORT).show();
            } else if (!flag2) {
                Toast.makeText(this, "生产日期不能晚于当前日期，请重新选择！", Toast.LENGTH_SHORT).show();
            } else if (!flag3) {
                Toast.makeText(this, "生产日期不能晚于购买日期，请重新选择！", Toast.LENGTH_SHORT).show();
            } else {
                int reminderTime = -1;
                Cursor userCursor = queryUser(this, new String[]{"reminderTime"}, "userID=?", new String[]{String.valueOf(userID)});
                while (userCursor.moveToNext()) {
                    reminderTime = userCursor.getInt(0);
                }

                Cursor roomCursor = queryRoom(this, new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{roomName, String.valueOf(houseID)}, null);
                while (roomCursor.moveToNext()) {
                    roomID = roomCursor.getInt(0);
                }
                Cursor furnitureCursor = queryFurniture(getApplicationContext(), new String[]{"furnitureID"}, "furnitureName=? AND roomID=?", new String[]{furnitureName, String.valueOf(roomID)});
                while (furnitureCursor.moveToNext()) {
                    furnitureID = furnitureCursor.getInt(0);
                }
                Cursor categoryCursor = queryCategory(getApplicationContext(), new String[]{"categoryID"}, "categoryName=? AND userID=?", new String[]{categoryName, String.valueOf(userID)});
                while (categoryCursor.moveToNext()) {
                    categoryID = categoryCursor.getInt(0);
                }

                if (addGoodsPacked.isChecked()) {
                    Cursor containerCursor = queryContainer(getApplicationContext(), new String[]{"containerID"}, "containerName=? AND userID=?", new String[]{containerName, String.valueOf(userID)});
                    while (containerCursor.moveToNext()) {
                        containerID = containerCursor.getInt(0);
                    }
                } else {
                    Cursor containerCursor = queryContainer(getApplicationContext(), new String[]{"containerID"}, "userID=?", new String[]{String.valueOf(userID)});
                    if (containerCursor.moveToNext()) {
                        containerID = containerCursor.getInt(0);
                    }
                }
                if (qualityGuaranteePeriod.equals("") || manufactureDate.equals("选择日期")) {
                    goods = new Goods(0, goodsName, userID, houseID, roomID, furnitureID, categoryID, containerID, Integer.parseInt(goodsNum), goodsPhoto1.toString(), goodsPhoto2.toString(), goodsPhoto3.toString(), buyTime, manufactureDate, Integer.parseInt(qualityGuaranteePeriod), qualityGuaranteePeriodType, null, false, false, addGoodsPacked.isChecked(), remark);
                    createGoods(goods);
                } else {
                    Date productDate = DateUtils.stringToDate(manufactureDate);
                    Date overtimeDate = DateUtils.addValue(productDate, Integer.parseInt(qualityGuaranteePeriod), qualityGuaranteePeriodType);
                    String overtime = DateUtils.dateToString(overtimeDate);
                    int subDate = DateUtils.getDaysIntervalStr(now, overtime);
                    if (subDate < 0) {
                        goods = new Goods(0, goodsName, userID, houseID, roomID, furnitureID, categoryID, containerID, Integer.parseInt(goodsNum), goodsPhoto1.toString(), goodsPhoto2.toString(), goodsPhoto3.toString(), buyTime, manufactureDate, Integer.parseInt(qualityGuaranteePeriod), qualityGuaranteePeriodType, "已过期" + (-subDate) + "天", true, false, addGoodsPacked.isChecked(), remark);
                        createGoods(goods);
                    } else{
                        goods = new Goods(0, goodsName, userID, houseID, roomID, furnitureID, categoryID, containerID, Integer.parseInt(goodsNum), goodsPhoto1.toString(), goodsPhoto2.toString(), goodsPhoto3.toString(), buyTime, manufactureDate, Integer.parseInt(qualityGuaranteePeriod), qualityGuaranteePeriodType, "还有" + subDate + "天过期", false, subDate <= reminderTime, addGoodsPacked.isChecked(), remark);
                        createGoods(goods);
                    }
                }
            }
        });
    }
    public void createGoods(Goods goods) {
        OkHttpClient client = new OkHttpClient();
        //将对象转换为json字符串
        String json = JSON.toJSONString(goods);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servlet)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(AddGoodsActivity.this, "创建物品失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendMessage(res, remindGoodsCreate);
                    }
                }.start();
            }
        });
    }

    public void sendMessage(String result, int what) {
        Message message=Message.obtain();
        message.what= what;
        message.obj=result;
        handler.sendMessage(message);
    }

    private void showDialog(String which) {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ImageView btn_picture = window.findViewById(R.id.btn_picture);
        ImageView btn_photo = window.findViewById(R.id.btn_photo);
        ImageView btn_cancel = window.findViewById(R.id.btn_cancel);

        btn_picture.setOnClickListener(v -> {
            switch (which) {
                case "1":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 201);
                    } else {
                        // 如果权限已经申请过，直接进行图片选择
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET1);
                        } else {
                            Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "2":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 202);
                    } else {
                        // 如果权限已经申请过，直接进行图片选择
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET2);
                        } else {
                            Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "3":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 203);
                    } else {
                        // 如果权限已经申请过，直接进行图片选择
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GET3);
                        } else {
                            Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        });
        btn_photo.setOnClickListener(v -> {
            switch (which) {
                case "1":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 301);
                    } else {
                        // 权限已经申请，直接拍照
                        dialog.dismiss();
                        imageCapture(which);
                    }
                    break;
                case "2":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 302);
                    } else {
                        // 权限已经申请，直接拍照
                        dialog.dismiss();
                        imageCapture(which);
                    }
                    break;
                case "3":
                    if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(AddGoodsActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 303);
                    } else {
                        // 权限已经申请，直接拍照
                        dialog.dismiss();
                        imageCapture(which);
                    }
                    break;
            }
        });
        btn_cancel.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * 处理回调结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 小图切割
                case REQUEST_SMALL_IMAGE_CUTTING1:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                case REQUEST_SMALL_IMAGE_CUTTING2:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                case REQUEST_SMALL_IMAGE_CUTTING3:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                // 大图切割
                case REQUEST_BIG_IMAGE_CUTTING1:
                    Bitmap bitmap1 = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    addGoodsGoodsPhoto1.setImageBitmap(bitmap1);
                    break;
                case REQUEST_BIG_IMAGE_CUTTING2:
                    Bitmap bitmap2 = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    addGoodsGoodsPhoto2.setImageBitmap(bitmap2);
                    break;
                case REQUEST_BIG_IMAGE_CUTTING3:
                    Bitmap bitmap3 = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    addGoodsGoodsPhoto3.setImageBitmap(bitmap3);
                    break;
                // 相册选取
                case REQUEST_IMAGE_GET1:
                    try {
                        // startSmallPhotoZoom(data.getData());
                        startBigPhotoZoom(data.getData(), "1");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_IMAGE_GET2:
                    try {
                        // startSmallPhotoZoom(data.getData());
                        startBigPhotoZoom(data.getData(), "2");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_IMAGE_GET3:
                    try {
                        // startSmallPhotoZoom(data.getData());
                        startBigPhotoZoom(data.getData(), "3");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE1:
                    File temp1 = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startBigPhotoZoom(temp1, "1");
                case REQUEST_IMAGE_CAPTURE2:
                    File temp2 = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startBigPhotoZoom(temp2, "2");
                case REQUEST_IMAGE_CAPTURE3:
                    File temp3 = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startBigPhotoZoom(temp3, "3");
            }
        }
    }

    /**
     * 处理权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 201:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET1);
                    } else {
                        Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                }
                break;
            case 202:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET2);
                    } else {
                        Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                }
                break;
            case 203:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET3);
                    } else {
                        Toast.makeText(AddGoodsActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                }
                break;
            case 301:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    imageCapture("1");
                } else {
                    dialog.dismiss();
                }
                break;
            case 302:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    imageCapture("2");
                } else {
                    dialog.dismiss();
                }
                break;
            case 303:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    imageCapture("3");
                } else {
                    dialog.dismiss();
                }
                break;
        }
    }

    /**
     * 判断系统及拍照
     */
    private void imageCapture(String which) {
        Intent intent;
        Uri pictureUri;
        File pictureFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pictureUri = FileProvider.getUriForFile(this,
                    "edu.hebut.here.fileProvider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//        intent.putExtra("which", which);
//        System.out.println(intent.getStringExtra("which"));
        switch (which) {
            case "1":
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE1);
                break;
            case "2":
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE2);
                break;
            case "3":
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE3);
                break;
        }

    }

    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     */
    public void startBigPhotoZoom(File inputFile, String which) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("startBigPhotoZoomFile", "文件夹创建失败");
                } else {
                    Log.e("startBigPhotoZoomFile", "文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
            mImageUri = imageUri; // 将 uri 传出，方便设置到视图中
        }

        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(AddGoodsActivity.this, inputFile), "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // intent.putExtra("noFaceDetection", true); // no face detection
        switch (which) {
            case "1":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING1);
                break;
            case "2":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING2);
                break;
            case "3":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING3);
                break;
        }
    }

    public void startBigPhotoZoom(Uri uri, String which) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("startBigPhotoZoomUri", "文件夹创建失败");
                } else {
                    Log.e("startBigPhotoZoomUri", "文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
            mImageUri = imageUri; // 将 uri 传出，方便设置到视图中
        }

        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // intent.putExtra("noFaceDetection", true); // no face detection
        switch (which) {
            case "1":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING1);
                break;
            case "2":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING2);
                break;
            case "3":
                startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING3);
                break;
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 小图模式中，保存图片后，设置到视图中
     * 将图片保存设置到视图中
     */
    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(storage + "/smallIcon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.e("TAG", "文件夹创建失败");
                    } else {
                        Log.e("TAG", "文件夹创建成功");
                    }
                }
                File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
                // 保存图片
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 在视图中显示图片
            switch (data.getStringExtra("which")) {
                case "1":
                    addGoodsGoodsPhoto1.setImageBitmap(photo);
                    break;
                case "2":
                    addGoodsGoodsPhoto2.setImageBitmap(photo);
                    break;
                case "3":
                    addGoodsGoodsPhoto3.setImageBitmap(photo);
                    break;
            }
        }
    }
}