package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.CasMonitorDao;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.entity.view.k8sView.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by gy on 2018/10/19.
 */
@Repository
public class CasMonitorDaoImpl implements CasMonitorDao {
//    private static final String IP = "http://127.0.0.1";
//    private static final String CAS_PORT = "9107";
    private static final String IP = "http://172.31.105.232";
//    private static final String IP = "http://172.17.5.135";
    private static final String CAS_PORT = "30107";
    private static final String PATH_GET_RESOURCE_LIST = "/v1/cas/resourceList";
    private static final String PATH_LIGHT="/v1/cas/clusterList";
    private static final String PATH_GET_KUBERNETES_RESOURCE_LIST="/api/v1/resources";

    private String virtualExporterPrefix() {
        return IP + ":" + CAS_PORT;
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

    @Override
    public Resource getK8sResourceListByExporter(String ip, String port) {

        return null;
    }
}
