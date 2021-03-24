package edu.hebut.here.ui.remind;

import android.Manifest;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import edu.hebut.here.MainActivity;
import edu.hebut.here.R;
import edu.hebut.here.data.MyContentResolver;
import edu.hebut.here.utils.*;

public class AddGoodsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText addGoodsGoodsName;
    EditText addGoodsRoomName;
    EditText addGoodsFurnitureName;
    EditText addGoodsCategoryName;
    EditText addGoodsGoodsNum;
    ImageView addGoodsGoodsPhoto1;
    ImageView addGoodsGoodsPhoto2;
    ImageView addGoodsGoodsPhoto3;
    TextView addGoodsBuyTime;
    TextView addGoodsManufactureDate;
    EditText addGoodsQualityGuaranteePeriod;
    Spinner addGoodsQualityGuaranteePeriodType;
    EditText addGoodsRemark;
    TextView save;
    TextView cancel;
    String qualityGuaranteePeriodType=null;

    Dialog dialog;
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

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        addGoodsGoodsName = findViewById(R.id.add_goods_goodsName);
        addGoodsRoomName = findViewById(R.id.add_goods_roomName);
        addGoodsFurnitureName = findViewById(R.id.add_goods_furnitureName);
        addGoodsCategoryName = findViewById(R.id.add_goods_categoryName);
        addGoodsGoodsNum = findViewById(R.id.add_goods_goodsNum);
        addGoodsGoodsPhoto1 = findViewById(R.id.add_goods_goodsPhoto1);
        addGoodsGoodsPhoto2 = findViewById(R.id.add_goods_goodsPhoto2);
        addGoodsGoodsPhoto3 = findViewById(R.id.add_goods_goodsPhoto3);
        addGoodsGoodsPhoto1.setOnClickListener(v -> {
            showDialog("1");
        });
        addGoodsGoodsPhoto2.setOnClickListener(v -> {
            showDialog("2");
        });
        addGoodsGoodsPhoto3.setOnClickListener(v -> {
            showDialog("3");
        });

        addGoodsBuyTime = findViewById(R.id.add_goods_buyTime);
        addGoodsBuyTime.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            new DatePickerDialog(AddGoodsActivity.this, (dp, year, month, dayOfMonth)->{
                addGoodsBuyTime.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        addGoodsManufactureDate = findViewById(R.id.add_goods_manufactureDate);
        addGoodsManufactureDate.setOnClickListener(v -> {
            Calendar calendar= Calendar.getInstance();
            new DatePickerDialog(AddGoodsActivity.this, (dp, year, month, dayOfMonth)->{
                addGoodsManufactureDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
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
            Intent intent=new Intent(AddGoodsActivity.this, edu.hebut.here.MainActivity.class);
            intent.putExtra("is_back", "1");
            startActivity(intent);
        });
        save = findViewById(R.id.add_goods_save);
        save.setOnClickListener(v -> {
            String goodsName = addGoodsGoodsName.getText().toString();
            String roomName = addGoodsRoomName.getText().toString();
            String furnitureName = addGoodsFurnitureName.getText().toString();
            String categoryName = addGoodsCategoryName.getText().toString();
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

            String buyTime = addGoodsBuyTime.getText().toString();
            String manufactureDate = addGoodsManufactureDate.getText().toString();
            String qualityGuaranteePeriod = addGoodsQualityGuaranteePeriod.getText().toString();
            String remark = addGoodsRemark.getText().toString();

            sharedPreferences= getSharedPreferences("here", Context.MODE_PRIVATE);
            int userID = sharedPreferences.getInt("userID", -1);
            int houseID = sharedPreferences.getInt("houseID", -1);
            Cursor roomCursor = MyContentResolver.queryRoomIDByRoomNameHouseID(getApplicationContext(), roomName, houseID);
            int roomID = -1;
            while (roomCursor.moveToNext()) {
                roomID = roomCursor.getInt(0);
            }
            Cursor furnitureCursor = MyContentResolver.queryFurnitureIDByFurnitureNameRoomID(getApplicationContext(), furnitureName, roomID);
            int furnitureID = -1;
            while (furnitureCursor.moveToNext()) {
                furnitureID = furnitureCursor.getInt(0);
            }
            Cursor categoryCursor = MyContentResolver.queryCategoryIDByCategoryNameUserID(getApplicationContext(), categoryName, userID);
            int categoryID = -1;
            while (categoryCursor.moveToNext()) {
                categoryID = categoryCursor.getInt(0);
            }

            if (qualityGuaranteePeriod.equals("") || qualityGuaranteePeriodType.equals("")) {
                MyContentResolver.createGoods(getApplicationContext(), goodsName, userID, houseID, roomID, furnitureID, categoryID, -1, Integer.parseInt(goodsNum), goodsPhoto1, goodsPhoto2, goodsPhoto3, buyTime, manufactureDate, null, null, null, false, false, false, remark);
            }
            else {
                Date nowDate = new Date();
                String now = DateUtils.fmtDateToYMD(nowDate);
                Date productDate = DateUtils.fmtStrToDate(manufactureDate);
                Date overtimeDate = DateUtils.addValue(productDate, Integer.parseInt(qualityGuaranteePeriod), qualityGuaranteePeriodType);
                String overtime = DateUtils.fmtDateToYMD(overtimeDate);
                boolean isOvertime;
                isOvertime = DateUtils.compare_date(overtime, now) == -1;
                MyContentResolver.createGoods(getApplicationContext(), goodsName, userID, houseID, roomID, furnitureID, categoryID, -1, Integer.parseInt(goodsNum), goodsPhoto1, goodsPhoto2, goodsPhoto3, buyTime, manufactureDate, qualityGuaranteePeriod, qualityGuaranteePeriodType, overtime, isOvertime, false, false, remark);
            }
            AddGoodsActivity.this.finish();
            Intent intent=new Intent(AddGoodsActivity.this, edu.hebut.here.MainActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AddGoodsActivity.this, edu.hebut.here.MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
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
            switch (which){
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
            switch (which){
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
                        setPicToView(data, "1");
                    }
                    break;
                case REQUEST_SMALL_IMAGE_CUTTING2:
                    if (data != null) {
                        setPicToView(data, "2");
                    }
                    break;
                case REQUEST_SMALL_IMAGE_CUTTING3:
                    if (data != null) {
                        setPicToView(data, "3");
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
        switch (which){
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
        switch (which){
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
        switch (which){
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
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

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
    private void setPicToView(Intent data, String which) {
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
            switch (data.getStringExtra("which")){
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