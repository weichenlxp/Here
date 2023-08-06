package edu.hebut.here.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import edu.hebut.here.ui.space.SpaceContent;

import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class SpaceRoomAdapter extends FragmentPagerAdapter {
    Fragment currentFragment;
    SharedPreferences sharedPreferences;
    int userID, houseID;
    ArrayList<SpaceContent> fragmentList = new ArrayList<SpaceContent>();
    String[] roomName;
    private Context mContext;

    public SpaceRoomAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        houseID = sharedPreferences.getInt("houseID", -1);
        Cursor roomCursor = queryRoom(this.mContext, new String[]{"roomName", "roomID"}, "userID=? AND houseID=?", new String[]{String.valueOf(userID), String.valueOf(houseID)}, null);
        String[] temp = new String[roomCursor.getCount()];
        for (int i = 0; roomCursor.moveToNext(); i++) {
            temp[i] = roomCursor.getString(0);
        }
        roomName = temp;

        for (int i = 0; i < roomName.length; i++) {
            fragmentList.add(new SpaceContent());
        }
    }

    @Override
    public Fragment getItem(int position) {
        fragmentList.get(position).setRoomPosition(String.valueOf(position));
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return roomName[position];
    }
}
