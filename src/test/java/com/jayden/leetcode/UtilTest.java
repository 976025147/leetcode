package com.jayden.leetcode;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class UtilTest {

    @Test
    public void test() throws Exception {
        byte[] bytes = "测试字符".getBytes(StandardCharsets.UTF_8);
        System.out.println(DigestUtils.md5Hex(bytes));
    }

}
