package com.xuecheng.fastdfs;


/**
 * @author Ying on 2020/4/18
 */
public class Main {
    public static void main(String[] args) {
        int r = f();
        System.out.println("qwertyui:+"      +r);
    }

    public static int f() {
        int ret = 0;
        try {
//            return ret;  // 返回 [1]，finally内的修改效果起了作用
            int a = 12 / 0;
            return 1;
        } catch (Exception e) {
            return 2;
        } finally {
            ret++;
            System.out.println("finally执行");
        }
    }
}
