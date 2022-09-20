package com.github.catvod.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.catvod.spider.Fuli;
import com.github.catvod.spider.XPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomActivity {
    private static String TAG = "CustomActivity";
    private static  CustomActivity instance;
    public static CustomActivity GetInstance()
    {
        if (instance == null)
        {
            instance = new CustomActivity();
        }
        return instance;
    }

    public void Create(Activity activity, Bundle savedInstanceState) {
        if (true){
//            String url = "http://a.mayishidai.cn:7080/tv/custom/sites/csp_XPath/mrnjz.json";
//            String url = "http://a.mayishidai.cn:7080/tv/custom/sites/csp_XPath/485ck.json"; //黄仓
//            String url = "http://a.mayishidai.cn:7080/tv/custom/sites/csp_XPath/wzk666.json"; //绅士
//            String url = "http://a.mayishidai.cn:7080/tv/custom/sites/csp_XPath/kmff22.json"; //快喵
            String url = "http://a.mayishidai.cn:7080/tv/custom/sites/csp_XPath/uu_zz.json"; //柚子
            XPath aidi = new XPath();
//            Fuli aidi = new Fuli();
            aidi.init(activity, url);
            Log.e(TAG, "获取主页内容: " + aidi.homeContent(true));//获取主页内容
            Log.e(TAG, "获取主页视频内容: " + aidi.homeVideoContent());//获取主页视频内容
            HashMap<String, String> filterMap = new HashMap<>();
            filterMap.put("area","");
            filterMap.put("year","");
            filterMap.put("state","");
            filterMap.put("order","");
            Log.e(TAG, "获取分页内容: " + aidi.categoryContent("1", "1", true, filterMap));//获取分页内容
//            Log.e(TAG, "获取分页内容: " + aidi.categoryContent("home/japan", "2", true, null));
//            String videoId = "157402";
//            String videoId = "113642";//绅士
//            String videoId = "113642";//快喵
            String videoId = "/z/?328792.html";//柚子
            List<String> ids = new ArrayList<String>();
            ids.add(videoId);
            Log.e(TAG, "获取视频页描述: " + aidi.detailContent(ids));//获取视频页描述
            Log.e(TAG, "获取播放内容: " + aidi.playerContent("", "/p/?328792-0-0.html", new ArrayList<>()));//播放内容
            Log.e(TAG, "获取搜索内容: " + aidi.searchContent("陪你一起", false));//搜索内容
        }
    }
}
