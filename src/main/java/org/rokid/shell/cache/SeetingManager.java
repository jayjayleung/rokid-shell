package org.rokid.shell.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.setting.Setting;
import org.rokid.shell.util.DirUtil;

/**
 * @Author liangshijie
 * @Date 2024/4/15
 * @Description:
 */
public class SeetingManager {

    private static final Setting setting = new Setting(DirUtil.getUserDir() + "config.setting"); // 缓存有效期为1分钟


    public static Setting getSetting() {
        return setting;
    }

}
