package com.jayden.leetcode.util;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CollectionsUtil {
    private CollectionsUtil() {
    }

    public static List<Map<String, Object>> listToTree(List<Map<String, Object>> mapList, String idKey, String pidKey, String rootPValue, String childrenKey) {
        if (StringUtils.isEmpty(idKey) || StringUtils.isEmpty(pidKey) || null == mapList || mapList.isEmpty()) {
            return mapList;
        }
        List<Map<String, Object>> rootList = new ArrayList<>();
        //找到根结点
        for (Map<String, Object> item : mapList) {
            if ((null != rootPValue && null != item.get(pidKey) && rootPValue.equals(item.get(pidKey).toString()))
                    || (null == rootPValue && null == item.get(pidKey))) {
                rootList.add(item);
            }
        }
        if (rootList.isEmpty()) {
            return mapList;
        }
        //寻找下面的子节点
        for (Map<String, Object> item : rootList) {
            findChildrens(item, mapList, idKey, pidKey, item.get(idKey).toString(), childrenKey);
        }
        return rootList;
    }


    public static Map<String, Object> findChildrens(Map<String, Object> rootMap, List<Map<String, Object>> mapList, String idKey, String pidKey, String pidValue, String childrenKey) {
        if (mapList == null || mapList.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Map<String, Object>> childrenList = new ArrayList<>();
        for (Map<String, Object> item : mapList) {
            if ((pidValue == null && null == item.get(pidKey))
                    || (null != pidValue && null != item.get(pidKey) && pidValue.equals(item.get(pidKey).toString()))) {
                childrenList.add(findChildrens(item, mapList, idKey, pidKey, item.get(idKey).toString(), childrenKey));
            }
        }
        rootMap.put(childrenKey, childrenList);
        return rootMap;

    }
}
