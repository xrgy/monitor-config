package com.gy.monitorCore.service;

import com.gy.monitorCore.entity.snmp.InterfaceInfo;
import com.gy.monitorCore.entity.snmp.LldpInfos;

/**
 * Created by gy on 2018/3/31.
 */
public interface SnmpExporterService {


    /**
     * 从snmp exporter获取lldp信息
     * @return
     */
    LldpInfos getExporterLldpInfo();


    /**
     * 获取设备的网口信息
     * @param monitoruuid
     * @return
     */
    InterfaceInfo getExporterInterfaceInfo(String monitoruuid);



}
