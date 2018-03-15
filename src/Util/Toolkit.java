package Util;

/**
 * @program: Gobang
 * @description: 工具包
 * @author: Yang Yang
 * @create: 2018-03-15 19:28
 **/
public class Toolkit {

    /**
     * @Description: ip合法性检测
     * @Param : [text] ip
     * @return: boolean
     * @Author: Yang Yang
     * @Time: 19:31 2018/3/15
     **/
    public static boolean ipCheck(String text) {

        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }
}
