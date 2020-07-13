package com.jayden.leetcode;

import com.jayden.leetcode.util.QrCodeUtil;
import org.junit.Test;

import java.awt.*;

public class UtilTest {

    @Test
    public void test() throws Exception {
        String msg = "测试字符串";
        String filePath = "F://";
        System.out.println(QrCodeUtil.qrCodeToFile(msg, 200, 200, 0, filePath, Color.RED));
    }

}
