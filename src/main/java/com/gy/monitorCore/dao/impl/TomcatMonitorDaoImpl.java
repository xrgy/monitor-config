package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.DbMonitorDao;
import com.gy.monitorCore.dao.TomcatonitorDao;
import com.gy.monitorCore.entity.view.AccessBackResult;
import com.gy.monitorCore.entity.view.DbAccessView;
import com.gy.monitorCore.entity.view.TomcatAccessView;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * Created by gy on 2018/10/19.
 */
@Repository
public class TomcatMonitorDaoImpl implements TomcatonitorDao {

    private static final String PORT = "9105";
    private static final String HTTP = "http://";


    private static final String PATH_TOMCAT_ACCESS = "api/v1/tomcat/access";

    private String tomcatExporterPrefix() {
        String ip = "";
        try {
            ip = "127.0.0.1";
//            ip = EtcdUtil.getClusterIpByServiceName("tomcat-exporter-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTTP + ip + ":" + PORT + "/" ;
//        return HTTP+"47.105.64.176"+":"+"30103"+"/";
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public AccessBackResult tomcatCanAccess(TomcatAccessView view) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monitor_info",view);
        return rest().postForEntity(tomcatExporterPrefix()+PATH_TOMCAT_ACCESS, jsonObject,AccessBackResult.class).getBody();
    }
}
