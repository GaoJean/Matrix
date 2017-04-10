package com.baidu.disconf.web.utils.redmine;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.common.redmine.Issue;

/**
 * @version 2016-10-10
 * @author GaoJean
 *
 */
@Service
public class RedmineService {

    /**
     * 向 Redmine 发送信息（PUT方式）
     * @param desc
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void send2Redmine(String desc) throws ClientProtocolException, IOException {
        JSONObject jo = new JSONObject();
        Issue issue = new Issue();
        issue.setNotes(desc);
        jo.put("issue", issue);

        String url = Constants.REDMINE_URL + "?key=" + Constants.REDMINE_USER_KEY;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(url);
        put.setHeader("Content-type", "application/json;charset=utf-8");

        StringEntity params = new StringEntity(jo.toString(), "utf-8");
        put.setEntity(params);

        HttpResponse response = client.execute(put);
        System.out.println("Response Code:" + response.getStatusLine().getStatusCode());

    }

}
