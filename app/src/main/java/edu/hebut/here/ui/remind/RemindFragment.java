package edu.hebut.here.ui.remind;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.hebut.here.R;
import edu.hebut.here.adapter.BarHouseAdapter;
import edu.hebut.here.ui.search.SearchActivity;

import static edu.hebut.here.data.MyContentResolver.queryAccount;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.updateAccountValue;


public class RemindFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView[] account;
    Cursor houseCursor;
    Cursor accountCursor;
    Cursor goodsCursorHome;
    Cursor goodsCursorContainer;
    int houseID;
    int userID;
    RecyclerView recyclerView;
    View root;

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_remind, container, false);
        init(root);
        return root;
    }

    @SuppressLint("NonConstantResourceId")
    public void init(View root) {
        sharedPreferences = requireContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);

        houseCursor = queryHouse(requireContext(), new String[]{"houseName"}, "houseID=?", new String[]{String.valueOf(houseID)});
        TextView barHouseName = root.findViewById(R.id.bar_house_name);
        while (houseCursor.moveToNext()) {
            barHouseName.setText(houseCursor.getString(0));
        }
        ConstraintLayout barHouse = root.findViewById(R.id.bar_house_layout);
        ImageView barHouseUnfold = root.findViewById(R.id.bar_house_unfold);
        Drawable up = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up);
        Drawable down = ContextCompat.getDrawable(requireContext(), R.drawable.ic_down);
        View changeHouse = LayoutInflater.from(requireContext()).inflate(R.layout.change_house, null, false);
        final PopupWindow popWindow = new PopupWindow(changeHouse,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v1, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        popWindow.setOnDismissListener(() -> barHouseUnfold.setImageDrawable(down));

        barHouse.setOnClickListener(v -> {
            popWindow.showAsDropDown(v, 0, 0);
            barHouseUnfold.setImageDrawable(up);
        });

        //获取列表控件
        recyclerView = changeHouse.findViewById(R.id.house_list);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        recyclerView.setAdapter(new BarHouseAdapter(getContext(), getActivity()));

        ImageView barSearch = root.findViewById(R.id.bar_search);
        barSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            getActivity().startActivity(intent);
        });
        goodsCursorHome = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND houseID=? AND packed=? AND isOvertime=1", new String[]{String.valueOf(userID), String.valueOf(houseID), String.valueOf(0)});
        goodsCursorContainer = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND packed=? AND isOvertime=1", new String[]{String.valueOf(userID), String.valueOf(1)});
        TextView over = root.findViewById(R.id.num_overdue);
        over.setText(String.valueOf(goodsCursorHome.getCount()+goodsCursorContainer.getCount()));
        goodsCursorHome = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND houseID=? AND packed=? AND isCloseOvertime=1", new String[]{String.valueOf(userID), String.valueOf(houseID), String.valueOf(0)});
        goodsCursorContainer = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND packed=? AND isCloseOvertime=1", new String[]{String.valueOf(userID), String.valueOf(1)});
        TextView closeOver = root.findViewById(R.id.num_closeOverdue);
        closeOver.setText(String.valueOf(goodsCursorHome.getCount()+goodsCursorContainer.getCount()));

        over.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OverdueActivity.class);
            intent.setAction("overdue");
            intent.putExtra("isOverdue", "1");
            intent.putExtra("num", over.getText());
            startActivity(intent);
        });

        closeOver.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OverdueActivity.class);
            intent.setAction("overdue");
            intent.putExtra("isOverdue", "0");
            intent.putExtra("num", closeOver.getText());
            startActivity(intent);
        });

        account = new TextView[]{root.findViewById(R.id.num_internetAccount)
                , root.findViewById(R.id.num_electricAccount)
                , root.findViewById(R.id.num_waterAccount)
                , root.findViewById(R.id.num_gasAccount)
                , root.findViewById(R.id.num_TVAccount)
                , root.findViewById(R.id.num_oilAccount)};
        accountCursor = queryAccount(requireContext(), new String[]{"accountValue"}, "houseID=?", new String[]{String.valueOf(houseID)});
        for (TextView textView : account) {
            if (accountCursor.moveToNext()) {
                textView.setText(accountCursor.getString(0));
            }
            textView.setOnLongClickListener(v -> {
                try {
                    final ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
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
                ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_edit_account, null);
                new AlertDialog.Builder(getContext()).setIcon(R.drawable.ic_edit).setTitle("编辑账号").setView(dialogLayout).setPositiveButton("保存", ((dialog, which) -> {
                    EditText editText = dialogLayout.findViewById(R.id.edit_account);
                    String temp = editText.getText().toString();
                    switch (imageView.getId()) {
                        case R.id.icon_editInternetAccount:
                            updateAccountValue(requireContext(), temp, houseID, "宽带");
                            account[0].setText(temp);
                            break;
                        case R.id.icon_editElectricAccount:
                            updateAccountValue(requireContext(), temp, houseID, "电表");
                            account[1].setText(temp);
                            break;
                        case R.id.icon_editWaterAccount:
                            updateAccountValue(requireContext(), temp, houseID, "水表");
                            account[2].setText(temp);
                            break;
                        case R.id.icon_editGasAccount:
                            updateAccountValue(requireContext(), temp, houseID, "燃气");
                            account[3].setText(temp);
                            break;
                        case R.id.icon_editTVAccount:
                            updateAccountValue(requireContext(), temp, houseID, "有线电视");
                            account[4].setText(temp);
                            break;
                        case R.id.icon_editOilAccount:
                            updateAccountValue(requireContext(), temp, houseID, "加油卡");
                            account[5].setText(temp);
                            break;
                    }
                })).setNegativeButton("取消", (dialog, which) -> {

                }).create().show();
            });
        }
        FloatingActionButton addGoods = root.findViewById(R.id.add_goods);
        addGoods.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsCursorHome = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND houseID=? AND packed=0 AND isOvertime=1", new String[]{String.valueOf(userID), String.valueOf(houseID)});
        goodsCursorContainer = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND packed=1 AND isOvertime=1", new String[]{String.valueOf(userID)});
        TextView over = root.findViewById(R.id.num_overdue);
        over.setText(String.valueOf(goodsCursorHome.getCount()+goodsCursorContainer.getCount()));
        goodsCursorHome = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND houseID=? AND packed=0 AND isCloseOvertime=1", new String[]{String.valueOf(userID), String.valueOf(houseID)});
        goodsCursorContainer = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND packed=1 AND isCloseOvertime=1", new String[]{String.valueOf(userID)});
        TextView closeOver = root.findViewById(R.id.num_closeOverdue);
        closeOver.setText(String.valueOf(goodsCursorHome.getCount()+goodsCursorContainer.getCount()));
    }
}