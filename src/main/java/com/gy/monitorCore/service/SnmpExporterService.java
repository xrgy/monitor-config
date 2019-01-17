package com.gy.monitorCore.service;

import com.gy.monitorCore.entity.lldp.LldpInfos;

/**
 * Created by gy on 2018/3/31.
 */
public interface SnmpExporterService {


    /**
     * 从snmp exporter获取lldp信息
     * @return
     */
    LldpInfos getExporterLldpInfo();



}
