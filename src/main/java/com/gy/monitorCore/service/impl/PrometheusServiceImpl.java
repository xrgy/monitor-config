package com.gy.monitorCore.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.entity.InstantData;
import com.gy.monitorCore.entity.InstantValue;
import com.gy.monitorCore.entity.QuotaItemData;
import com.gy.monitorCore.entity.QuotaItemInfo;
import com.gy.monitorCore.service.PrometheusService;
import com.gy.monitorCore.utils.EtcdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class PrometheusServiceImpl implements PrometheusService {

//    private String PORT = "30091";
    private String PORT = "9091";
    private String PREFIX = "api/v1";
    private static final String HTTP="http://";
    private static final String PATH_SINGLE_DATA="query?query=";

    @Bean
    public RestTemplate rest(){
        return new RestTemplate();
    }

    @Autowired
    ObjectMapper objectMapper;

    private String prometheusPrefix(){
        String ip = "";
        try {
//            ip="47.105.64.176";
            ip = EtcdUtil.getClusterIpByServiceName("prometheus-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTTP+ip+":"+PORT+"/"+PREFIX+"/";
    }


    @Override
    public String getQuotaValue(String url) {
        InstantData instantData = rest().getForObject(prometheusPrefix()+PATH_SINGLE_DATA+"{1}", InstantData.class,url);
        if (instantData.getStatus().equals("success")){
            List<InstantValue> result = instantData.getData().getResult();
            if (result.size()==1){
                List<String> str = result.get(0).getValue();
                if (str.size()==2){
                    //第一个值时时间戳，第二个值是value
                    return str.get(1);
                }
            }
        }
        return null;
    }

    @Override
    public List<QuotaItemData> getInterfaceQuotaValue(String url) {
        InstantData instantData = rest().getForObject(prometheusPrefix()+PATH_SINGLE_DATA+"{1}", InstantData.class,url);
        if (instantData.getStatus().equals("success")){
            List<QuotaItemData> itemdata = new ArrayList<>();
            List<InstantValue> result = instantData.getData().getResult();
            result.forEach(x->{
                List<String> str = x.getValue();
                if (str.size()==2){
                    //第一个值时时间戳，第二个值是value
                    QuotaItemData data = new QuotaItemData();
                    data.setName(x.getMetric().get("ifDescr"));
                    data.setValue(str.get(1));
                    itemdata.add(data);
                }
            });
            return itemdata;
        }
        return null;
    }
}
