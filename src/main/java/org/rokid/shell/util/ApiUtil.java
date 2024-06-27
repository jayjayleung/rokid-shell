package org.rokid.shell.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.rokid.shell.cache.SeetingManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class ApiUtil {

    public static String  contentStr = "<p><span>${text}</span></p><div class=\"media-wrap image-wrap\"><img src=\"/person/file/fileUrl?ossId=${ossId}\"></div><p></p>";
    public static void main(String[] args) {
        JSONObject account1 = uploadImage("account1");
        String ossId = account1.getStr("ossId");
        System.out.println(account1);
        if(StrUtil.isNotBlank(ossId)){
            JSONObject account11 = add("account1", ossId);
            System.out.println(account11);
        }
    }

    public static JSONObject  uploadImage(String group){
        String cookie = SeetingManager.getSetting().get("account1","cookie" );
        String imagePath = SeetingManager.getSetting().get("account1","imagePath");
        Map<String, Object> param = getUploadParam(imagePath);
        String url = "https://forum.rokid.com/person/file/upload?lang=zh-CN";
        HttpResponse res = HttpRequest.post(url)
                .cookie(cookie)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "forum.rokid.com")
                .form(param).execute();

        if(res.getStatus() != 200){
            return null;
        }
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
    }
    public static JSONObject  add(String group,String ossId){
        String cookie = SeetingManager.getSetting().get(group,"cookie" );
        String text = SeetingManager.getSetting().get(group,"text");
        String articleId = SeetingManager.getSetting().get("articleId");
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



    public static Map<String,Object> getUploadParam(String path){
        Map<String, Object> form = new HashMap<>();
        form.put("isReply",true);
        form.put("file", FileUtil.file(path));
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
