package org.rokid.shell.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.Setting;
import org.rokid.shell.util.ApiUtil;
import org.rokid.shell.util.SettingManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: jayjay
 * @Date: 2024/4/1
 * @ClassName: MainApplicaiton
 * @Description: 脚本执行主类
 */
public class MainApplicaiton {

    public static String textStr = "<p><span>${text}</span></p>";
    public static String imageStr = "<div class=\"media-wrap image-wrap\"><img src=\"/person/file/fileUrl?ossId=${ossId}\"></div>";

    public static void main(String[] args) {
        try {
            System.out.println("rokid-shell");
            System.out.println("检查配置...");
            Setting setting = SettingManager.getSetting();
            checkKey("name");
            checkKey("interval");
            String interval = setting.getWithLog("interval");
            checkKey("cookie");
            System.out.println("配置正常...");
            sleepTime(3);
            while (true) {
                System.out.println("开始运行...");
                run();
                System.out.println("休息" + interval + "秒");
                sleepTime(Integer.parseInt(interval));
            }
        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            System.out.println("按任意键退出...");
            // 读取一个字节等待用户输入，达到类似阻塞的效果
            try {
                System.in.read();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 检查配置是否缺失
     *
     * @param key 配置键值
     */
    public static void checkKey(String key) {
        if (!SettingManager.getSetting().containsKey(key)) {
            throw new RuntimeException("配置：" + key + "缺失");
        }
    }


    public static void run() {
        Setting setting = SettingManager.getSetting();
        List<String> groups = setting.getGroups();
        //无配置
        if (CollUtil.isEmpty(groups)) {
            return;
        }
        //循环
        groups.stream().filter(group -> group.contains("article")).forEach(group -> {
            if (group.contains("article")) {
                String articleId = setting.get(group, "articleId");
                System.out.println("======================= " + articleId + " start =======================");
                String text = setting.get(group, "text");
                String imagePath = setting.get(group, "imagePath");
                System.out.println("文章id:" + articleId);
                boolean newComment = isNewComment(articleId);
                if (!newComment) {
                    System.out.println("没有有新的评论，跳过...");
                } else {
                    System.out.println("有新的评论...");
                    if (StrUtil.isBlank(text) && StrUtil.isBlank(imagePath)) {
                        System.out.println("text和imagePath都为空，跳过...");
                    }
                    StringBuilder sb = new StringBuilder();
                    if (StrUtil.isNotBlank(text)) {
                        sb.append(textStr.replace("${text}", text));
                    }
                    if (StrUtil.isNotBlank(imagePath)) {
                        String[] split = imagePath.split(",");
                        for (String s : split) {
                            System.out.println("开始上传图片" + s + "...");
                            JSONObject uploadImageData = ApiUtil.uploadImage(s);
                            if (uploadImageData == null) {
                                System.out.println("上传图片" + s + "失败");
                                continue;
                            }
                            String ossId = uploadImageData.getStr("ossId");
                            if (StrUtil.isNotBlank(ossId)) {
                                System.out.println("上传图片成功,ossId:" + ossId);
                                sb.append(imageStr.replace("${ossId}", ossId));
                            }
                        }
                    }
                    System.out.println("开始添加评论...");
                    Map<String, Object> addParam = ApiUtil.getAddParam();
                    addParam.put("postId", articleId);
                    addParam.put("content", sb.toString());
                    JSONObject add = ApiUtil.add(addParam);
                    if (add == null) {
                        System.out.println("评论失败");
                    }
                    System.out.println(add.getStr("msg"));
                    System.out.println("======================= " + articleId + " end =======================");
                }
            }
        });
    }

    /**
     * 是否有新的评论
     *
     * @return true 为有新的，false则无
     */
    public static boolean isNewComment() {
        System.out.println("获取评论.....");
        JSONArray list = ApiUtil.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONObject jsonObject = list.getJSONObject(0);
            System.out.println("最新评论为：" + jsonObject.getStr("userName"));
            String name = SettingManager.getSetting().get("name");
            System.out.println("用户名为：" + name);
            if (jsonObject.getStr("userName").equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否有新的评论
     *
     * @param articleId 文章id
     * @return true 为有新的，false则无
     */
    public static boolean isNewComment(String articleId) {
        System.out.println("获取评论.....");
        JSONArray list = ApiUtil.getList(articleId);
        if (CollUtil.isNotEmpty(list)) {
            JSONObject jsonObject = list.getJSONObject(0);
            System.out.println("最新评论为：" + jsonObject.getStr("userName"));
            String name = SettingManager.getSetting().get("name");
            System.out.println("用户名为：" + name);
            if (jsonObject.getStr("userName").equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 睡眠＋倒计时
     *
     * @param interval
     * @throws InterruptedException
     */
    public static void sleepTime(int interval) throws InterruptedException {
        for (int i = interval; i >= 0; i--) {
            Thread.sleep(1000);
            if (i <= 3 && i >= 1) {
                System.out.println(i + "秒后运行");
            }
        }
    }


}
