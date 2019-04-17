package com.gy.monitorCore.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.view.AccessBackResult;
import com.gy.monitorCore.entity.view.DbAccessView;
import com.gy.monitorCore.entity.view.K8sAccessView;
import com.gy.monitorCore.entity.view.k8sView.Resource;

/**
 * Created by gy on 2018/10/19.
 */
public interface K8sMonitorDao {



    /**
     * 获取k8s资源列表
     * @param ip
     * @param port
     * @return
     */
    Resource getK8sResourceListByExporter(String ip, String port);

    AccessBackResult k8sCanAccess(K8sAccessView view) throws JsonProcessingException;

}
