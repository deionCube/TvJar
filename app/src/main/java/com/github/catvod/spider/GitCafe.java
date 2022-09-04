package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.utils.Misc;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitCafe extends Spider {
    private Context conText = null;
    private PushAgent pushAgent;
    private ArrayList<String> catList;
    private JSONObject filterConfig;
    private JSONArray allCat;


    @Override
    public void init(Context context, String extend) {
        super.init(context, extend);
        pushAgent = new PushAgent();
        pushAgent.init(context, extend);
        conText = context;
        try {
            filterConfig = new JSONObject("{\"hyds\":\"华语电视\",\"rhds\":\"日韩电视\",\"omds\":\"欧美电视\",\"qtds\":\"其他电视\",\"hydy\":\"华语电影\",\"rhdy\":\"日韩电影\",\"omdy\":\"欧美电影\",\"qtdy\":\"其他电影\",\"hydm\":\"华语动漫\",\"rhdm\":\"日韩动漫\",\"omdm\":\"欧美动漫\",\"jlp\":\"纪录片\",\"zyp\":\"综艺片\",\"jypx\":\"教育培训\",\"qtsp\":\"其他视频\",\"hyyy\":\"华语音乐\",\"rhyy\":\"日韩音乐\",\"omyy\":\"欧美音乐\",\"qtyy\":\"其他音乐\",\"kfrj\":\"娱乐软件\",\"xtrj\":\"系统软件\",\"wlrj\":\"网络软件\",\"bgrj\":\"办公软件\",\"qtrj\":\"其他软件\",\"mh\":\"漫画\",\"xs\":\"小说\",\"cbs\":\"出版书\",\"zspx\":\"知识培训\",\"qtwd\":\"其他文档\",\"bz\":\"壁纸\",\"rw\":\"人物\",\"fj\":\"风景\",\"qttp\":\"其他图片\",\"qt\":\"其他\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        catList = new ArrayList<>();
        catList.add("hydm");
        catList.add("hyds");
        catList.add("hydy");
        catList.add("omdm");
        catList.add("omds");
        catList.add("omdy");
        catList.add("rhdm");
        catList.add("rhds");
        catList.add("rhdy");
        catList.add("qtds");
        catList.add("qtdy");
        catList.add("qtsp");
        catList.add("jlp");
        catList.add("zyp");
        catList.add("hyyy");
        catList.add("rhyy");
        catList.add("omyy");
        catList.add("qtyy");
        catList.add("qt");
    }

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.54 Safari/537.36");
        return headers;
    }

    private JSONArray getCache(String key) {
        try {
            File dir = new File(conText.getCacheDir() + "/xzt/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File expiresFile = new File(conText.getCacheDir() + "/xzt/" + key + ".json.t");
            if (!expiresFile.exists()) {
                return null;
            }
            File file = new File(conText.getCacheDir() + "/xzt/" + key + ".json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(expiresFile), Misc.CharsetUTF8));
            if (System.currentTimeMillis() - Long.parseLong(bufferedReader.readLine().trim()) > 3600000) {
                bufferedReader.close();
                return null;
            }
            bufferedReader.close();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), Misc.CharsetUTF8));
            while (true) {
                String readLine = bufferedReader2.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    bufferedReader2.close();
                    return new JSONArray(sb.toString());
                }
            }
        } catch (Throwable unused) {
            return null;
        }
    }

    private void setCache(String key, String value) {
        try {
            File file = new File(conText.getCacheDir() + "/xzt/" + key + ".json.t");
            FileOutputStream fs = new FileOutputStream(conText.getCacheDir() + "/xzt/" + key + ".json");
            fs.write(value.getBytes(Misc.CharsetUTF8));
            fs.flush();
            fs.close();
            FileOutputStream fs2 = new FileOutputStream(file);
            fs2.write((System.currentTimeMillis() + "").getBytes(Misc.CharsetUTF8));
            fs2.flush();
            fs2.close();
        } catch (Throwable unused) {
        }
    }

    private JSONArray getCategory(String key) {
        try {
            JSONArray catCache = getCache(key);
            if (catCache != null && catCache.length() > 0) {
                return catCache;
            }
            String json = OkHttpUtil.string("https://gitcafe.net/alipaper/data/" + key + ".json?v=" + System.currentTimeMillis(), getHeaders());
            setCache(key, json);
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONArray getHome() {
        try {
            JSONArray catCache = getCache("home");
            if (catCache != null && catCache.length() > 0) {
                return catCache;
            }
            JSONArray json = new JSONObject(OkHttpUtil.string("https://gitcafe.net/alipaper/home.json?v=" + System.currentTimeMillis(), getHeaders())).getJSONObject("info").getJSONArray("new");
            setCache("home", json.toString());
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 首页数据内容
     *
     * @param filter 是否开启筛选
     * @return
     */
    public String homeContent(boolean filter) {
        try {
            JSONArray data = getHome();
            JSONArray classes = new JSONArray();
            for (String item : catList) {
                JSONObject type = new JSONObject();
                type.put("type_id", item);
                type.put("type_name", filterConfig.getString(item));
                classes.put(type);
            }
            JSONArray list = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                JSONObject vod = data.getJSONObject(i);
                if (catList.contains(vod.optString("cat", ""))) {
                    JSONObject v = new JSONObject();
                    v.put("vod_id", "https://www.aliyundrive.com/s/" + vod.getString("key"));
                    v.put("vod_name", vod.getString("title"));
                    v.put("vod_pic", "https://pic.rmb.bdstatic.com/bjh/1d0b02d0f57f0a42201f92caba5107ed.jpeg");
                    v.put("vod_remarks", vod.getString("date"));
                    list.put(v);
                }
            }
            JSONObject result = new JSONObject();
            result.put("class", classes);
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 分类数据
     *
     * @param tid
     * @param pg
     * @param filter
     * @param extend
     * @return
     */
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            JSONArray data = getCategory(tid);
            JSONArray list = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                JSONObject vod = data.getJSONObject(i);
                if (vod.optString("cat", "").equals(tid)) {
                    JSONObject v = new JSONObject();
                    v.put("vod_id", "https://www.aliyundrive.com/s/" + vod.getString("key"));
                    v.put("vod_name", vod.getString("title"));
                    v.put("vod_pic", "https://pic.rmb.bdstatic.com/bjh/1d0b02d0f57f0a42201f92caba5107ed.jpeg");
                    list.put(v);
                }
            }
            JSONObject result = new JSONObject();
            result.put("page", 1);
            result.put("pagecount", 1);
            result.put("limit", list.length());
            result.put("total", list.length());
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 视频详情信息
     *
     * @param ids 视频id
     * @return
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            return pushAgent.detailContent(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取视频播放信息
     *
     * @param flag     播放源
     * @param id       视频id
     * @param vipFlags 所有可能需要vip解析的源
     * @return
     */
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        return pushAgent.playerContent(flag, id, vipFlags);
    }

    public JSONArray getAllData() {
        try {
            if(allCat!=null && allCat.length()>0) {
                return allCat;
            }
            //获取的文件有问题先处理下文件
            Matcher ma = Pattern.compile("(\\s+\\{[\\S\\s]+?\\s+\\}),?").matcher(OkHttpUtil.string("https://gitcafe.net/alipaper/alls.json", getHeaders()).replace("\0",""));
            int start =0;
            JSONArray list = new JSONArray();
            while(ma.find(start)){
                list.put(new JSONObject(ma.group(1)));
                start = ma.end();
            }
            allCat = list;
            //                allCat = new JSONObject(OkHttpUtil.string("https://gitcafe.net/alipaper/alls.json", getHeaders()).replace("\0","")).getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allCat;
    }


    /**
     * 搜索
     *
     * @param key
     * @param quick 是否播放页的快捷搜索
     * @return
     */
    @Override
    public String searchContent(String key, boolean quick) {
        try {
            JSONArray data = getAllData();
            JSONArray list = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                JSONObject vod = data.getJSONObject(i);
                if (catList.contains(vod.getString("cat"))) {
                    String string = vod.getString("title");
                    if (string.contains(key)) {
                        JSONObject v = new JSONObject();
                        v.put("vod_id", "https://www.aliyundrive.com/s/" + vod.getString("key"));
                        v.put("vod_name", string);
                        list.put(v);
                    }
                }
            }
            JSONObject result = new JSONObject();
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
