package org.rokid.shell.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jayjay
 * @Date: 2024/4/4
 * @ClassName: ApiUtil
 * @Description: Api请求工具类
 */
public class ApiUtil {

    public static String  contentStr = "<p><span>${text}</span></p><div class=\"media-wrap image-wrap\"><img src=\"/person/file/fileUrl?ossId=${ossId}\"></div><p></p>";
    public static String  contentTextStr = "<p><span>${text}</span></p>";
    public static void main(String[] args) {
        String name = SettingManager.getSetting().get("name");
        JSONArray list = getList();
        if(CollUtil.isNotEmpty(list)){
            JSONObject jsonObject = list.getJSONObject(0);
            System.out.println(jsonObject);
            if (jsonObject.getStr("userName").equals(name)) {
                System.out.println("没有新的，停止");
                return;
            }
        }
        JSONObject account1 = uploadImage();
        String ossId = account1.getStr("ossId");
        System.out.println(account1);
        if(StrUtil.isNotBlank(ossId)){
            JSONObject account11 = add(ossId);
            System.out.println(account11);
        }

    }
    @Test
    public void test1() {
        JSONObject account1 = uploadImage();
        System.out.println(account1);
    }

    public static JSONArray getList(){
        String cookie = SettingManager.getSetting().get("cookie" );
        String articleId = SettingManager.getSetting().get("articleId");
        if(StrUtil.isBlank(articleId)){
            return null;
        }
        String url = "https://forum.rokid.com/person/reply/list?lang=zh-CN&sortType=2&pageNum=1&pageSize=10&postId="+articleId;
        HttpResponse res = HttpRequest.get(url)
                .cookie(cookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "forum.rokid.com")
                .execute();
        if(res.getStatus() != 200){
            return null;
        }
        String body = res.body();
        JSONObject json = toJSON(body);
        Integer code = json.getInt("code");
        if(code != 1){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }
        JSONArray list = data.getJSONArray("list");
        if(JSONUtil.isNull(list)){
            return null;
        }
        return list;
    }


    public static JSONArray getList(String articleId){
        String cookie = SettingManager.getSetting().get("cookie" );
        if(StrUtil.isBlank(articleId)){
            return null;
        }
        String url = "https://forum.rokid.com/person/reply/list?lang=zh-CN&sortType=2&pageNum=1&pageSize=10&postId="+articleId;
        HttpResponse res = HttpRequest.get(url)
                .cookie(cookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "forum.rokid.com")
                .execute();
        if(res.getStatus() != 200){
            return null;
        }
        String body = res.body();
        JSONObject json = toJSON(body);
        Integer code = json.getInt("code");
        if(code != 1){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }
        JSONArray list = data.getJSONArray("list");
        if(JSONUtil.isNull(list)){
            return null;
        }
        return list;
    }

    public static JSONObject  uploadImage(){
        String cookie = SettingManager.getSetting().get("cookie" );
        String imagePath = SettingManager.getSetting().get("imagePath");
        String imageName = System.currentTimeMillis()+"-"+ SecureUtil.md5(System.currentTimeMillis()+"")+"." +FileUtil.getSuffix(imagePath);
        Map<String, Object> param = getUploadParam();
        String tempImage = DirUtil.getUserDir()+"temp"+File.separator + imageName;
        File copy = FileUtil.copy(imagePath, tempImage, false);
        param.put("file", copy);
        try {

            String url = "https://forum.rokid.com/person/file/upload?lang=zh-CN";
            HttpResponse res = HttpRequest.post(url)
                    .cookie(cookie)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                    .header("Host", "forum.rokid.com")
                    .form(param)
                    .execute();

            if(res.getStatus() != 200){
                return null;
            }
            FileUtil.del(copy);
            String body = res.body();
            JSONObject json = toJSON(body);
            if(JSONUtil.isNull(json)){
                return null;
            }
            Integer code = json.getInt("code");
            if(code != 1){
                return null;
            }
            JSONObject data = json.getJSONObject("data");
            if(JSONUtil.isNull(data)){
                return null;
            }
            return data;
        }finally {
            //删除图片
            FileUtil.del(copy);
        }

    }

    public static JSONObject  uploadImage(String path){
        String cookie = SettingManager.getSetting().get("cookie" );
        String imageName = System.currentTimeMillis()+"-"+ SecureUtil.md5(System.currentTimeMillis()+"")+"." +FileUtil.getSuffix(path);
        Map<String, Object> param = getUploadParam();
        String tempImage = DirUtil.getUserDir()+"temp"+File.separator + imageName;
        File copy = FileUtil.copy(path, tempImage, false);
        param.put("file", copy);
        try {

            String url = "https://forum.rokid.com/person/file/upload?lang=zh-CN";
            HttpResponse res = HttpRequest.post(url)
                    .cookie(cookie)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                    .header("Host", "forum.rokid.com")
                    .form(param)
                    .execute();

            if(res.getStatus() != 200){
                return null;
            }
            FileUtil.del(copy);
            String body = res.body();
            JSONObject json = toJSON(body);
            if(JSONUtil.isNull(json)){
                return null;
            }
            Integer code = json.getInt("code");
            if(code != 1){
                return null;
            }
            JSONObject data = json.getJSONObject("data");
            if(JSONUtil.isNull(data)){
                return null;
            }
            return data;
        }finally {
            //删除图片
            FileUtil.del(copy);
        }

    }
    public static JSONObject  add(String ossId){
        String cookie = SettingManager.getSetting().get("cookie" );
        String text = SettingManager.getSetting().get("text");
        String articleId = SettingManager.getSetting().get("articleId");
        String url = "https://forum.rokid.com/person/reply/add?lang=zh-CN";
        Map<String, Object> param = getAddParam();
        param.put("postId",articleId);
        String replace = contentStr.replace("${text}", text).replace("${ossId}", ossId);
        param.put("content",replace);
        System.out.println(JSONUtil.toJsonStr(param));
//        System.out.println(1/0);
        HttpResponse res = HttpRequest.post(url)
                .cookie(cookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "forum.rokid.com")
                .body(JSONUtil.toJsonStr(param)).execute();

        String body = res.body();
        if(res.getStatus() != 200){
            return null;
        }
        JSONObject json = toJSON(body);
        if(JSONUtil.isNull(json)){
            return null;
        }
        Integer code = json.getInt("code");
        if(code != 1){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }

        String msg = json.getStr("msg");
        data.set("msg",msg);
        return data;
    }


    public static JSONObject  add(Map<String,Object> param){
        String cookie = SettingManager.getSetting().get("cookie" );
        String url = "https://forum.rokid.com/person/reply/add?lang=zh-CN";
        System.out.println(JSONUtil.toJsonStr(param));
//        System.out.println(1/0);
        HttpResponse res = HttpRequest.post(url)
                .cookie(cookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "forum.rokid.com")
                .body(JSONUtil.toJsonStr(param)).execute();

        String body = res.body();
        if(res.getStatus() != 200){
            return null;
        }
        JSONObject json = toJSON(body);
        if(JSONUtil.isNull(json)){
            return null;
        }
        Integer code = json.getInt("code");
        if(code != 1){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }

        String msg = json.getStr("msg");
        data.set("msg",msg);
        return data;
    }




    public static Map<String,Object> getUploadParam(){
        Map<String, Object> form = new HashMap<>();
        form.put("isReply",true);
        return form;
    }

    public static Map<String,Object> getAddParam(){
        Map<String, Object> form = new HashMap<>();
        form.put("visible", 1);
        return form;
    }



    public static JSONObject toJSON(String str){
        if(StrUtil.isBlank(str)){
            return null;
        }
        if(!JSONUtil.isTypeJSON(str)){
            return null;
        }
        return JSONUtil.parseObj(str);
    }



}
