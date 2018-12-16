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

//    private static final String IP="47.105.64.176";
    private static final String IP="47.94.157.199";

    //    private static final String IP="172.31.105.232";
    private static final String ETCD_PORT="2379";
    private static final String ETCD_PREFIX="v2/keys/gy";
    private static final String PATH_SERVICE_INFO="v2/keys/registry/services/specs/default/";
    private static final String PATH_RESOURCE_MONITOR="prometheus/resource_monitor";
    private static final String PATH_EXPORETR_MAP="exporter-map";
    private static final String HTTP="http://";


    private String etcdPrefix() {
        return HTTP+IP + ":" + ETCD_PORT + "/" + ETCD_PREFIX + "/";
    }

    private String etcdlocalPrefix(){
        return HTTP+IP + ":" + ETCD_PORT + "/";
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
        String response = rest().getForObject(etcdPrefix()+PATH_EXPORETR_MAP,String.class);
        Map<String,Object> resmap = objectMapper.readValue(response,HashMap.class);
        Map<String,String> exportermap = (Map<String, String>) resmap.get("node");
        String exporterinfo = exportermap.get("value");
        Map<String,String> map = objectMapper.readValue(exporterinfo,HashMap.class);
        if (map.keySet().contains(monitorType)){
            String servicenameAndPort = map.get(monitorType);
            String[] str =servicenameAndPort.split(":");
            String clusterIp = getClusterIpByServiceName(str[0]);
            return clusterIp+":"+str[1];

        }
        return null;
    }

    @Override
    public boolean delEtcdMonitor(String uuid) {
        rest().delete(etcdPrefix()+PATH_RESOURCE_MONITOR+"/{1}",uuid);
        return true;
    }

    public String getClusterIpByServiceName(String serviceName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject(etcdlocalPrefix()+PATH_SERVICE_INFO+serviceName,String.class);
        Map<String,Object> resmap = objectMapper.readValue(response,HashMap.class);
        Map<String,String> nodeMap = (Map<String, String>) resmap.get("node");
        String cont =  nodeMap.get("value");
        Map<String,Object> contMap = objectMapper.readValue(cont,HashMap.class);
        Map<String,String> specMap = (Map<String, String>) contMap.get("spec");
        String clusterIP = specMap.get("clusterIP");
        return clusterIP;
    }
}
