package com.gy.monitorCore.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.entity.view.k8sView.Resource;

import java.util.List;

/**
 * Created by gy on 2018/10/19.
 */
public interface CasMonitorDao {


    /**
     * 获取cas资源列表
     * @param model
     * @return
     */
    ResourceData getCasResourceListByExporter(CasTransExporterModel model);

    /**
     * 获取k8s资源列表
     * @param ip
     * @param port
     * @return
     */
    Resource getK8sResourceListByExporter(String ip,String port);
}
