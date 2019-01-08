package com.gy.monitorCore.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.entity.InstantData;
import com.gy.monitorCore.service.PrometheusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class PrometheusServiceImpl implements PrometheusService {

    private String PORT = "30091";
//    private String PORT = "9091";
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
            ip="47.94.157.199";
//            ip = EtcdUtil.getClusterIpByServiceName("prometheus-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTTP+ip+":"+PORT+"/"+PREFIX+"/";
    }


    @Override
    public String getQuotaValue(String url) {
        InstantData instantData = rest().getForObject(prometheusPrefix()+PATH_SINGLE_DATA+url, InstantData.class);
        return null;
    }
}
