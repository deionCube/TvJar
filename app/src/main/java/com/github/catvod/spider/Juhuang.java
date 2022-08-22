package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Juhuang extends Spider {

    private static final String siteUrl = "https://juhuang.tv";
    private static final String siteHost = "juhuang.tv";

    protected JSONObject playerConfig;
    protected JSONObject filterConfig;

    protected Pattern regexCategory = Pattern.compile("/type/(\\d+)_type.html");
    protected Pattern regexVid = Pattern.compile("/play/(\\d+)_play_\\d+_\\d+.html");
    protected Pattern regexPlay = Pattern.compile("/play/(\\d+)_play_(\\d+)_(\\d+).html");
    protected Pattern regexPage = Pattern.compile("/type/\\d+_type_(\\d+).html");

    @Override
    public void init(Context context) {
        super.init(context);
        try {
            playerConfig = new JSONObject("{\"xg\":{\"sh\":\"xg������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"dplayer\":{\"sh\":\"dplayer������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"videojs\":{\"sh\":\"videojs-H5������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"iva\":{\"sh\":\"iva-H5������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"iframe\":{\"sh\":\"iframe��������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"link\":{\"sh\":\"��������\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"swf\":{\"sh\":\"Flash�ļ�\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"flv\":{\"sh\":\"Flv�ļ�\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"plyr\":{\"sh\":\"plyr\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"H5player\":{\"sh\":\"H5player\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"playerjs\":{\"sh\":\"playerjs\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"aliplayer\":{\"sh\":\"���ﲥ����\",\"or\":999,\"sn\":0,\"pu\":\"\"}}");

        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    protected static HashMap<String, String> Headers() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
        headers.put("Host", "so.juhuang.tv");
        headers.put("Referer", "https://juhuang.tv/");
        return headers;
    }

    /**
     * ����headers
     *
     * @param refererUrl
     * @return
     */
    protected HashMap<String, String> getHeaders(String refererUrl) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        headers.put("Host", siteHost);
        headers.put("Referer", refererUrl);
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        return headers;
    }

    /**
     * ��ȡ�������� + ��ҳ���������Ƶ�б�����
     *
     * @param filter �Ƿ���ɸѡ �������� ���������� ��ҳ����Դ���ɸѡ����
     * @return
     */
    @Override
    public String homeContent(boolean filter) {
        try {
            String url = siteUrl + '/';
            Document doc = Jsoup.parse(OkHttpUtil.string(siteUrl, getHeaders(siteUrl)));
            // ����ڵ�
            Elements elements = doc.select("ul.nav-menu-items > li > a");
            JSONArray classes = new JSONArray();
            for (Element ele : elements) {
                //������
                String name = ele.text();
                boolean show = name.equals("Youtube��ѡ") ||
                        name.equals("��Ӱ") ||
                        name.equals("�缯") ||
                        name.equals("����") ||
                        name.equals("����") ||
                        name.equals("��¼Ƭ");
                if (show) {
                    Matcher mather = regexCategory.matcher(ele.attr("href"));
                    if (!mather.find())
                        continue;
                    // �ѷ����id������ȡ�����ӵ��б���
                    String id = mather.group(1).trim();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type_id", id);
                    jsonObject.put("type_name", name);
                    classes.put(jsonObject);
                }
            }
            JSONObject result = new JSONObject();
            if (filter) {
                result.put("filters", filterConfig);
            }
            result.put("class", classes);
            try {
                // ȡ��ҳ�Ƽ���Ƶ�б�
                Elements list = doc.select("div.module-items>div");
                JSONArray videos = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Element vod = list.get(i);
                    String title = vod.selectFirst("div.module-item-cover > div.module-item-pic > a").attr("title");
                    String cover = vod.selectFirst("div.module-item-cover > div.module-item-pic > img").attr("data-src");
                    String remark = vod.selectFirst("div.module-item-text").text();
                    Matcher matcher = regexVid.matcher(vod.selectFirst("div.module-item-cover > div.module-item-pic > a").attr("href"));
                    if (!matcher.find())
                        continue;
                    String id = matcher.group(1);
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", remark);
                    videos.put(v);
                }
                result.put("list", videos);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * ��ȡ������Ϣ����
     *
     * @param tid    ����id
     * @param pg     ҳ��
     * @param filter ͬhomeContent�����е�filter
     * @param extend ɸѡ����{k:v, k1:v1}
     * @return
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            String url = siteUrl + "/type/" + tid + "_type.html";
            if (pg != null && Integer.parseInt(pg) > 1) {
                url = siteUrl + String.format("/type/%s_type_%s.html", tid, pg);
            }
            // ��ȡ�������ݵ�url

            String html = OkHttpUtil.string(url, getHeaders(url));
            Document doc = Jsoup.parse(html);
            JSONObject result = new JSONObject();
            int pageCount = 1;
            int page = -1;
//            int page = Integer.parseInt(doc.select("div.module-footer >div[id=page] > span").text().trim());
            // ȡҳ�������Ϣ
            Elements pageInfo = doc.select("div.module-footer >div[id=page] > a");
            if (pageInfo.size() == 0) {
                page = Integer.parseInt(pg);
                pageCount = page;
            } else {
                for (int i = 0; i < pageInfo.size(); i++) {
                    Element div = pageInfo.get(i);
                    Element a = div.selectFirst("a");
                    if (a == null)
                        continue;
                    String name = a.text();

                    if (name.equals("βҳ")) {
                        Matcher matcher = regexPage.matcher(a.attr("href"));
                        if (matcher.find()) {
                            pageCount = Integer.parseInt(matcher.group(1));
                        }
                        break;
                    }
                }
            }

            JSONArray videos = new JSONArray();
            if (!html.contains("û���ҵ�����Ҫ�Ľ��Ŷ")) {
                // ȡ��ǰ����ҳ����Ƶ�б�
                Elements list = doc.select("div.module-items>div");
                for (int i = 0; i < list.size(); i++) {
                    Element vod = list.get(i);
                    String title = vod.selectFirst("div.module-item-cover > div.module-item-pic > a").attr("title");
                    String cover = vod.selectFirst("div.module-item-cover > div.module-item-pic > img").attr("data-src");
//                    String remark = vod.selectFirst("div.module-item-text").text();
                    Matcher matcher = regexVid.matcher(vod.selectFirst("div.module-item-cover > div.module-item-pic > a").attr("href"));
                    if (!matcher.find())
                        continue;
                    String id = matcher.group(1);
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
//                    v.put("vod_remarks", remark);
                    videos.put(v);
                }
            }
            result.put("page", page);
            result.put("pagecount", pageCount);
            result.put("limit", 48);
            result.put("total", pageCount <= 1 ? videos.length() : pageCount * 48);

            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * ��Ƶ������Ϣ
     *
     * @param ids ��Ƶid
     * @return
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            // ��Ƶ����url
            String url = siteUrl + "/vod/" + ids.get(0) + "_vod.html";
            //System.out.println(url);
            Document doc = Jsoup.parse(OkHttpUtil.string(url, getHeaders(url)));
            JSONObject result = new JSONObject();
            JSONObject vodList = new JSONObject();

            // ȡ��������
            String cover = doc.selectFirst("div.module-item-pic > img").attr("data-src");
            String title = doc.selectFirst("div.video-info-header > h1.page-title").text();
            String desc = doc.selectFirst("p.zkjj_a").text();


            vodList.put("vod_id", ids.get(0));
            vodList.put("vod_name", title);
            vodList.put("vod_pic", cover);
            vodList.put("vod_content", desc);
            //System.out.println(vodList.toString());
            Map<String, String> vod_play = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        int sort1 = playerConfig.getJSONObject(o1).getInt("or");
                        int sort2 = playerConfig.getJSONObject(o2).getInt("or");

                        if (sort1 == sort2) {
                            return 1;
                        }
                        return sort1 - sort2 > 0 ? 1 : -1;
                    } catch (JSONException e) {
                        SpiderDebug.log(e);
                    }
                    return 1;
                }
            });

            // ȡ�����б�����
            Elements modules = doc.select("div.module");


            for (int i = 0; i < modules.size() - 1; i++) {
                Element playSourceDoc = modules.get(i);
                Elements source = playSourceDoc.select("div.module-heading > h2.module-title");
                String sourceName = source.text();
                List<String> vodItems = new ArrayList<>();
                Elements playChapter = playSourceDoc.select("div.module-list > div.module-blocklist > div.scroll-content > a");
                for (int j = 0; j < playChapter.size(); j++) {
                    Element aEle = playChapter.get(j);
                    String chapterHref = aEle.attr("href");
                    String playUrl = chapterHref.substring(6, chapterHref.indexOf(".html"));
                    String chapterName = aEle.select("span").text();
                    vodItems.add(chapterName + "$" + playUrl);
                }
                String playList = "";
                if (vodItems.size() > 0) {
                    playList = TextUtils.join("#", vodItems);
                }
                vod_play.put(sourceName, playList);
            }


            if (vod_play.size() > 0) {
                String vod_play_from = TextUtils.join("$$$", vod_play.keySet());
                String vod_play_url = TextUtils.join("$$$", vod_play.values());
                vodList.put("vod_play_from", vod_play_from);
                vodList.put("vod_play_url", vod_play_url);
            }
            JSONArray list = new JSONArray();
            list.put(vodList);
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }


    /**
     * ��ȡ��Ƶ������Ϣ
     *
     * @param flag     ����Դ
     * @param id       ��Ƶid
     * @param vipFlags ���п�����Ҫvip������Դ
     * @return
     */
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            //���岥���õ�headers
            JSONObject headers = new JSONObject();
            headers.put("origin", " https://juhuang.tv");
            headers.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
            headers.put("Accept", " */*");
            headers.put("Accept-Language", " zh-CN,zh;q=0.9,en-US;q=0.3,en;q=0.7");
            headers.put("Accept-Encoding", " gzip, deflate");
            headers.put("Referer", " https://juhuang.tv/");


            // ����ҳ url
            String url = siteUrl + "/play/" + id + ".html";
            Document doc = Jsoup.parse(OkHttpUtil.string(url, getHeaders(url)));
            Elements allScript = doc.select("script");
            JSONObject result = new JSONObject();
            for (int i = 0; i < allScript.size(); i++) {
                String scContent = allScript.get(i).html().trim();
                if (scContent.startsWith("var player_")) { // ȡֱ��
                    int start = scContent.indexOf('{');
                    int end = scContent.lastIndexOf('}') + 1;
                    String json = scContent.substring(start, end);
                    JSONObject player = new JSONObject(json);
                    if (playerConfig.has(player.getString("from"))) {
                        JSONObject pCfg = playerConfig.getJSONObject(player.getString("from"));
                        String videoUrl = player.getString("url");

                        if (player.has("encrypt")) {
                            int encrypt = player.getInt("encrypt");
                            if (encrypt == 1) {
                                videoUrl = URLDecoder.decode(videoUrl);
                            } else if (encrypt == 2) {
                                videoUrl = new String(Base64.decode(videoUrl, Base64.DEFAULT));
                                videoUrl = URLEncoder.encode(new String(Base64.decode(URLDecoder.decode(videoUrl),Base64.DEFAULT)));
                            }
                        }
                        String jxvideoUrl = "https://web-webapi-tsjqsvyzyx.cn-shenzhen.fcapp.run/?url=" + videoUrl;
                        Map<String, String> jxheader = new HashMap<>();
                        jxheader.put("Referer", " https://juhuang.tv/");
                        jxheader.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
                        JSONObject Urljson = new JSONObject(OkHttpUtil.string(jxvideoUrl, jxheader));
                        String RealUrl = Urljson.getString("play_url");
//                        String FinalUrl = "https://" + RealUrl;
                        result.put("url", RealUrl);
                        result.put("parse", "0");
                        result.put("playUrl", "");
                        result.put("header", "");
                    }
                    break;
                }
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }


    @Override
    public String searchContent(String key, boolean quick) {
        try {
            if (quick)
                return "";
            String url = "https://so.juhuang.tv/soapi.php?wd=" + URLEncoder.encode(key);
            JSONObject searchResult = new JSONObject(OkHttpUtil.string(url, Headers()));
            JSONObject result = new JSONObject();
            JSONArray videos = new JSONArray();
            if (searchResult.getInt("count") > 0) {
                JSONArray lists = new JSONArray(searchResult.getString("list"));
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject vod = lists.getJSONObject(i);
                    String id = vod.getString("vod_id");
                    String title = vod.getString("vod_name");
                    String cover = vod.getString("vod_pic");
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", "");
                    videos.put(v);
                }
            }
            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
