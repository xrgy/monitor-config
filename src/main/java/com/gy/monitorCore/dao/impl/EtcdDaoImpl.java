package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.EtcdDao;
import com.gy.monitorCore.entity.MonitorEtcdView;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gy on 2018/10/23.
 */
@Repository
public class EtcdDaoImpl implements EtcdDao {

    private static final String IP="172.31.105.232";
    private static final String ETCD_PORT="2379";
    private static final String ETCD_PREFIX="v2/keys/gy";
    private static final String PATH_RESOURCE_MONITOR="prometheus/resource_monitor";
    private static final String PATH_EXPORETR_MAP="exporter-map";


    private String etcdPrefix() {
        return IP + ":" + ETCD_PORT + "/" + ETCD_PREFIX + "/";
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Autowired
    ObjectMapper objectMapper;
    @Override
    public boolean updateEtcdMonitor(MonitorEtcdView etcdView, String uuid) throws JsonProcessingException {
        rest().put(etcdPrefix()+PATH_RESOURCE_MONITOR+"/{1}?value={2}",null,uuid,objectMapper.writeValueAsString(etcdView));
        return true;
    }

    @Override
    public String getExporterInfoByMonitorType(String monitorType) throws IOException {
        Map<String,Object> response = rest().getForObject(IP+":"+ETCD_PORT+"/"+PATH_EXPORETR_MAP,HashMap.class);
        Map<String,String> exportermap = (Map<String, String>) response.get("node");
        String exporterinfo = exportermap.get("value");
        Map<String,String> map = objectMapper.readValue(exporterinfo,HashMap.class);
        if (map.keySet().contains(monitorType)){
            return map.get(monitorType);
        }
        return null;
    }

    @Override
    public boolean delEtcdMonitor(String uuid) {
        rest().delete(etcdPrefix()+PATH_RESOURCE_MONITOR+"/{1}",uuid);
        return true;
    }
}
