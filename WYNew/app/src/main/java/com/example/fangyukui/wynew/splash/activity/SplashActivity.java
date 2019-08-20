package com.example.fangyukui.wynew.splash.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.example.fangyukui.wynew.MainActivity;
import com.example.fangyukui.wynew.splash.OnTimeClickListener;
import com.example.fangyukui.wynew.splash.TimeView;
import com.example.fangyukui.wynew.splash.bean.Action;
import com.example.fangyukui.wynew.splash.bean.Ads;
import com.example.fangyukui.wynew.splash.bean.AdsDetail;
import com.example.fangyukui.wynew.splash.server.DowloadImageService;
import com.example.fangyukui.wynew.splash.util.Constant;
import com.example.fangyukui.wynew.splash.util.ImageUtil;
import com.example.fangyukui.wynew.splash.util.JsonUtil;
import com.example.fangyukui.wynew.splash.util.Md5Helper;
import com.example.fangyukui.wynew.splash.util.SharePrenceUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fangyukui.wynew.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SplashActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    //广告图片
    ImageView ads_img;
    //倒计时
    TimeView time;

    MyHanlder mHandler;

    int length = 2 * 1000;
    int space = 250;
    int now = 0;
    int total;

    //json 缓存
    static final String JSON_CACHE = "ads_Json";
    static final String JSON_CACHE_TIME_OUT = "ads_Json_time_out";
    static final String JSON_CACHE_LAST_SUCCESS = "ads_Json_last";
    static final String LAST_IMAGE_INDEX ="img_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //7.0~2.0
        //开启全屏的设置
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //4.4 沉浸式
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        setContentView(R.layout.activity_splash);

        initView();

        getAds();

        showImage();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(reshRing);
    }



    //判断是否需要http请求
    public void getAds(){
        String cache =  SharePrenceUtil.getString(this,JSON_CACHE);
        if(TextUtils.isEmpty(cache)){
            httpRequest();
        }else{
            int time_Out = SharePrenceUtil.getInt(this,JSON_CACHE_TIME_OUT);
            long now  = System.currentTimeMillis();
            long last = SharePrenceUtil.getLong(this,JSON_CACHE_LAST_SUCCESS);
            if((now-last)>time_Out*60*1000 ){
                httpRequest();
            }
        }
    }



    public void httpRequest()  {
        Request request = new Request.Builder()
                .url(Constant.SPLASH_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(SplashActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;
                    }


//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }

//                    System.out.println(responseBody.string());

//                    Log.v("520it",responseBody.string());

                    String data = responseBody.string();

                    Ads ads =  JsonUtil.parseJson(data, Ads.class);

                    if (null != ads){
                        Log.d("520it","" + ads);

                        //http成功后,缓存json
                        SharePrenceUtil.saveString(SplashActivity.this,JSON_CACHE,data);
                        //http成功后,缓存超时时间
                        SharePrenceUtil.saveInt(SplashActivity.this,JSON_CACHE_TIME_OUT,ads.getNext_req());
                        //http成功后,缓存上次请求成功的时间
                        SharePrenceUtil.saveLong(SplashActivity.this,JSON_CACHE_LAST_SUCCESS,System.currentTimeMillis());


                        Intent i = new Intent();
                        i.setClass(SplashActivity.this, DowloadImageService.class);
                        i.putExtra(DowloadImageService.ADS_DATE,ads);
                        startService(i);



                    }else {

                    }




                }

            }
        });
    }


    public void showImage(){
        //读出缓存
        String cache =  SharePrenceUtil.getString(this,JSON_CACHE);
        if(!TextUtils.isEmpty(cache)){


            //只有显示了广告图的情况下才显示倒数控件
            time.setVisibility(View.VISIBLE);
            mHandler.post(reshRing);


            //读出这次显示的图片的角标
            int index = SharePrenceUtil.getInt(this,LAST_IMAGE_INDEX);
            //转化成对象
            Ads ads =  JsonUtil.parseJson(cache, Ads.class);
            int size = ads.getAds().size();

            if(null==ads){
                return;
            }

            List<AdsDetail> adsDetails = ads.getAds();
            if(null!=adsDetails && adsDetails.size()>0){
                final AdsDetail detail =  adsDetails.get(index%size);
                List<String> urls = detail.getRes_url();
                if(urls!=null&&!TextUtils.isEmpty(urls.get(0))){
                    //获取到url
                    String url = urls.get(0);
                    //计算出文件名
                    String image_name =  Md5Helper.toMD5(url);

                    File image = ImageUtil.getFileByName(image_name);
                    if(image.exists()){
                        Bitmap bitmap = ImageUtil.getImageBitMapByFile(image);
                        ads_img.setImageBitmap(bitmap);
                        //指向下一张图片
                        index++;
                        SharePrenceUtil.saveInt(this,LAST_IMAGE_INDEX,index);

                        ads_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Action action =  detail.getAction_params();
                                //如果h5的数据是空的或者没有广告页面,我们不跳转
                                if(action!=null&&!TextUtils.isEmpty(action.getLink_url())){

                                    Intent intent = new Intent();
                                    intent.setClass(SplashActivity.this,WebViewActivity.class);
                                    intent.putExtra(WebViewActivity.ACTION_NAME,action);
                                    startActivity(intent);

                                    finish();
                                    //跳转到web广告页面后也要把倒数清空
                                    mHandler.removeCallbacks(reshRing);

                                }
                            }
                        });
                    }
                }
            }
        }
        else{
            //没有缓存,显示不了图片,3秒后跳转到首页
            mHandler.postDelayed(NoPhotoGotoMain,3000);
        }

    }


    //初始化各个控件
    public void initView() {
        ads_img =  findViewById(R.id.ads);
        time = findViewById(R.id.time);
        time.setVisibility(View.GONE);
        time.setListener(new OnTimeClickListener() {
            @Override
            public void onClickTime(View view) {
                //直接跳转到MainAcitivy
                //我们选择跳过按钮后,就应该把定时去除
                mHandler.removeCallbacks(reshRing);
                gotoMain();

            }
        });

        //刷新的次数
        total = length / space;

        //内存泄漏
        //Logger->uithread

        mHandler = new MyHanlder(this);
    }


    Runnable NoPhotoGotoMain  = new Runnable() {
        @Override
        public void run() {
            gotoMain();
        }
    };

    //圆环控件的计时
    Runnable reshRing = new Runnable() {
        @Override
        public void run() {
//            //重新新建
//            Message message = new Message();
            //消息池中复用
            Message message = mHandler.obtainMessage(0);
            message.arg1 = now;
            mHandler.sendMessage(message);
            mHandler.postDelayed(this, space);
            now++;
        }
    };

    public void gotoMain() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    //1 使用静态内部类切断访问activcity
    static class MyHanlder extends Handler {

        //强应用
        //弱引用 jvm是无法保证他的存活
        //软应用

        //2  使用弱引用持有对象
        WeakReference<SplashActivity> activity;

        public MyHanlder(SplashActivity act) {
            this.activity = new WeakReference<SplashActivity>(act);

        }

        @Override
        public void handleMessage(Message msg) {

            //获取对象,如果对象被回收
            SplashActivity act = activity.get();
            //
            if (act == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    int now = msg.arg1;
                    if (now <= act.total) {
                        act.time.setProgess(act.total, now);
                    } else {
                        this.removeCallbacks(act.reshRing);
                        act.gotoMain();
                    }
                    Log.i("it520", "handleMessage");
                    break;

            }

        }
    }




}
