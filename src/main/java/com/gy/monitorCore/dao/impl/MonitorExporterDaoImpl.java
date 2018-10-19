package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.MonitorExporterDao;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.ResourceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Created by gy on 2018/10/19.
 */
@Repository
public class MonitorExporterDaoImpl implements MonitorExporterDao {
    private static final String IP = "http://127.0.0.1";
    private static final String MONITOR_PORT = "9107";
    private static final String PATH_GET_RESOURCE_LIST = "/v1/cas/resourceList";
    private static final String PATH_LIGHT="/v1/cas/clusterList";

    private String virtualExporterPrefix() {
        return IP + ":" + MONITOR_PORT;
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public ResourceData getCasResourceListByExporter(CasTransExporterModel model) {

        ResponseEntity<String> response = rest().getForEntity(virtualExporterPrefix()+PATH_GET_RESOURCE_LIST+
                "?ip={1}&port={2}&username={3}&password={4}",String.class,model.getIp(),model.getPort(),model.getUsername(),model.getPassword());
        try {
            return objectMapper.readValue(response.getBody(),ResourceData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
