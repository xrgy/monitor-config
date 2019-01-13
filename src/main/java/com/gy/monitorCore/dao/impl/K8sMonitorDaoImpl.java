package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.CasMonitorDao;
import com.gy.monitorCore.dao.K8sMonitorDao;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.NodeListView;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.entity.view.k8sView.Resource;
import com.gy.monitorCore.utils.EtcdUtil;
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
public class K8sMonitorDaoImpl implements K8sMonitorDao {

    private static final String PORT = "9109";
    private static final String HTTP = "http://";


    private static final String PATH_GET_KUBERNETES_RESOURCE_LIST = "api/v1/resources";

    private String containerExporterPrefix() {
        String ip = "";
        try {
            ip = "127.0.0.1";
//            ip = EtcdUtil.getClusterIpByServiceName("c-exporter-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTTP + ip + ":" + PORT + "/" ;
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Resource getK8sResourceListByExporter(String ip, String port) {
        ResponseEntity<String> response = rest().getForEntity(containerExporterPrefix() + PATH_GET_KUBERNETES_RESOURCE_LIST +
                "?master_ip={1}&api_port={2}", String.class, ip, port);
        try {
            return objectMapper.readValue(response.getBody(), Resource.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
