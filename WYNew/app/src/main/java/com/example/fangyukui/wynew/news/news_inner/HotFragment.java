package com.example.fangyukui.wynew.news.news_inner;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fangyukui.wynew.R;
import com.example.fangyukui.wynew.news.adapter.BannerAdapter;
import com.example.fangyukui.wynew.news.adapter.HotAdapter;
import com.example.fangyukui.wynew.news.bean.Banner;
import com.example.fangyukui.wynew.news.bean.Hot;
import com.example.fangyukui.wynew.news.bean.HotDetail;
import com.example.fangyukui.wynew.splash.util.Constant;
import com.example.fangyukui.wynew.splash.util.HttpRespon;
import com.example.fangyukui.wynew.splash.util.HttpUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class HotFragment extends Fragment implements ViewPager.OnPageChangeListener {
    ListView mListView;
    //放置轮播图
    ArrayList<Banner> mBanners;
    ArrayList<HotDetail> mHotDetails;
    ArrayList<View> views;
    ArrayList<ImageView> dot_imgs;

    MyHandler mHandler;
    HotAdapter adapter;
    LayoutInflater inflater;


    //刷新数据成功
    private final static int INIT_SUCCESS = 0;
    //轮播图->相关的控件
    ViewPager viewpager;
    BannerAdapter bAdapter;
    TextView bannerTitle;
    LinearLayout dots;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_hot,container,false);
        mListView = (ListView) view.findViewById(R.id.listView);
        return  view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBanners = new ArrayList<>();
        mHotDetails = new ArrayList<>();
        views = new ArrayList<>();
        dot_imgs = new ArrayList<>();


        mHandler = new MyHandler(this);

        inflater = LayoutInflater.from(getActivity());
        View head = inflater.inflate(R.layout.include_banner, null);
//        //将轮播图控件加入listview
        mListView.addHeaderView(head);
        viewpager = (ViewPager) head.findViewById(R.id.viewpager);
        viewpager.addOnPageChangeListener(this);
        bannerTitle = (TextView) head.findViewById(R.id.title);
        dots = (LinearLayout)head.findViewById(R.id.dots);




        HttpUtil util = HttpUtil.getInstance();
        util.getDate(Constant.HOT_URL, new HttpRespon<Hot>(Hot.class) {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onSuccess(Hot hot) {

                if (null != hot && null != hot.getT1348647909107()) {
                    List<HotDetail> details = hot.getT1348647909107();
                    //取出第0位包含轮播图的数据
                    HotDetail tmp_baner = details.get(0);
                    List<Banner> banners = tmp_baner.getAds();
                    mBanners.addAll(banners);
                    //获取轮播图片成功

                    //删除轮播图片数据
                    details.remove(0);
                    mHotDetails.addAll(details);
                    //列表数据加载完成

                    //异步线程无法更改UI
                    mHandler.sendEmptyMessage(INIT_SUCCESS);
                }
            }
        });



    }

    public void end(String end){
        Log.i("it520","end");
    }


    //处理listView的数据
    public void initDate() {
        adapter = new HotAdapter(mHotDetails, getActivity());
        mListView.setAdapter(adapter);
    }

    public void initBanner() {
        if (null != mBanners && mBanners.size() > 0) {
            for (int i = 0; i < mBanners.size(); i++) {
                View view = inflater.inflate(R.layout.item_banner, null);
                views.add(view);

                ImageView dot = new ImageView(getActivity());
                dot.setImageResource(R.drawable.gray_dot);

                // 设置布局参数
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(0,0,10,0);
                // dots是LinearLayout，LinearLayout本质是ViewGroup
                dots.addView(dot,p);

                // dot_imgs存放ImageView类型的dot
                dot_imgs.add(dot);


            }
            bAdapter = new BannerAdapter(views,mBanners);
            viewpager.setAdapter(bAdapter);


            int half = Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2)%mBanners.size();
            viewpager.setCurrentItem(half);

            //设置默认显示的数据
            setImageDot(0);
            setBannerTitle(0);

        }


    }

    public void setImageDot(int index){
        int size = dot_imgs.size();
        int realPosition = index%size;
        for(int i = 0;i<size;i++){
            ImageView dot = dot_imgs.get(i);
            if(i== realPosition){
                dot.setImageResource(R.drawable.white_dot);
            }else{
                dot.setImageResource(R.drawable.gray_dot);
            }
        }
    }

    public void setBannerTitle(int index){
        int size = dot_imgs.size();
        int realPosition = index%size;
        //显示默认数据
        bannerTitle.setText(mBanners.get(realPosition).getTitle());
    }

    static class MyHandler extends Handler {
        WeakReference<HotFragment> weak_fragment;

        public MyHandler(HotFragment fragment) {
            this.weak_fragment = new WeakReference(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HotFragment hot = weak_fragment.get();
            if (hot == null) {
                return;
            }
            switch (msg.what) {
                case INIT_SUCCESS:
                    hot.initDate();
                    hot.initBanner();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        setImageDot(position);
//        setBannerTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
