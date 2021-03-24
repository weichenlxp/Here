package edu.hebut.here.ui.space;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import edu.hebut.here.R;
import edu.hebut.here.room.Kitchen;
import edu.hebut.here.room.Livingroom;
import edu.hebut.here.room.MainBedroom;
import edu.hebut.here.room.SideBedroom;
import edu.hebut.here.room.Toilet;

public class SpaceFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewpager;
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    String[] room = {"客厅","主卧","次卧","厨房","卫生间"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_space, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewpager = root.findViewById(R.id.viewPager);
        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fragment中嵌套fragment, Manager需要用(getChildFragmentManager())
        MPagerAdapter mPagerAdapter = new MPagerAdapter(getChildFragmentManager());
        fragmentList.add(new Livingroom());
        fragmentList.add(new MainBedroom());
        fragmentList.add(new SideBedroom());
        fragmentList.add(new Kitchen());
        fragmentList.add(new Toilet());
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(mPagerAdapter);
    }

    class MPagerAdapter extends FragmentPagerAdapter {

        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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
        public CharSequence getPageTitle(int position) {
            return room[position];
        }
    }
}