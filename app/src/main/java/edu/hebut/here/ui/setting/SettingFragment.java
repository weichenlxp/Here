package edu.hebut.here.ui.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.hebut.here.R;
import edu.hebut.here.WelcomeActivity;
import edu.hebut.here.data.MyContentResolver;

public class SettingFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID;
    int reminderTime;
    ListView settingNormal;
    ListView settingSecurity;
    ListView settingOther;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ImageView profilePhoto = root.findViewById(R.id.profilePhoto);
        sharedPreferences= requireContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID",-1);
        Cursor userCursor = MyContentResolver.queryUserByUserID(requireContext(), userID, new String[]{"profilePhoto","username","gender","reminderTime"});
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

        TextView usernameText  = root.findViewById(R.id.setting_username);
        usernameText.setText(username);
        ImageView genderText = root.findViewById(R.id.setting_gender);
        if (gender.equals("男")){
            genderText.setImageResource(R.drawable.ic_male);
        }
        else {
            genderText.setImageResource(R.drawable.ic_female);
        }
        settingNormal = root.findViewById(R.id.setting_normal);
        settingNormal.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i){
                case 0:
                    TableLayout dialogLayout = (TableLayout)getLayoutInflater().inflate(R.layout.dialog_edit_account, null);
                    EditText editText = dialogLayout.findViewById(R.id.edit_account);
                    editText.setText(String.valueOf(reminderTime));
                    new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_edit).setTitle("更改提前通知天数").setView(dialogLayout).setPositiveButton("保存",((dialog, which) -> {
                        int temp = Integer.parseInt(editText.getText().toString());
                        Toast.makeText(getActivity(),String.valueOf(temp),Toast.LENGTH_LONG).show();
                    })).setNegativeButton("取消",(dialog, which) -> {
                    }).create().show();
                    break;
                case 1:
                    Intent intent1 = new Intent(getActivity(), AddHouseActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent(getActivity(), AddRoomActivity.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 = new Intent(getActivity(), AddFurnitureActivity.class);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 = new Intent(getActivity(), AddContainerActivity.class);
                    startActivity(intent4);
                    break;
                case 5:
                    Intent intent5 = new Intent(getActivity(), AddCategoryActivity.class);
                    startActivity(intent5);
                    break;
            }

        });

        Button exit = root.findViewById(R.id.exit);
        exit.setOnClickListener(v -> {
            editor = sharedPreferences.edit();
            editor.putBoolean("main", false);
            editor.apply();
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        return root;
    }
}