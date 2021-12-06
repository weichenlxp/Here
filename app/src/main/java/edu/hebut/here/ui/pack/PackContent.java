package edu.hebut.here.ui.pack;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.hebut.here.R;
import edu.hebut.here.adapter.PackGoodsAdapter;

import static edu.hebut.here.data.MyContentResolver.queryContainer;
import static edu.hebut.here.data.MyContentResolver.queryGoods;

public class PackContent extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    ConstraintLayout constraintLayout;
    int userID, containerID;
    String containerPosition;
    private boolean isFirstLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pack_content, container, false);
        sharedPreferences = requireContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        constraintLayout = root.findViewById(R.id.packNone);
        constraintLayout.setVisibility(View.GONE);
        isNone();
        recyclerView = root.findViewById(R.id.goodsRV);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PackGoodsAdapter(getActivity(), containerPosition, this, getActivity()));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            isNone();
            recyclerView.setAdapter(new PackGoodsAdapter(getActivity(), containerPosition, this, getActivity()));
        }
        isFirstLoading = false;
    }

    public void setContainerPosition(String containerPosition) {
        this.containerPosition = containerPosition;
    }

    public void reSetAdapter(int count) {
        isNone();
        recyclerView.setAdapter(new PackGoodsAdapter(getActivity(), containerPosition, this, getActivity()));
    }

    public void isNone() {
        Cursor containerCursor = queryContainer(getContext(), new String[]{"containerID"}, "userID=?", new String[]{String.valueOf(userID)});
        containerCursor.moveToPosition(Integer.parseInt(containerPosition));
        containerID = containerCursor.getInt(0);

        Cursor goodsCursor = queryGoods(getContext(), null, "userID=? AND containerID=? AND packed=?", new String[]{String.valueOf(userID), String.valueOf(containerID), String.valueOf(1)});
        if (goodsCursor.getCount() == 0) {
            constraintLayout.setVisibility(View.VISIBLE);
        }
    }
}