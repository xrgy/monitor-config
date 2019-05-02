package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.DbMonitorDao;
import com.gy.monitorCore.dao.K8sMonitorDao;
import com.gy.monitorCore.entity.snmp.InterfaceInfo;
import com.gy.monitorCore.entity.view.AccessBackResult;
import com.gy.monitorCore.entity.view.AccessBackView;
import com.gy.monitorCore.entity.view.DbAccessView;
import com.gy.monitorCore.entity.view.k8sView.Resource;
import com.gy.monitorCore.utils.EtcdUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gy on 2018/10/19.
 */
@Repository
public class DbMonitorDaoImpl implements DbMonitorDao {

    private static final String PORT = "9103";
    private static final String HTTP = "http://";


    private static final String PATH_DB_ACCESS = "api/v1/mysql/access";

    private String dbExporterPrefix() {
        String ip = "";
        try {
//            ip = "127.0.0.1";
            ip = EtcdUtil.getClusterIpByServiceName("dbexporter-service");
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
    public AccessBackResult dbCanAccess(DbAccessView view) throws JsonProcessingException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("monitorInfo",view);
//        Map<String,String> map = new HashMap<>();
//        map.put("ip",view.getIp());
//        map.put("username",view.getUsername());
//        map.put("password",view.getPassword());
//        map.put("databasename",view.getDatabasename());
//        map.put("port",view.getPort());
//        Map<String,String> map1= new HashMap<>();
//        map1.put("monitorInfo",objectMapper.writeValueAsString(map));

        return rest().postForEntity(dbExporterPrefix()+PATH_DB_ACCESS, view,AccessBackResult.class).getBody();
    }
}
