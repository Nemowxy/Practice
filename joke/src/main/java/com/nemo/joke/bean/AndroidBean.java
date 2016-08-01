package com.nemo.joke.bean;

import java.util.List;

/**
 * Created by nemo on 2016/8/1 0001.
 */

public class AndroidBean {

    /**
     * error : false
     * results : [{"_id":"579eb6fa421aa91e26064748","createdAt":"2016-08-01T10:42:02.491Z","desc":"有朋友通过尝试逆向微信图片压缩算法而实现的一个压缩比还不错的压缩算法。","publishedAt":"2016-08-01T12:00:57.45Z","source":"chrome","type":"Android","url":"https://github.com/Curzibn/Luban","used":true,"who":"代码家"},{"_id":"579eb5e4421aa90d39e709a8","createdAt":"2016-08-01T10:37:24.458Z","desc":"Java 和 Javascript Bridge 封装。","publishedAt":"2016-08-01T12:00:57.45Z","source":"chrome","type":"Android","url":"https://github.com/ImangazalievM/Scripto","used":true,"who":"代码家"},{"_id":"579e9ba8421aa90d39e709a4","createdAt":"2016-08-01T08:45:28.443Z","desc":"聊天列表样式，使用很简单","publishedAt":"2016-08-01T12:00:57.45Z","source":"chrome","type":"Android","url":"https://github.com/snipsnap/SlyceMessaging","used":true,"who":"花开堪折枝"},{"_id":"579d9ebe421aa90d36e960c8","createdAt":"2016-07-31T14:46:22.719Z","desc":"(非常棒)深入理解Davilk字节码指令及Smali文件","publishedAt":"2016-08-01T12:00:57.45Z","source":"chrome","type":"Android","url":"http://blog.csdn.net/dd864140130/article/details/52076515","used":true,"who":"sbbic"},{"_id":"579ab71d421aa91e2606472b","createdAt":"2016-07-29T09:53:33.816Z","desc":"视图分层视差效果","publishedAt":"2016-08-01T12:00:57.45Z","source":"chrome","type":"Android","url":"https://github.com/SchibstedSpain/Parallax-Layer-Layout","used":true,"who":"Jason"},{"_id":"579aade1421aa90d39e70989","createdAt":"2016-07-29T09:14:09.997Z","desc":"类似刮刮乐擦除效果，做的挺逼真，可以可以。","publishedAt":"2016-07-29T09:37:39.219Z","source":"chrome","type":"Android","url":"https://github.com/jackpocket/android_scratchoff","used":true,"who":"代码家"},{"_id":"579aadb9421aa90d36e960b3","createdAt":"2016-07-29T09:13:29.889Z","desc":"Android-3DTouch-PeekView","publishedAt":"2016-07-29T09:37:39.219Z","source":"chrome","type":"Android","url":"https://github.com/klinker24/Android-3DTouch-PeekView","used":true,"who":"花开堪折枝"},{"_id":"579aab60421aa90d39e70988","createdAt":"2016-07-29T09:03:28.829Z","desc":"EditText 实现 Tag 输入和快速选择功能","publishedAt":"2016-07-29T09:37:39.219Z","source":"chrome","type":"Android","url":"https://github.com/OfficialAmal/ChipLayout","used":true,"who":"代码家"},{"_id":"579aaaf0421aa90d36e960b2","createdAt":"2016-07-29T09:01:36.925Z","desc":"基于 Picasso 库实现的人脸检测和图片自动智能裁剪，做的超棒。","publishedAt":"2016-07-29T09:37:39.219Z","source":"chrome","type":"Android","url":"https://github.com/aryarohit07/PicassoFaceDetectionTransformation","used":true,"who":"代码家"},{"_id":"579aaa7d421aa90d2fc94b83","createdAt":"2016-07-29T08:59:41.262Z","desc":"Flipboard 中国区新晋小王子开源的一款漂亮的 Banner 组件。","publishedAt":"2016-07-29T09:37:39.219Z","source":"chrome","type":"Android","url":"https://github.com/chengdazhi/DecentBanner","used":true,"who":"代码家"}]
     */

    private boolean error;
    /**
     * _id : 579eb6fa421aa91e26064748
     * createdAt : 2016-08-01T10:42:02.491Z
     * desc : 有朋友通过尝试逆向微信图片压缩算法而实现的一个压缩比还不错的压缩算法。
     * publishedAt : 2016-08-01T12:00:57.45Z
     * source : chrome
     * type : Android
     * url : https://github.com/Curzibn/Luban
     * used : true
     * who : 代码家
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
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

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
