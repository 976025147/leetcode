package com.jayden.leetcode.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Jayden
 */
public class ThreadCommunication {

    public static int[] ARR_1 = new int[26];
    public static char[] ARR_2 = new char[26];
    static Thread thread1 = null;
    static Thread thread2 = null;

    public static void main(String[] args) {
        //初始化值
        for (int i = 0; i < 26; i++) {
            ARR_1[i] = (i + 1);
            ARR_2[i] = (char) (i + 'A');
        }

        thread1 = new Thread(() -> {
            for (int s : ARR_1) {
                System.out.print(s);
                LockSupport.unpark(thread2);
                LockSupport.park();
            }
        });

        thread2 = new Thread(() -> {
            for (char s : ARR_2) {
                LockSupport.park();
                System.out.println(s);
                LockSupport.unpark(thread1);
            }
        });

        //启动线程
        thread1.start();
        thread2.start();
    }

}
