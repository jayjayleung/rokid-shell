package org.rokid.shell.util;

import cn.hutool.setting.Setting;

/**
 * @Author: jayjay
 * @Date: 2024/4/4
 * @ClassName: SeetingManager
 * @Description:
 */
public class SettingManager {

    private static final Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");


    public static Setting getSetting() {
        return setting;
    }

    public static void main(String[] args) {
        System.out.println(setting.get("article_1","articleId"));
        System.out.println(setting.get("article_4","articleId"));
        System.out.println(setting.isEmpty("article_1"));
        System.out.println(setting.isEmpty("article_4"));

        setting.getGroups().forEach(System.out::println);
    }

}
