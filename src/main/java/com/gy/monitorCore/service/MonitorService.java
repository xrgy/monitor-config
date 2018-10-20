package com.gy.monitorCore.service;

import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.k8sView.Container;
import com.gy.monitorCore.entity.view.k8sView.Node;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by gy on 2018/3/31.
 */
public interface MonitorService {
    public TestEntity getJPAInfo();

    /**
     * 通过uuid获取监控实体
     * @param uuid
     * @return
     */
    public OperationMonitorEntity getOperationMonitorEntity(String uuid);


    /**
     * 获取二级规格实体
     * @param
     * @return
     */
    public List<MiddleTypeEntity> getMiddleTypeEntity();


    /**
     * 通过uuid获取三级规格实体
     * @param
     * @return
     */
    public List<LightTypeEntity> getLightTypeEntity();

    /**
     * 插入监控记录
     * @param entity
     * @return
     */
    boolean insertMonitorRecord(OperationMonitorEntity entity);

    /**
     * 获取集群列表
     * @param model
     * @return
     */
    List<Cluster> getClusterListByExporter(CasTransExporterModel model);

    /**
     * 插入监控记录列表
     * @param list
     * @return
     */
    boolean insertMonitorRecordList(List<OperationMonitorEntity> list);

    /**
     * 获取cvk和vm列表
     * @param model
     * @return
     */
    List<Host> getCvkAndVmListByExporter(CasTransExporterModel model);

    /**
     * 获取容器列表
     */
    List<Container> getContainerListByExporter(String ip,String port);

    /**
     * 获取Node列表
     */
    List<Node> getNodeListByExporter(String ip,String port);
}
