package com.github.catvod.spider;

import com.github.catvod.crawler.SpiderDebug;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XPathFilterF extends XPath {
    private boolean firstPageFlag;

    private String firstPageUrl = "";

    @Override
    protected void loadRuleExt(String json) {

        try {
            JSONObject jsonObj = new JSONObject(json);
            firstPageFlag = jsonObj.optBoolean("firstPageFlag", false);
            firstPageUrl  = jsonObj.optString("firstPage").trim();
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    @Override
    protected String categoryUrl(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        String cateUrl = "";
        if (firstPageFlag)  {
            cateUrl = firstPageUrl;
            if (pg != null && Integer.parseInt(pg) > 1) {
                cateUrl = rule.getCateUrl();
            }
        } else {
            cateUrl = rule.getCateUrl();
        }

        if (filter && extend != null && extend.size() > 0) {
            for (Iterator<String> it = extend.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = extend.get(key);
                if (value.length() > 0) {
                    cateUrl = cateUrl.replace("{" + key + "}", URLEncoder.encode(value));
                }
            }
        }
        cateUrl = cateUrl.replace("{cateId}", tid).replace("{catePg}", pg);
        Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(cateUrl);
        while (m.find()) {
            String n = m.group(0).replace("{", "").replace("}", "");
            cateUrl = cateUrl.replace(m.group(0), "").replace("/" + n + "/", "");
        }
        return cateUrl;
    }
}
