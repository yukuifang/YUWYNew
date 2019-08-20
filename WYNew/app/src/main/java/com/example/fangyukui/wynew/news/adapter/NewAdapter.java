package com.example.fangyukui.wynew.news.adapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.fangyukui.wynew.news.bean.FragmentInfo;
import java.util.ArrayList;




public class NewAdapter extends FragmentStatePagerAdapter {
    ArrayList<FragmentInfo> mFragments;

    public NewAdapter(FragmentManager fm, ArrayList<FragmentInfo> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getmFragment();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  mFragments.get(position).getTitle();
    }
}
