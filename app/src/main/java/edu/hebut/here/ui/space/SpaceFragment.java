package edu.hebut.here.ui.space;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.hebut.here.R;
import edu.hebut.here.adapter.BarHouseAdapter;
import edu.hebut.here.adapter.SpaceRoomAdapter;

import static edu.hebut.here.data.MyContentResolver.queryHouse;

public class SpaceFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Cursor houseCursor;
    int houseID;
    int userID;
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ViewPager viewpager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_space, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewpager = root.findViewById(R.id.viewPager);

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

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editor = sharedPreferences.edit();
        editor.putInt("room_position", 0);
        editor.apply();
        // fragment中嵌套fragment, Manager需要用(getChildFragmentManager())
        SpaceRoomAdapter mPagerAdapter = new SpaceRoomAdapter(getChildFragmentManager(), getContext());

        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.getCurrentItem();
    }
}