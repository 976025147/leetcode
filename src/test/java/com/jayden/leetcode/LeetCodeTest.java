package com.jayden.leetcode;

import com.jayden.leetcode.code.Solution;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeetCodeTest {

    Logger logger = LoggerFactory.getLogger(LeetCodeTest.class);

    @Test
    public void testLeetCode() {
        String[] words = {"abcdef", "abceee", "ab001231", "abc"};
        logger.info(new Solution().longestCommonPrefix(words));
    }


}
