package com.example.fangyukui.wynew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.fangyukui.wynew.news.fragment.EmptyFragment;
import com.example.fangyukui.wynew.news.fragment.NewsFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tab_Host);

        //获取tab的标题
        String[] titles = getResources().getStringArray(R.array.tab_title);
        //背景图
        int [] icons = new int[]{R.drawable.news_selector,R.drawable.reading_selector,R.drawable.video_selector,R.drawable.topic_selector,R.drawable.mine_selector};
        Class[] classes = new Class[]{NewsFragment.class, EmptyFragment.class,EmptyFragment.class,EmptyFragment.class,EmptyFragment.class};
        //1 绑定 ->fragment显示的容器
        tabHost.setup(this,getSupportFragmentManager(),R.id.content);

        for(int i=0;i<titles.length;i++){
            TabHost.TabSpec tmp =  tabHost.newTabSpec(""+i);
            tmp.setIndicator(getEveryView(this,titles,icons,i));
            tabHost.addTab(tmp,classes[i],null);
        }

        //监控这个FragmentTabHost的切换
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("it520","tabId = "+tabId);
            }
        });
//        //设置选中的页面,tag
//        tabHost.setCurrentTabByTag("1");




    }

    public View getEveryView(Context context, String[] titles, int[] icons, int index){
        LayoutInflater inflater = LayoutInflater.from(context);
        View title_view = inflater.inflate(R.layout.item_title,null);
        TextView title = (TextView) title_view.findViewById(R.id.title);
        ImageView icon = (ImageView) title_view.findViewById(R.id.icon);
        // 设置标签的内容
        title.setText(titles[index]);
        icon.setImageResource(icons[index]);
        return title_view;
    }



}
