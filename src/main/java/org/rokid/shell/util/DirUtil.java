package org.rokid.shell.util;

import java.io.File;

/**
 * @Author: jayjay
 * @Date: 2024/4/4
 * @ClassName: DirUtil
 * @Description: 目录工具类
 */
public class DirUtil {

    public static String getUserDir(){
        return System.getProperty("user.dir")+ File.separator;
    }
}
