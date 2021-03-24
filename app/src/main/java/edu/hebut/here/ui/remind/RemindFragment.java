package edu.hebut.here.ui.remind;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hebut.here.R;

import static edu.hebut.here.data.MyContentResolver.*;


public class RemindFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView[] account;
    Cursor accountCursor;
    Cursor goodsCursor;
    int houseID;
    int userID;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remind, container, false);
        init(root);
        return root;
    }

    @SuppressLint("NonConstantResourceId")
    public void init(View root) {
        sharedPreferences = getContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        goodsCursor = queryGoods(getContext(), new String[]{"_id"}, "userID=? AND isOvertime=1",new String[]{String.valueOf(userID)});
        TextView over = root.findViewById(R.id.num_overdue);
        over.setText(String.valueOf(goodsCursor.getCount()));
        goodsCursor = queryGoods(getContext(), new String[]{"_id"}, "userID=? AND isCloseOvertime=1",new String[]{String.valueOf(userID)});
        TextView closeOver = root.findViewById(R.id.num_closeOverdue);
        closeOver.setText(String.valueOf(goodsCursor.getCount()));
        account = new TextView[]{root.findViewById(R.id.num_internetAccount)
                , root.findViewById(R.id.num_electricAccount)
                , root.findViewById(R.id.num_waterAccount)
                , root.findViewById(R.id.num_gasAccount)
                , root.findViewById(R.id.num_TVAccount)
                , root.findViewById(R.id.num_oilAccount)};
        sharedPreferences = getContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        houseID = sharedPreferences.getInt("houseID", -1);
        accountCursor = queryAccount(getContext(), new String[]{"accountValue"}, "houseID=?", new String[]{String.valueOf(houseID)});
        for (TextView textView : account) {
            if (accountCursor.moveToNext()) {
                textView.setText(accountCursor.getString(0));
            }
            textView.setOnLongClickListener(v -> {
                try {
                    final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                    ClipData textCd = ClipData.newPlainText("data", textView.getText());
                    clipboard.setPrimaryClip(textCd);
                    Toast.makeText(getActivity(), "您的账号已成功复制到粘贴板上", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            });
        }
        ImageView[] edit = {root.findViewById(R.id.icon_editInternetAccount)
                , root.findViewById(R.id.icon_editElectricAccount)
                , root.findViewById(R.id.icon_editWaterAccount)
                , root.findViewById(R.id.icon_editGasAccount)
                , root.findViewById(R.id.icon_editTVAccount)
                , root.findViewById(R.id.icon_editOilAccount)};
        for (ImageView imageView : edit) {
            imageView.setOnClickListener(v -> {
                TableLayout dialogLayout = (TableLayout) getLayoutInflater().inflate(R.layout.dialog_edit_account, null);
                new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_edit).setTitle("编辑账号").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                    EditText editText = dialogLayout.findViewById(R.id.edit_account);
                    String temp = editText.getText().toString();
                    switch (imageView.getId()) {
                        case R.id.icon_editInternetAccount:
                            updateAccountValue(getContext(), temp, houseID, "宽带");
                            account[0].setText(temp);
                            break;
                        case R.id.icon_editElectricAccount:
                            updateAccountValue(getContext(), temp, houseID, "电表");
                            account[1].setText(temp);
                            break;
                        case R.id.icon_editWaterAccount:
                            updateAccountValue(getContext(), temp, houseID, "水表");
                            account[2].setText(temp);
                            break;
                        case R.id.icon_editGasAccount:
                            updateAccountValue(getContext(), temp, houseID, "燃气");
                            account[3].setText(temp);
                            break;
                        case R.id.icon_editTVAccount:
                            updateAccountValue(getContext(), temp, houseID, "有线电视");
                            account[4].setText(temp);
                            break;
                        case R.id.icon_editOilAccount:
                            updateAccountValue(getContext(), temp, houseID, "加油卡");
                            account[5].setText(temp);
                            break;
                    }
                })).setNegativeButton("取消", (dialog, which) -> {

                }).create().show();
            });
        }
        FloatingActionButton addGoods = root.findViewById(R.id.add_goods);
        addGoods.setOnClickListener(v -> {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
            startActivity(intent);
        });

    }
}