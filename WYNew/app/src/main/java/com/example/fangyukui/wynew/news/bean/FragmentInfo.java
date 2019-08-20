package com.example.fangyukui.wynew.news.bean;

import androidx.fragment.app.Fragment;

public class FragmentInfo {
    Fragment mFragment;
    String title;

    public FragmentInfo(Fragment mFragment, String title) {
        this.mFragment = mFragment;
        this.title = title;
    }

    public Fragment getmFragment() {
        return mFragment;
    }

    public String getTitle() {
        return title;
    }

    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FragmentInfo{" +
                "mFragment=" + mFragment +
                ", title='" + title + '\'' +
                '}';
    }
}
