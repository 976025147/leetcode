package com.jayden.leetcode;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//百度图片下载
public class BaiduPicDownloadTest {

    public static String doGetOhter(String url) throws Exception {
        //1,创建一个httpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //2,创建uriBuilder 对于httpClient4.3访问指定页面url必须要使用http://开始
        URIBuilder uriBuilder = new URIBuilder(url);
        //4,创建httpget对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //5,设置请求报文头部的编码
        httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; utf-8"));
        //6,设置期望服务返回的编码
        httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
        //7，请求服务
        CloseableHttpResponse response = client.execute(httpGet);
        //8，获取请求返回码
        int statusCode = response.getStatusLine().getStatusCode();
        //9如果请求返回码是200，则说明请求成功
        String str = "";
        if (statusCode == 200) {
            //10,获取返回实体
            HttpEntity entity = response.getEntity();
            //11,通过EntityUtils的一个工具类获取返回的内容
            str = EntityUtils.toString(entity);
        } else {
            System.out.println("请求失败！");
        }
        response.close();
        client.close();
        return str;
    }

    /**
     * @return void
     * @Author yangyue
     * @Description //TODO 查询百度图片
     * @Date 14:15 2019/11/14
     * @Param [Qury, bastnuber] 输入查询的关键词和下载的数量/30
     **/
    public static void downloadBiaduPic(String Qury, int bastnuber) {

        for (int i = 1; i < bastnuber + 1; i++) {
            String url = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=" + Qury + "&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=&hd=&latest=&copyright=&word=" + Qury + "&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&pn=" + i * 30 + "&rn=30&gsm=&666=";
            String josnString = null;
            try {
                josnString = doGetOhter(url);
                List<String> resultList = new ArrayList<String>();
                //将JSON转化为Map
                Map mapString = (Map) JSON.parse(josnString);
                JSONArray jsonArray = JSONObject.parseArray(mapString.get("data").toString());
                for (int g = 0; g < jsonArray.size(); g++) {
                    JSONObject partDaily = jsonArray.getJSONObject(g);
                    String jsonEntrylinks = partDaily.getString("thumbURL");
                    resultList.add(jsonEntrylinks);
                    downloadPicToFile(jsonEntrylinks);
                }
                System.out.println(resultList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void downloadPicToFile(String url) {
        try {
            if (url != null) {
                Files.write(Paths.get("D:\\pictures\\test\\" + System.currentTimeMillis() + url.substring(url.lastIndexOf("."))), IOUtils.toByteArray(URI.create(url)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        downloadBiaduPic("大熊猫", 1);
    }


}
