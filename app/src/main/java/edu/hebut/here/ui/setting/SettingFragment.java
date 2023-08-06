package edu.hebut.here.ui.setting;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;

import edu.hebut.here.R;
import edu.hebut.here.WelcomeActivity;
import edu.hebut.here.service.LongRunningService;
import edu.hebut.here.utils.BitmapUtils;
import edu.hebut.here.utils.ExcelUtils;

import static edu.hebut.here.data.MyContentResolver.queryUser;
import static edu.hebut.here.data.MyContentResolver.updateUserProfilePhoto;
import static edu.hebut.here.data.MyContentResolver.updateUserReminderTime;
import static edu.hebut.here.utils.IntentUtils.marketGrade;
import static edu.hebut.here.utils.IntentUtils.sendEmail;

public class SettingFragment extends Fragment {
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;
    private static final String IMAGE_FILE_NAME = "icon.jpg";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID, houseID;
    int reminderTime;
    ImageView profilePhoto;
    ListView settingNormal;
    ListView settingSecurity;
    ListView settingOther;
    byte[] profilePhotoByte;
    Dialog dialog;
    private Uri mImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        profilePhoto = root.findViewById(R.id.profilePhoto);
        sharedPreferences = requireContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
        Cursor userCursor = queryUser(requireContext(), new String[]{"profilePhoto", "username", "gender", "reminderTime"}, "userID=?", new String[]{String.valueOf(userID)});
        byte[] profilePhotoByte = null;
        String username = "";
        String gender = "";
        while (userCursor.moveToNext()) {
            profilePhotoByte = userCursor.getBlob(0);
            username = userCursor.getString(1);
            gender = userCursor.getString(2);
            reminderTime = userCursor.getInt(3);
        }
        assert profilePhoto != null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(profilePhotoByte, 0, profilePhotoByte.length);
        profilePhoto.setImageBitmap(bitmap);

        profilePhoto.setOnClickListener(v -> {
            showDialog();
        });

        TextView usernameText = root.findViewById(R.id.setting_username);
        usernameText.setText(username);
        ImageView genderText = root.findViewById(R.id.setting_gender);
        if (gender.equals("男")) {
            genderText.setImageResource(R.drawable.ic_male);
        } else {
            genderText.setImageResource(R.drawable.ic_female);
        }
        settingNormal = root.findViewById(R.id.setting_normal);
        settingSecurity = root.findViewById(R.id.setting_security);
        settingOther = root.findViewById(R.id.setting_other);
        settingNormal.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_remind_time, null);
                    EditText editText = dialogLayout.findViewById(R.id.edit_remind_time);
                    editText.setText(String.valueOf(reminderTime));
                    new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_edit).setTitle("更改提前提醒天数").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                        reminderTime = Integer.parseInt(editText.getText().toString());
                        updateUserReminderTime(getContext(), reminderTime, userID);
                        Intent service = new Intent(getContext(), LongRunningService.class);
                        getActivity().startService(service);
                        Toast.makeText(getActivity(), String.valueOf(reminderTime), Toast.LENGTH_LONG).show();//!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    })).setNegativeButton("取消", (dialog, which) -> {
                    }).create().show();
                    break;
                case 1:
                    Intent intent1 = new Intent(getActivity(), HouseActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(getActivity(), RoomActivity.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(getActivity(), FurnitureActivity.class);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent(getActivity(), ContainerActivity.class);
                    startActivity(intent4);
                    break;
                case 5:
                    Intent intent5 = new Intent(getActivity(), CategoryActivity.class);
                    startActivity(intent5);
                    break;
            }
        });
        settingSecurity.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    Intent intent0 = new Intent(getActivity(), PasswordActivity.class);
                    startActivity(intent0);
                    break;
                case 1:
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        Toast.makeText(getContext(), "请授予权限", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        final androidx.appcompat.app.AlertDialog.Builder alterDialog = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        alterDialog.setIcon(R.drawable.ic_close_overdue);//图标
                        alterDialog.setTitle("提示");//文字
                        alterDialog.setMessage("导出的文件不包含物品图片，是否继续？");
                        alterDialog.setPositiveButton("继续", (dialog, which) -> {
                            String fileName = ExcelUtils.sqlToExcel(userID, getContext());
                            Toast.makeText(getContext(), "导出成功！文件名为：" + fileName + "，请到手机根目录查看。", Toast.LENGTH_SHORT).show();
                        });
                        alterDialog.setNegativeButton("取消", (dialog, which) -> {
                        });
                        alterDialog.show();
                    }
                    break;
            }

        });
        settingOther.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    sendEmail(getContext());
                    break;
                case 1:
                    marketGrade(getActivity(), getContext());
                    break;
                case 2:
                    Intent intent3 = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent3);
                    break;
            }
        });

        Button exit = root.findViewById(R.id.exit);
        exit.setOnClickListener(v -> {
            if (sharedPreferences.getString("password", null) != null) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(getActivity());
                alterDialog.setIcon(R.drawable.ic_edit);//图标
                alterDialog.setTitle("警告");//文字
                alterDialog.setMessage("检测到您已设置应用密码，请先到设置中移除密码后再退出。");//提示消息
                alterDialog.setPositiveButton("确定", (dialog, which) -> {
                });
                alterDialog.show();
            } else {
                editor = sharedPreferences.edit();
                editor.putBoolean("main", false);
                editor.putInt("chance", 5);
                editor.apply();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return root;
    }

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        dialog = new Dialog(getContext(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = requireActivity().getWindowManager().getDefaultDisplay().getHeight();
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
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            } else {
                // 如果权限已经申请过，直接进行图片选择
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 判断系统中是否有处理该 Intent 的 Activity
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                } else {
                    Toast.makeText(requireActivity(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_photo.setOnClickListener(v -> {
            // 权限申请
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(requireActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调成功
        if (resultCode == requireActivity().RESULT_OK) {
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
                    profilePhoto.setImageBitmap(bitmap);

                    profilePhoto.setDrawingCacheEnabled(true);
                    Bitmap bitmap1 = profilePhoto.getDrawingCache();
                    profilePhotoByte = BitmapUtils.bitmapToByteArray(bitmap1);
                    profilePhoto.setDrawingCacheEnabled(false);
                    updateUserProfilePhoto(requireContext(), profilePhotoByte, userID);
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
                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(requireActivity(), "未找到图片查看器", Toast.LENGTH_SHORT).show();
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
            pictureUri = FileProvider.getUriForFile(requireActivity(),
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
        intent.setDataAndType(getImageContentUri(requireActivity(), inputFile), "image/*");
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
            profilePhoto.setImageBitmap(photo);
        }
    }
}