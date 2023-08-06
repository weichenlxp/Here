package edu.hebut.here.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import edu.hebut.here.ui.pack.PackContent;

import static edu.hebut.here.data.MyContentResolver.queryContainer;

public class PackContainerAdapter extends FragmentPagerAdapter {
    Fragment currentFragment;
    SharedPreferences sharedPreferences;
    int userID;
    ArrayList<PackContent> fragmentList = new ArrayList<PackContent>();
    String[] containerName;
    private Context mContext;

    public PackContainerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        sharedPreferences = this.mContext.getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        Cursor containerCursor = queryContainer(this.mContext, new String[]{"containerName", "containerID"}, "userID=?", new String[]{String.valueOf(userID)});
        String[] temp = new String[containerCursor.getCount()];
        for (int i = 0; containerCursor.moveToNext(); i++) {
            temp[i] = containerCursor.getString(0);
        }
        containerName = temp;

        for (int i = 0; i < containerName.length; i++) {
            fragmentList.add(new PackContent());
        }
    }

    @Override
    public Fragment getItem(int position) {
        fragmentList.get(position).setContainerPosition(String.valueOf(position));
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
        return containerName[position];
    }
}
