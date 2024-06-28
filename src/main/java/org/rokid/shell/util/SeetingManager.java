package org.rokid.shell.util;

import cn.hutool.setting.Setting;

/**
 * @Author liangshijie
 * @Date 2024/4/15
 * @Description:
 */
public class SeetingManager {

    private static final Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");


    public static Setting getSetting() {
        return setting;
    }

}
