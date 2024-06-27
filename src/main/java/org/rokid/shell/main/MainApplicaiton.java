package org.rokid.shell.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.AbsSetting;
import cn.hutool.setting.Setting;
import org.rokid.shell.cache.SeetingManager;
import org.rokid.shell.util.DirUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: jayjay
 * @Date: 2024/4/1
 * @ClassName: MainApplicaiton
 * @Description:
 */
public class MainApplicaiton {

    public static void main(String[] args) {
        Setting setting = SeetingManager.getSetting();
        String tokenPath = setting.getStrNotEmpty("tokenPath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "tokens.txt");
        // String threshold = setting.getStrNotEmpty("threshold", AbsSetting.DEFAULT_GROUP, "0.5");
        // System.out.println(threshold);

        File file = FileUtil.touch(tokenPath);
        boolean flag = true;
        while (flag) {
            // clearConsole();
            String action = getAction();
            switch (action) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "9":

                    break;
                case "0":
                    flag = false;
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }


    }

    public static String getAction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===========================================================================");
        System.out.println("===========================================================================");
        System.out.println("*请按照config.setting设置好对应的参数带*是必填项" );
        System.out.println("*注意：浏览器驱动一定要下载和当前谷歌浏览器对应版本号的驱动，" );
        System.out.println("*查看版本号方式：" );
        System.out.println("*  1.浏览器地址栏输入：chrome://version/" );
        System.out.println("*  2.点击浏览器右上角三个点，设置->关于关于 Chrome 即可查看" );
        System.out.println("*驱动下载地址为(需要科学上网)：https://chromedriver.com/download" );
        System.out.println("*注意：token文件路径是一行一个toke,输入后请按回车再输入下一行，否则视为同一个token" );
        System.out.println("*注意：token如果需要设置单独的地狱地板价百分比的,在后面加\"-\",百分比是小数点,50%就是0.5,如：eth-0.2" );
        System.out.println("请输入序号："); // 打印提示
        System.out.println("1：查看配置"); // 打印提示
        System.out.println("2：追加一个token"); // 打印提示
        System.out.println("3：列出所有token"); // 打印提示
        System.out.println("4：初始化钱包"); // 打印提示
        System.out.println("5：开始监控"); // 打印提示
        // System.out.println("9：修改配置"); // 打印提示
        System.out.println("0：退出"); // 打印提示
        System.out.println("===========================================================================");
        System.out.println("===========================================================================");
        System.out.printf(":");
        String action = scanner.nextLine();
        return action;
    }


    public static String getStr() {
        Scanner scanner = new Scanner(System.in);
        String str = "";
        if (scanner.hasNextLine()) {
            str = scanner.nextLine();
            if (StrUtil.isBlank(str)) {
                System.err.print("不能为空，请重新输入：");
                getStr();
            }
        }
        return str;
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
