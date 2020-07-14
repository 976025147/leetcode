package com.jayden.leetcode.news;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TencentNewsControllerTest {
    @Resource
    TencentNewsServiceTest serviceTest;

    @RequestMapping("/loadNews")
    public Object loadNews(int page) {
        try {
            List data = serviceTest.loadData(page);
            Map resultMap = new HashMap();
            resultMap.put("data", data);
            resultMap.put("page", page);
            resultMap.put("size", data.size());
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
