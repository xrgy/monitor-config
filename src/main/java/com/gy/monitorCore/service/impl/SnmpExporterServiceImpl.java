package com.gy.monitorCore.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.entity.snmp.InterfaceInfo;
import com.gy.monitorCore.entity.snmp.LldpInfos;
import com.gy.monitorCore.service.SnmpExporterService;
import com.gy.monitorCore.utils.EtcdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class SnmpExporterServiceImpl implements SnmpExporterService {

    private String PORT = "9106";
//    private String PORT = "9091";
    private String PREFIX = "api/v1";
    private static final String HTTP="http://";
    private static final String PATH_LLDP="lldp";
    private static final String PATH_DEVICE_INTERFACE_INFO="netdev/interface/";


    @Bean
    public RestTemplate rest(){
        return new RestTemplate();
    }

    @Autowired
    ObjectMapper objectMapper;

    private String snmpExporterPrefix(){
        String ip = "";
        try {
//            ip="47.94.157.199";
            ip = EtcdUtil.getClusterIpByServiceName("snmp-exporter-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HTTP+ip+":"+PORT+"/"+PREFIX+"/";
    }


    @Override
    public LldpInfos getExporterLldpInfo() {
        return rest().getForEntity(snmpExporterPrefix()+PATH_LLDP,LldpInfos.class).getBody();
    }

    @Override
    public InterfaceInfo getExporterInterfaceInfo(String monitoruuid) {
        return rest().getForEntity(snmpExporterPrefix()+PATH_DEVICE_INTERFACE_INFO+monitoruuid,InterfaceInfo.class).getBody();
    }
}
