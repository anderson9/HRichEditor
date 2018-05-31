package com.huangdali.bean;

import android.text.TextUtils;

/**
 * -------------------------------------------
 * Dec:富文本content的对象
 * Created by: Luojiusan on 2018/5/30--:10:53
 * Modify by:
 * -------------------------------------------
 **/

public class EContent  {
    private String url;  //图片或者 视频的地址 get时候拼接成地址
    private String content;//富文本内容 自成html格式
    private String type;

    public EContent() {
    }

    public EContent(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public EContent(String url, String content, String style, String type) {
        this.url = url;
        this.content = content;
        this.type = type;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getHtml() {
        String html = "";
        switch (type) {
            case ItemType.IMG:
                if (!TextUtils.isEmpty(content)) {
                    html = content + "</div><img src='" + url + "' />";
                } else {
                    html = "<img src='" + url + "' />";
                }
                html += "<br/>";
                break;
            case ItemType.VIDEO:
                if (!TextUtils.isEmpty(content)) {
                    html = content + "</div><video src='" + url + "' />";
                } else {
                    html = "<video src='" + url + "' />";
                }
                html += "<br/>";
                break;
            case ItemType.TXT:
                html = content;
                html += "<br/>";
                break;
        }
        return html;
    }

}
