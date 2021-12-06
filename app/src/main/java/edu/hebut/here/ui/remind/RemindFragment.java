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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.alibaba.fastjson.JSON;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import edu.hebut.here.R;
import edu.hebut.here.activity.LoginActivity;
import edu.hebut.here.activity.MainActivity;
import edu.hebut.here.adapter.BarHouseAdapter;
import edu.hebut.here.entity.Account;
import edu.hebut.here.entity.House;
import edu.hebut.here.entity.LoginUser;
import edu.hebut.here.ui.search.SearchActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static edu.hebut.here.activity.AppContext.*;
import static edu.hebut.here.data.MyContentResolver.queryGoods;

public class RemindFragment extends Fragment {
    String servletBarHouseName = "HouseBarServlet";
    String servletAccountQuery = "AccountQueryServlet";
    String servletAccountUpdate = "AccountUpdateServlet";
    SharedPreferences sharedPreferences;
    TextView[] accountValue;
    Cursor goodsCursor;
    int houseID;
    int userID;
    TextView barHouseName;
    RecyclerView recyclerView;
    View root;

    @SuppressLint("HandlerLeak")
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case remindBarHouseName:
                    House houseRes = JSON.parseObject(String.valueOf(msg.obj), House.class);
                    barHouseName.setText(houseRes.getHouseName());
                    break;
                case remindAccountQuery:
                    List<Account> accountRes = JSON.parseArray(String.valueOf(msg.obj), Account.class);
                    accountValue = new TextView[]{root.findViewById(R.id.num_internetAccount)
                            , root.findViewById(R.id.num_electricAccount)
                            , root.findViewById(R.id.num_waterAccount)
                            , root.findViewById(R.id.num_gasAccount)
                            , root.findViewById(R.id.num_TVAccount)
                            , root.findViewById(R.id.num_oilAccount)};
                    for (int i=0; i<accountValue.length;i++) {
                        accountValue[i].setText(accountRes.get(i).getAccountValue());
                        int finalI = i;
                        accountValue[i].setOnLongClickListener(v -> {
                            try {
                                final ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                                ClipData textCd = ClipData.newPlainText("data", accountValue[finalI].getText());
                                clipboard.setPrimaryClip(textCd);
                                Toast.makeText(getActivity(), "您的账号已成功复制到粘贴板上", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        });
                    }
                    break;
                case remindAccountUpdate:
                    Log.e("Account:", String.valueOf(msg.obj));
                    if (Boolean.parseBoolean(String.valueOf(msg.obj))) {
                        Toast.makeText(getActivity(), "修改账号成功", Toast.LENGTH_SHORT).show();
                        queryAccountValue(houseID);
                    }
                    else {
                        Toast.makeText(requireContext(), "修改账号失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

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

        barHouseName = root.findViewById(R.id.bar_house_name);
        initTitleBarHouseName(houseID);

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

        //goodsCursor = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND isOvertime=1", new String[]{String.valueOf(userID)});
        TextView over = root.findViewById(R.id.num_overdue);
        over.setText(String.valueOf(initOvertimeSum()));

        //goodsCursor = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND isCloseOvertime=1", new String[]{String.valueOf(userID)});
        TextView closeOver = root.findViewById(R.id.num_closeOverdue);
        closeOver.setText(String.valueOf(initCloseOvertimeSum()));

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

        queryAccountValue(houseID);
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
                            updateAccountValue(temp, houseID, "宽带");
                            break;
                        case R.id.icon_editElectricAccount:
                            updateAccountValue(temp, houseID, "电表");
                            break;
                        case R.id.icon_editWaterAccount:
                            updateAccountValue(temp, houseID, "水表");
                            break;
                        case R.id.icon_editGasAccount:
                            updateAccountValue(temp, houseID, "燃气");
                            break;
                        case R.id.icon_editTVAccount:
                            updateAccountValue(temp, houseID, "有线电视");
                            break;
                        case R.id.icon_editOilAccount:
                            updateAccountValue(temp, houseID, "加油卡");
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

    public int initCloseOvertimeSum() {
        return 0;
    }

    public int initOvertimeSum() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsCursor = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND isOvertime=1", new String[]{String.valueOf(userID)});
        TextView over = root.findViewById(R.id.num_overdue);
        over.setText(String.valueOf(goodsCursor.getCount()));
        goodsCursor = queryGoods(requireContext(), new String[]{"goodsID"}, "userID=? AND isCloseOvertime=1", new String[]{String.valueOf(userID)});
        TextView closeOver = root.findViewById(R.id.num_closeOverdue);
        closeOver.setText(String.valueOf(goodsCursor.getCount()));
    }

    public void initTitleBarHouseName(int houseID) {
        OkHttpClient client = new OkHttpClient();
        LoginUser user = new LoginUser();
        user.setHouseID(houseID);
        //将对象转换为json字符串
        String json = JSON.toJSONString(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servletBarHouseName)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(requireContext(), "获取住所列表失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("resBarHouseName----", res);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendMessage(res, remindBarHouseName);
                    }
                }.start();
            }
        });
    }

    public void queryAccountValue(int houseID) {
        OkHttpClient client = new OkHttpClient();
        LoginUser user = new LoginUser();
        user.setHouseID(houseID);
        //将对象转换为json字符串
        String json = JSON.toJSONString(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servletAccountQuery)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(requireContext(), "获取账号信息失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("resQueryAccount----", res);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendMessage(res, remindAccountQuery);
                    }
                }.start();
            }
        });
    }
    public void updateAccountValue(String newAccountValue, int houseID, String accountName) {
        OkHttpClient client = new OkHttpClient();
        Account account = new Account();
        account.setAccountName(accountName);
        account.setAccountValue(newAccountValue);
        account.setHouseID(houseID);
        //将对象转换为json字符串
        String json = JSON.toJSONString(account);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(httpURL+servletAccountUpdate)//请求的url
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(requireContext(), "修改账号失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("resUpdateAccount----", res);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendMessage(res, remindAccountUpdate);
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
}