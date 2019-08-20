package com.example.fangyukui.wynew.news.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fangyukui.wynew.R;
import com.example.fangyukui.wynew.news.adapter.NewAdapter;
import com.example.fangyukui.wynew.news.bean.FragmentInfo;
import com.example.fangyukui.wynew.news.news_inner.HotFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;


public class NewsFragment extends Fragment {

    ArrayList<FragmentInfo> pages;
    NewAdapter mNewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pages = new ArrayList<>();

        FrameLayout layout =  getActivity().findViewById(R.id.tabs);
        //  R.layout.include_tab 布局里面只有 SmartTabLayout 这个布局
        layout.addView(View.inflate(getActivity(),R.layout.include_tab,null));



        SmartTabLayout smartTabLayout =  getActivity().findViewById(R.id.smart_tab);
        ViewPager viewPager =  getActivity().findViewById(R.id.viewpager22);

        String[] titles = getResources().getStringArray(R.array.news_titles);
        for(int i =0;i<titles.length;i++){
            FragmentInfo info;
            if(i==0){
                info= new FragmentInfo(new HotFragment(),titles[i]);
            }else{
                info= new FragmentInfo(new EmptyFragment(),titles[i]);
            }
            pages.add(info);
        }
        mNewAdapter = new NewAdapter(getFragmentManager(),pages);
        viewPager.setAdapter(mNewAdapter);

        //!!!!关键代码,自动绑定数据
        smartTabLayout.setViewPager(viewPager);




    }

}
