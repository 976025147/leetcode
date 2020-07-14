package com.jayden.leetcode.news;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TencentNewsServiceTest {

    public List<Tencent> loadData(int page) throws Exception {
        return loadPage(page);
    }

    private List<Tencent> loadPage(int page) throws Exception {
        String urlTencent = "https://pacaio.match.qq.com/irs/rcd?cid=135&token=6e92c215fb08afa901ac31eca115a34f&ext=world&page=" + page + "&expIds=&callback=__jp4";
        //确定路径
        //String urlTencent = "https://pacaio.match.qq.com/irs/rcd?cid=89&token=4d4e2946f92c5708f32141479596d72e&id=&ext=bj&page="+page+"&expIds=&callback=__jp0";
        List<Tencent> tencentList = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(urlTencent);
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            Gson gson = new Gson();
            //转换
            String html = EntityUtils.toString(httpEntity);
            //得到json
            String json = parseJson(html);
            //转换成map
            Map map = gson.fromJson(json, Map.class);
            //判断有多少数据，然后退出循环
            Object num = map.get("datanum");
            String nums = num.toString();
            Double double1 = Double.parseDouble(nums);
            int number = double1.intValue();

            if (number == 0) {
                return tencentList;
            }

            //得到页面的data
            List<Map> list = (List<Map>) map.get("data");


            //遍历集合
            for (Map map2 : list) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Tencent tencent = new Tencent();
                String title = map2.get("title").toString();
                String intro = map2.get("intro").toString();
                String turl = map2.get("vurl").toString();
                String img = map2.get("img").toString();
                String mediaIcon = map2.get("media_icon").toString();
                String source = map2.get("source").toString();
                Date publishTime = simpleDateFormat.parse(map2.get("publish_time").toString());


                tencent.setTitle(title);
                tencent.setUrl(turl);
                tencent.setIntro(intro);
                tencent.setSource(source);
                tencent.setImg(img);
                tencent.setMediaIcon(mediaIcon);
                tencent.setPublishTime(publishTime);
                tencentList.add(tencent);
            }
            addNews(tencentList);
        }
        return tencentList;
    }

    @Resource
    JdbcTemplate jdbcTemplate;

    private void addNews(List<Tencent> tencentList) {
        StringBuilder sql = new StringBuilder("insert IGNORE  into  t_tencent (title,intro,url,source,publish_time,img,media_icon) values  ");
        for (int i = 0; i < tencentList.size(); i++) {
            sql.append("(?,?,?,?,?,?,?),");
        }
        Object[] params = new Object[tencentList.size() * 7];
        for (int i = 0; i < tencentList.size(); i++) {
            params[i * 7 + 0] = tencentList.get(i).getTitle();
            params[i * 7 + 1] = tencentList.get(i).getIntro();
            params[i * 7 + 2] = tencentList.get(i).getUrl();
            params[i * 7 + 3] = tencentList.get(i).getSource();
            params[i * 7 + 4] = tencentList.get(i).getPublishTime();
            params[i * 7 + 5] = tencentList.get(i).getImg();
            params[i * 7 + 6] = tencentList.get(i).getMediaIcon();
        }
        jdbcTemplate.update(sql.substring(0, sql.length() - 1), params);

    }

    private static String parseJson(String data) {
        int start = data.indexOf("(");
        int end = data.lastIndexOf(")");
        String html = data.substring(start + 1, end);
        return html;

    }
}
