package com.example.fangyukui.wynew.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.fangyukui.wynew.R;
import com.example.fangyukui.wynew.news.bean.HotDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;




public class HotAdapter extends BaseAdapter {
    private final ImageLoader imageLoader;
    ArrayList<HotDetail> mHotDetails;
    LayoutInflater mInflater;
    //这个类的显示的配置类
    DisplayImageOptions mOptions;
    Context mContext;

    public HotAdapter(ArrayList<HotDetail> hotDetails, Context context) {
        mHotDetails = hotDetails;
        mContext = context;
        mInflater = LayoutInflater.from(context);

        this.imageLoader = ImageLoader.getInstance(); // Get singleton instance

        //建造者模式 -> 创建一个复杂的对象
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    @Override
    public int getCount() {
        return mHotDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mHotDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder;

        HotDetail detail = mHotDetails.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_hot, null);

            hoder = new ViewHoder();
            hoder.icon = (ImageView) convertView.findViewById(R.id.img);
            hoder.title = (TextView) convertView.findViewById(R.id.title);
            hoder.source = (TextView) convertView.findViewById(R.id.source);
            hoder.reply_count = (TextView) convertView.findViewById(R.id.reply_count);
            hoder.special = (TextView) convertView.findViewById(R.id.special);

            convertView.setTag(hoder);
        } else {
            hoder = (ViewHoder) convertView.getTag();
        }

        initViews(hoder, detail);
        return convertView;
    }

    public void initViews(ViewHoder hoder, HotDetail detail) {

        hoder.title.setText(detail.getTitle());
        hoder.source.setText(detail.getSource());
        hoder.reply_count.setText(detail.getReplyCount() + "跟帖");


        String img = detail.getImg() == null ? "http://bjnewsrec-cv.ws.126.net/little199d22f80694dbe57c4201be76cb6bdb58d.jpg":detail.getImg();
        //显示图片的方法
//        imageLoader.displayImage(img, hoder.icon, null, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                Log.i("it520","onLoadingStarted");
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                Log.i("it520","onLoadingComplete");
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        },new ImageLoadingProgressListener() {
//            @Override
//            public void onProgressUpdate(String imageUri, View view, int current, int total) {
//
//            }
//        });


        Glide.with(mContext).
                load(img)
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .into(hoder.icon);


    }

    class ViewHoder {
        ImageView icon;
        TextView title;
        TextView source;
        TextView reply_count;
        TextView special;
    }
}
