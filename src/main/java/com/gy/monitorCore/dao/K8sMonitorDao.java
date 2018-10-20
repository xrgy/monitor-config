package com.gy.monitorCore.dao;

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
}
