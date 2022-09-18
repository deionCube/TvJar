package com.github.catvod.spider;

import android.content.Context;
import com.github.catvod.crawler.Spider;
import com.github.catvod.utils.okhttp.OkHttpUtil;
//RAS解密
import com.github.catvod.utils.RSA;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class KanSJ extends Spider {
    private String ue = "http://kantv.zsyzcy.cn";
    private String u = "";
    private String B = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDqbQ2AO1Ehyap6FmLrkqjhsqpyqOL/M9RtytRwqRqv/Yzeq21mToFuJ3yE9SIaN1hYhq7ZSYnWhhvjwBXXVHo7gSR/ABMCkRSRMLENNds5zYA8pVxxPMLWUXsdyokZlsIRF2XOxz8Ol6zwqURkp9ThJBDu4ym91ZXh4FglqQS2CwIDAQAB";
    private String W = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOptDYA7USHJqnoWYuuSqOGyqnKo4v8z1G3K1HCpGq/9jN6rbWZOgW4nfIT1Iho3WFiGrtlJidaGG+PAFddUejuBJH8AEwKRFJEwsQ012znNgDylXHE8wtZRex3KiRmWwhEXZc7HPw6XrPCpRGSn1OEkEO7jKb3VleHgWCWpBLYLAgMBAAECgYBVuuHoFkk6YQTONyef3PeT6oH5AphZGfxC1p1QQhd3avM8b1bHxkgBH8Gi4f7BtaHCZibFYeZdpJfId3PFVqiILR2rebJ5prJpFyfHl3RVqUZ9AzPumHRRD5JIC6YA7vBCg5724T8Veg9CxTdHzybYLCwXaDYFTlW0/6nnh1gPoQJBAPqbq5gkXCh+o/DrpT7kqia2cTun45XMCmYTiKShjmMujIVPC+/O43aErRpuLbeivxI6B9XqA841mSPb8uTUpbsCQQDveECxlKfpq01q//xAXb3lL+e+P7DX18W8ek+lLtXoUDhyKEB2c6zHO9g6A/6fHgx2DJH1+vnYmjHiiH2QsoPxAkEAmNaRy0L5lZTOpSMB756DiwKfglN9ACGlgeWN42HINgLwnmi8De/uV5zI+aKSbTlrMFGF79c9pOiZUf5VX2u0+wJABk6GdabSnUbTrSO8wv01CRov4kTPJYAbRxF5k4IeRBYIxojk2bnGLSEYWr7ML+icr2c5WN8ZQWkeMzchB3SMIQJADGzV6RHvx/6DCzLvSrtHUnyfKyCh19tC5hxyVyuXMAlKdQIzRSiTIOiRY/5HwvVi8ZCVvYmHQ7n0XGYFdJ3s7Q==";
    private String Pd = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCriltAoK1NhfK6Tpjip3uQg87B8Gnf+m/Kf7Wz+oPsfTfkpCppreQ6j1hDdPbAbh87+iuszCdgSqbm/xr84PFIcWtF5nndS3rxLqo9EMgDrC+yTdeVGC6jku8lr9FgTPRf+LfMR65LesXlj2dhmI9PD/xFotQE/YpsCMeovV1zzwIDAQAB";
    private String u6 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKuKW0CgrU2F8rpOmOKne5CDzsHwad/6b8p/tbP6g+x9N+SkKmmt5DqPWEN09sBuHzv6K6zMJ2BKpub/Gvzg8Uhxa0Xmed1LevEuqj0QyAOsL7JN15UYLqOS7yWv0WBM9F/4t8xHrkt6xeWPZ2GYj08P/EWi1AT9imwIx6i9XXPPAgMBAAECgYB4Fsv7WoD4nKVouE1fn1Bpw1yjeOMl4fURFobmEKL6lE7kyejHQu1b8a4gy2lTHcTQADtDJUkLqDdfz4fdMLy4jMl/Cv2FJ0K3zKXQKVhMpTcndLLjkdRvYyrdmyNRbW3ZghMwcwNrmkYwQNUz7y3Cvf0+pNHhEck7PZBc3l8uwQJBANoQ3ip9va8oPkxG68iSnVzDASnqOGDdgDD9bIyva0S7L0wix0chcy00CkmoR7srivPRxhM8xBq6iPeWMmoXPxUCQQDJYZISJ9470OVrFVo24utj+0ju7bfAbFDqb2uFxyM6fXKmWeeNDLJIvSQ3efl6SJIYJg2h+pXP32SdKJDNhABTAkBdBfcQH0Wsk7lHprVsgYbZjGJRtBXK1JKb02/b7/UNg+BgVTG3WUCd1uZQl5XJ9YA4FJOvnyt0z4AgRhVJhpMlAkEAnkz5oxX95u1d+mBNi0mzmeOFQHRTdopuzOqLDh0s/yRvxH51u3XYi79KweU7DDoRxbPcEZFGxKIpmDS0Jyl1iwJBAIODi6S2PViooWayw9+lP9pvqOLAEwI/zO35iUyKqO8rizf3FFHp9YpzNzugxXylcL30g5Sz8eVUIqfjaRqcSCg=";
    private String jw = "1";
    private String b = null;

    private HashMap<String, String> ue(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10; PBEM00 Build/QKQ1.190918.001)");
        return hashMap;
    }

    public String categoryContent(String str, String str2, boolean z, HashMap<String, String> hashMap) {
        try {
            JSONObject jSONObject = new JSONObject();
            String str3 = this.ue + "/1080detail.php/app/video?&tid=" + str;
            HashMap<String, String> hashMap2 = hashMap == null ? new HashMap<>() : hashMap;
            if (!hashMap2.containsKey("area")) {
                hashMap2.put("area", "");
            }
            if (!hashMap2.containsKey("type")) {
                hashMap2.put("type", "");
            }
            if (!hashMap2.containsKey("year")) {
                hashMap2.put("year", "");
            }
            if (!hashMap2.containsKey("class")) {
                hashMap2.put("class", "");
            }
            String str4 = (((str3 + "&class=" + hashMap2.get("type")) + "&area=" + hashMap2.get("area")) + "&year=" + hashMap2.get("year")) + "&pg=" + str2;
            JSONObject jSONObject2 = new JSONObject(OkHttpUtil.string(str4, ue(str4)));
            JSONArray jSONArray = jSONObject2.getJSONArray("list");
            JSONArray jSONArray2 = new JSONArray();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("vod_id", jSONObject3.getString("vod_id"));
                jSONObject4.put("vod_name", jSONObject3.getString("vod_name"));
                jSONObject4.put("vod_pic", jSONObject3.getString("vod_pic"));
                jSONObject4.put("vod_remarks", jSONObject3.getString("vod_remarks"));
                jSONArray2.put(jSONObject4);
            }
            int i2 = jSONObject2.getInt("total");
            int i3 = jSONObject2.getInt("pagecount");
            jSONObject.put("page", jSONObject2.getInt("page"));
            jSONObject.put("pagecount", i3);
            jSONObject.put("limit", jSONObject2.getInt("limit"));
            jSONObject.put("total", i2);
            jSONObject.put("list", jSONArray2);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public String detailContent(List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            String str = this.ue + "/1080tv.php/provide/vod/?ac=detail&ids=" + list.get(0);
            JSONObject jSONObject3 = new JSONObject(OkHttpUtil.string(str, ue(str))).getJSONArray("list").getJSONObject(0);
            jSONObject.put("vod_id", jSONObject3.getString("vod_id"));
            jSONObject.put("vod_name", jSONObject3.getString("vod_name"));
            jSONObject.put("vod_pic", jSONObject3.getString("vod_pic"));
            jSONObject.put("type_name", jSONObject3.getString("vod_class"));
            jSONObject.put("vod_year", jSONObject3.getString("vod_year"));
            jSONObject.put("vod_area", jSONObject3.getString("vod_area"));
            jSONObject.put("vod_remarks", jSONObject3.getString("vod_remarks"));
            jSONObject.put("vod_actor", jSONObject3.getString("vod_actor"));
            jSONObject.put("vod_director", jSONObject3.getString("vod_director"));
            jSONObject.put("vod_content", jSONObject3.getString("vod_content"));
            jSONObject.put("vod_play_from", jSONObject3.getString("vod_play_from"));
            jSONObject.put("vod_play_url", RSA.decryptByPrivateKey(jSONObject3.getString("vod_play_url").replace("\\/", "/"), this.u6));
           // jSONObject.put("vod_play_url", RSA.decryptByPublicKey(jSONObject3.getString("vod_play_url").replace("\\/", "/"), this.u6));
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject);
            jSONObject2.put("list", jSONArray);
            return jSONObject2.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public String homeContent(boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("class", new JSONArray("[{\"type_id\":\"1\",\"type_name\":\"电影\"},{\"type_id\":\"2\",\"type_name\":\"电视剧\"},{\"type_id\":\"30\",\"type_name\":\"日韩剧\"},{\"type_id\":\"29\",\"type_name\":\"欧美剧\"},{\"type_id\":\"33\",\"type_name\":\"港台剧\"},{\"type_id\":\"3\",\"type_name\":\"综艺\"},{\"type_id\":\"4\",\"type_name\":\"动漫\"}]"));
            if (z) {
                jSONObject.put("filters", new JSONObject("{\"1\":[{\"key\":\"type\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"警匪\",\"v\":\"警匪\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"动画\",\"v\":\"动画\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"枪战\",\"v\":\"枪战\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"经典\",\"v\":\"经典\"},{\"n\":\"青春\",\"v\":\"青春\"},{\"n\":\"文艺\",\"v\":\"文艺\"},{\"n\":\"微电影\",\"v\":\"微电影\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"运动\",\"v\":\"运动\"},{\"n\":\"农村\",\"v\":\"农村\"},{\"n\":\"儿童\",\"v\":\"儿童\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"内地\"},{\"n\":\"中国大陆\",\"v\":\"大陆\"},{\"n\":\"中国香港\",\"v\":\"香港\"},{\"n\":\"中国台湾\",\"v\":\"台湾\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"意大利\",\"v\":\"意大利\"},{\"n\":\"西班牙\",\"v\":\"西班牙\"},{\"n\":\"加拿大\",\"v\":\"加拿大\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"year\",\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"2006\",\"v\":\"2006\"},{\"n\":\"2005\",\"v\":\"2005\"},{\"n\":\"2004\",\"v\":\"2004\"},{\"n\":\"2003\",\"v\":\"2003\"},{\"n\":\"2002\",\"v\":\"2002\"},{\"n\":\"2001\",\"v\":\"2001\"},{\"n\":\"2000\",\"v\":\"2000\"}]}],\"2\":[{\"key\":\"type\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"青春偶像\",\"v\":\"青春偶像\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"家庭\",\"v\":\"家庭\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"经典\",\"v\":\"经典\"},{\"n\":\"乡村\",\"v\":\"乡村\"},{\"n\":\"情景\",\"v\":\"情景\"},{\"n\":\"商战\",\"v\":\"商战\"},{\"n\":\"网剧\",\"v\":\"网剧\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"内地\"},{\"n\":\"中国大陆\",\"v\":\"大陆\"},{\"n\":\"中国台湾\",\"v\":\"中国台湾\"},{\"n\":\"中国香港\",\"v\":\"中国香港\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"新加坡\",\"v\":\"新加坡\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"year\",\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"2006\",\"v\":\"2006\"},{\"n\":\"2005\",\"v\":\"2005\"},{\"n\":\"2004\",\"v\":\"2004\"},{\"n\":\"2003\",\"v\":\"2003\"},{\"n\":\"2002\",\"v\":\"2002\"},{\"n\":\"2001\",\"v\":\"2001\"},{\"n\":\"2000\",\"v\":\"2000\"}]}],\"4\":[{\"key\":\"type\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"情感\",\"v\":\"情感\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"热血\",\"v\":\"热血\"},{\"n\":\"推理\",\"v\":\"推理\"},{\"n\":\"搞笑\",\"v\":\"搞笑\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"萝莉\",\"v\":\"萝莉\"},{\"n\":\"校园\",\"v\":\"校园\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"机战\",\"v\":\"机战\"},{\"n\":\"运动\",\"v\":\"运动\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"少年\",\"v\":\"少年\"},{\"n\":\"少女\",\"v\":\"少女\"},{\"n\":\"社会\",\"v\":\"社会\"},{\"n\":\"原创\",\"v\":\"原创\"},{\"n\":\"亲子\",\"v\":\"亲子\"},{\"n\":\"益智\",\"v\":\"益智\"},{\"n\":\"励志\",\"v\":\"励志\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"中国\",\"v\":\"内地\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"欧美\",\"v\":\"欧美\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"year\",\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"2006\",\"v\":\"2006\"},{\"n\":\"2005\",\"v\":\"2005\"},{\"n\":\"2004\",\"v\":\"2004\"},{\"n\":\"2003\",\"v\":\"2003\"},{\"n\":\"2002\",\"v\":\"2002\"},{\"n\":\"2001\",\"v\":\"2001\"},{\"n\":\"2000\",\"v\":\"2000\"}],\"3\":[{\"key\":\"type\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"选秀\",\"v\":\"选秀\"},{\"n\":\"情感\",\"v\":\"情感\"},{\"n\":\"访谈\",\"v\":\"访谈\"},{\"n\":\"播报\",\"v\":\"播报\"},{\"n\":\"旅游\",\"v\":\"旅游\"},{\"n\":\"音乐\",\"v\":\"音乐\"},{\"n\":\"美食\",\"v\":\"美食\"},{\"n\":\"纪实\",\"v\":\"纪实\"},{\"n\":\"曲艺\",\"v\":\"曲艺\"},{\"n\":\"生活\",\"v\":\"生活\"},{\"n\":\"游戏互动\",\"v\":\"游戏互动\"},{\"n\":\"财经\",\"v\":\"财经\"},{\"n\":\"求职\",\"v\":\"求职\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"内地\"},{\"n\":\"港台\",\"v\":\"港台\"},{\"n\":\"日韩\",\"v\":\"日韩\"},{\"n\":\"欧美\",\"v\":\"欧美\"}]},{\"key\":\"year\",\"name\":\"年份\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]}]}]}"));
            }
            return jSONObject.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public String homeVideoContent() {
        try {
            String str = this.ue + "/1080tv.php/provide/vod/?ac=list";
            JSONObject jSONObject = new JSONObject(OkHttpUtil.string(str, ue(str)));
            System.out.println("jSONObject"+jSONObject);
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = jSONObject.getJSONArray("list");
            for (int i = 0; i < jSONArray2.length(); i++) {
                JSONObject jSONObject2 = jSONArray2.getJSONObject(i);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("vod_id", jSONObject2.getString("vod_id"));
                jSONObject3.put("vod_name", jSONObject2.getString("vod_name"));
                jSONObject3.put("vod_pic", jSONObject2.getString("vod_pic"));
                jSONObject3.put("vod_remarks", jSONObject2.getString("vod_remarks"));
                jSONArray.put(jSONObject3);
            }
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("list", jSONArray);
            return jSONObject4.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public void init(Context context) {
       super.init(context);
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject(OkHttpUtil.string("https://jihulab.com/nonullgg/xiaobai/-/raw/main/w/ksj.txt", null));
            JSONObject jSONObject2 = new JSONObject();
            String url = jSONObject.getString("tvjx") + str2;
            String ue = RSA.decryptByPrivateKey(OkHttpUtil.string(url, ue(url)), this.W);
            jSONObject2.put("parse", 0);
            jSONObject2.put("url", ue);
            return jSONObject2.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public String searchContent(String str, boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            String str2 = this.ue + "/1080tv.php/provide/vod/?ac=detail&wd=" + URLEncoder.encode(str) + "&pg=1";
            String h = OkHttpUtil.string(str2, ue(str2));
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = new JSONObject(h).getJSONArray("list");
            for (int i = 0; i < jSONArray2.length(); i++) {
                JSONObject jSONObject2 = jSONArray2.getJSONObject(i);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("vod_id", jSONObject2.getString("vod_id"));
                jSONObject3.put("vod_name", jSONObject2.getString("vod_name"));
                jSONObject3.put("vod_pic", jSONObject2.getString("vod_pic"));
                jSONObject3.put("vod_remarks", jSONObject2.getString("vod_remarks"));
                jSONArray.put(jSONObject3);
            }
            jSONObject.put("list", jSONArray);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return "";
        }
    }
}
