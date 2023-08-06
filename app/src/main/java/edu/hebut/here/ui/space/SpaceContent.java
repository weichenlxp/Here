package edu.hebut.here.ui.space;

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
import edu.hebut.here.adapter.SpaceFurnitureAdapter;

import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class SpaceContent extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    ConstraintLayout constraintLayout;
    int userID, houseID;
    String roomPosition;
    private boolean isFirstLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_space_content, container, false);
        sharedPreferences = requireContext().getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
        constraintLayout = root.findViewById(R.id.spaceNone);
        constraintLayout.setVisibility(View.GONE);
        Cursor roomCursor = queryRoom(getContext(), new String[]{"roomID"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
        roomCursor.moveToPosition(Integer.parseInt(roomPosition));
        int roomID = roomCursor.getInt(0);
        Cursor furnitureCursor = queryFurniture(getContext(), null, "userID=? AND roomID=?", new String[]{String.valueOf(userID), String.valueOf(roomID)});
        if (furnitureCursor.getCount()==0) {
            constraintLayout.setVisibility(View.VISIBLE);
        }
        recyclerView = root.findViewById(R.id.furnitureRV);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SpaceFurnitureAdapter(getActivity(), roomPosition, this));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            recyclerView.setAdapter(new SpaceFurnitureAdapter(getActivity(), roomPosition, this));
        }
        isFirstLoading = false;
    }

    public void setRoomPosition(String roomPosition) {
        this.roomPosition = roomPosition;
    }
}