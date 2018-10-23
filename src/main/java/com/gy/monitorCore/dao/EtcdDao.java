package com.gy.monitorCore.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by gy on 2018/3/31.
 */
public interface EtcdDao {


    /**
     * 插入更新etcd监控记录
     * @param etcdView
     * @param uuid
     * @return
     */
    boolean updateEtcdMonitor(MonitorEtcdView etcdView, String uuid) throws JsonProcessingException;

    /**
     * 根据monitortype获取exporter信息
     * @param monitorType
     * @return
     */
    String getExporterInfoByMonitorType(String monitorType) throws IOException;
}
