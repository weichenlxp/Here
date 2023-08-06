package edu.hebut.here;

import android.Manifest;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

import edu.hebut.here.utils.JudgeUtils;

import static edu.hebut.here.data.MyContentResolver.createAccount;
import static edu.hebut.here.data.MyContentResolver.createCategory;
import static edu.hebut.here.data.MyContentResolver.createContainer;
import static edu.hebut.here.data.MyContentResolver.createFurniture;
import static edu.hebut.here.data.MyContentResolver.createHouse;
import static edu.hebut.here.data.MyContentResolver.createRoom;
import static edu.hebut.here.data.MyContentResolver.createUser;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;
import static edu.hebut.here.data.MyContentResolver.queryUser;
import static edu.hebut.here.data.MyContentResolver.uri_user;
import static edu.hebut.here.utils.BitmapUtils.bitmapToByteArray;

public class SignActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;
    private static final String IMAGE_FILE_NAME = "icon.jpg";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView signProfilePhoto;
    TextView signUsername;
    TextView signPassword;
    TextView signRePassword;
    RadioGroup signGender;
    TextView tipSignUsername;
    TextView tipSignPassword;
    TextView tipSignRePassword;
    TextView tipSignGender;
    Button sign;
    TextView log;
    String[] accountName = {"宽带", "电表", "水表", "燃气", "有线电视", "加油卡"};
    String[] roomName = {"客厅", "主卧", "次卧", "厨房", "卫生间"};
    String[] furnitureName1 = {"茶几", "电视柜"};
    String[] furnitureName2 = {"床头柜", "衣柜"};
    String[] furnitureName3 = {"床头柜", "衣柜"};
    String[] furnitureName4 = {"橱柜1", "橱柜2"};
    String[] furnitureName5 = {"洗手台", "置物架"};
    String[] categoryName = {"男装", "饰品", "女装", "鞋靴", "内衣", "饰品", "母婴", "电器", "数码", "百货", "运动", "车品", "医药", "生鲜"};
    String[] containerName = {"行李箱", "背包"};
    String username = null;
    String password = null;
    byte[] profilePhoto;
    String gender = null;
    Dialog dialog;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signProfilePhoto = findViewById(R.id.sign_profilePhoto);
        signProfilePhoto.setOnClickListener(v -> {
            showDialog();
        });

        signUsername = findViewById(R.id.sign_username);
        signPassword = findViewById(R.id.sign_password);
        signRePassword = findViewById(R.id.sign_repassword);
        signGender = findViewById(R.id.sign_gender);

        tipSignUsername = findViewById(R.id.tip_sign_username);
        tipSignPassword = findViewById(R.id.tip_sign_password);
        tipSignRePassword = findViewById(R.id.tip_sign_repassword);
        tipSignGender = findViewById(R.id.tip_sign_gender);

        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signGender.setOnCheckedChangeListener(((group, checkedId) -> {
            gender = checkedId == R.id.sign_male ? "男" : "女";
        }));

        sign = findViewById(R.id.btn_sign);
        sign.setOnClickListener(view -> {
            if (JudgeUtils.judgeExist(getApplicationContext(), uri_user, null, "username=?", new String[]{signUsername.getText().toString()}, null)) {
                tipSignUsername.setText("已存在该用户");
            } else {
                tipSignUsername.setText("");
                if (!JudgeUtils.judgeUsername(signUsername.getText().toString())) {
                    tipSignUsername.setText("用户名格式错误");
                } else {
                    tipSignUsername.setText("");
                    if (!JudgeUtils.judgePassword(signPassword.getText().toString())) {
                        tipSignPassword.setText("密码格式错误");
                    } else if (!JudgeUtils.judgePassword(signRePassword.getText().toString())) {
                        tipSignPassword.setText("");
                        tipSignRePassword.setText("密码格式错误");
                    } else if (!signRePassword.getText().toString().equals(signPassword.getText().toString())) {
                        tipSignPassword.setText("");
                        tipSignRePassword.setText("两次密码不一致");
                    } else if (gender == null) {
                        tipSignRePassword.setText("");
                        tipSignGender.setText("请选择性别");
                    } else {
                        tipSignRePassword.setText("");
                        signProfilePhoto.setDrawingCacheEnabled(true);
                        Bitmap bitmap = signProfilePhoto.getDrawingCache();
                        profilePhoto = bitmapToByteArray(bitmap);
                        signProfilePhoto.setDrawingCacheEnabled(false);
                        username = signUsername.getText().toString();
                        password = signRePassword.getText().toString();
                        //建用户
                        createUser(getApplicationContext(), username, password, gender, profilePhoto, 3);
                        Cursor userCursor = queryUser(getApplicationContext(), new String[]{"userID"}, "username=?", new String[]{username});
                        int userID = -1;
                        while (userCursor.moveToNext()) {
                            userID = userCursor.getInt(0);
                        }
                        //建住所
                        String houseName = username + "的家";
                        createHouse(getApplicationContext(), houseName, userID);
                        Cursor houseCursor = queryHouse(getApplicationContext(), new String[]{"houseID"}, "userID=?", new String[]{String.valueOf(userID)});
                        int houseID = -1;
                        while (houseCursor.moveToNext()) {
                            houseID = houseCursor.getInt(0);
                        }
                        //建账号
                        for (String account : accountName) {
                            createAccount(getApplicationContext(), account, houseID);

                        }
                        //建房间
                        for (String room : roomName) {
                            createRoom(getApplicationContext(), room, houseID, userID);
                        }
                        //建家具
                        Cursor roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"客厅", String.valueOf(houseID)}, null);
                        int roomID = -1;
                        while (roomCursor.moveToNext()) {
                            roomID = roomCursor.getInt(0);
                        }

                        for (String furniture : furnitureName1) {
                            createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                        }
                        roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"主卧", String.valueOf(houseID)}, null);
                        while (roomCursor.moveToNext()) {
                            roomID = roomCursor.getInt(0);
                        }
                        for (String furniture : furnitureName2) {
                            createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                        }
                        roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"次卧", String.valueOf(houseID)}, null);
                        while (roomCursor.moveToNext()) {
                            roomID = roomCursor.getInt(0);
                        }
                        for (String furniture : furnitureName3) {
                            createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                        }
                        roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"厨房", String.valueOf(houseID)}, null);
                        while (roomCursor.moveToNext()) {
                            roomID = roomCursor.getInt(0);
                        }
                        for (String furniture : furnitureName4) {
                            createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                        }
                        roomCursor = queryRoom(getApplicationContext(), new String[]{"roomID"}, "roomName=? AND houseID=?", new String[]{"卫生间", String.valueOf(houseID)}, null);
                        while (roomCursor.moveToNext()) {
                            roomID = roomCursor.getInt(0);
                        }
                        for (String furniture : furnitureName5) {
                            createFurniture(getApplicationContext(), furniture, userID, houseID, roomID);
                        }
                        //建分类
                        for (String category : categoryName) {
                            createCategory(getApplicationContext(), category, userID);
                        }
                        //建容器
                        for (String container : containerName) {
                            createContainer(getApplicationContext(), container, userID);
                        }

                        Intent intent = new Intent(edu.hebut.here.SignActivity.this, edu.hebut.here.MainActivity.class);
                        editor.putBoolean("main", true);
                        editor.putInt("userID", userID);
                        editor.putInt("houseID", houseID);
                        editor.apply();
                        startActivity(intent);
                        edu.hebut.here.SignActivity.this.finish();
                    }
                }
            }
        });

        log = findViewById(R.id.btn_log);
        log.setOnClickListener(view -> {
            Intent intent = new Intent(edu.hebut.here.SignActivity.this, LoginActivity.class);
            startActivity(intent);
            edu.hebut.here.SignActivity.this.finish();
        });
    }

    private void showDialog() {
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
            if (ContextCompat.checkSelfPermission(edu.hebut.here.SignActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(edu.hebut.here.SignActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            } else {
                // 如果权限已经申请过，直接进行图片选择
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 判断系统中是否有处理该 Intent 的 Activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                } else {
                    Toast.makeText(edu.hebut.here.SignActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_photo.setOnClickListener(v -> {
            // 权限申请
            if (ContextCompat.checkSelfPermission(edu.hebut.here.SignActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(edu.hebut.here.SignActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(edu.hebut.here.SignActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
            } else {
                // 权限已经申请，直接拍照
                dialog.dismiss();
                imageCapture();
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
                case REQUEST_SMALL_IMAGE_CUTTING:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                // 大图切割
                case REQUEST_BIG_IMAGE_CUTTING:
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    signProfilePhoto.setImageBitmap(bitmap);
                    break;
                // 相册选取
                case REQUEST_IMAGE_GET:
                    try {
                        // startSmallPhotoZoom(data.getData());
                        startBigPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    // startSmallPhotoZoom(Uri.fromFile(temp));
                    startBigPhotoZoom(temp);
            }
        }
    }

    /**
     * 处理权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(edu.hebut.here.SignActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    imageCapture();
                } else {
                    dialog.dismiss();
                }
                break;
        }
    }

    /**
     * 小图模式切割图片
     * 此方式直接返回截图后的 bitmap，由于内存的限制，返回的图片会比较小
     */
    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    /**
     * 判断系统及拍照
     */
    private void imageCapture() {
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
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     */
    public void startBigPhotoZoom(File inputFile) {
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
        intent.setDataAndType(getImageContentUri(edu.hebut.here.SignActivity.this, inputFile), "image/*");
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
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
    }

    public void startBigPhotoZoom(Uri uri) {
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
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
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
            signProfilePhoto.setImageBitmap(photo);
        }
    }
}