package org.rokid.shell.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.AbsSetting;
import cn.hutool.setting.Setting;
import org.rokid.shell.util.ApiUtil;
import org.rokid.shell.util.SeetingManager;
import org.rokid.shell.util.DirUtil;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @Author: jayjay
 * @Date: 2024/4/1
 * @ClassName: MainApplicaiton
 * @Description: 脚本执行主类
 */
public class MainApplicaiton {

    public static void main(String[] args) {
        try {
                System.out.println("检查配置...");
                Setting setting = SeetingManager.getSetting();
                setting.getWithLog("articleId");
                String interval = setting.getWithLog("interval");
                setting.getWithLog("name");
                setting.getWithLog("text");
                setting.getWithLog("cookie");
                System.out.println("配置正常...");
                sleepTime(3);
                while (true) {
                    System.out.println("开始运行...");
                    boolean newComment = isNewComment();
                    if (!newComment) {
                        System.out.println("没有有新的评论，等待"+interval+"秒后再次运行");
                    }else {
                        System.out.println("有新的评论");
                        System.out.println("开始上传图片...");
                        JSONObject uploadImageData = ApiUtil.uploadImage();
                        if (uploadImageData == null) {
                            System.out.println("上传图片失败");
                        }
                        String ossId = uploadImageData.getStr("ossId");
                        if (StrUtil.isNotBlank(ossId)) {
                            System.out.println("上传图片成功,ossId:" + ossId);
                            System.out.println("开始添加评论...");
                            JSONObject add = ApiUtil.add(ossId);
                            if (add == null) {
                                System.out.println("评论失败");
                            }
                            System.out.println(add.getStr("msg"));
                        }
                        System.out.println("休息" + interval + "秒");
                    }
                    sleepTime(Integer.parseInt(interval));
                }
        }catch (Exception e){
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

    public static boolean isNewComment() {
        System.out.println("获取评论.....");
        JSONArray list = ApiUtil.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONObject jsonObject = list.getJSONObject(0);
            System.out.println("最新评论为："+jsonObject.getStr("userName"));
            String name = SeetingManager.getSetting().get("name");
            System.out.println("用户名为："+name);
            if (jsonObject.getStr("userName").equals(name)) {
                return false;
            }
        }
        return true;
    }

    public static void sleepTime(int interval) throws InterruptedException {
        for (int i = interval; i >=0; i--) {
            Thread.sleep(1000);
            if(i <= 3 && i >=1){
                System.out.println(i+"秒后运行");
            }
        }
    }


}
