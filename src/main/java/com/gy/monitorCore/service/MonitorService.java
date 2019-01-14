package com.gy.monitorCore.service;

import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.k8sView.Container;
import com.gy.monitorCore.entity.view.k8sView.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by gy on 2018/3/31.
 */
public interface MonitorService {
    public TestEntity getJPAInfo();

//    /**
//     * 通过uuid获取监控实体
//     * @param uuid
//     * @return
//     */
//    public OperationMonitorEntity getOperationMonitorEntity(String uuid);

    public String getOperationMonitorEntity(String uuid,String lightType);

//    /**
//     * 获取二级规格实体
//     * @param
//     * @return
//     */
//    public List<MiddleTypeEntity> getMiddleTypeEntity();
//
//
//    /**
//     * 通过uuid获取三级规格实体
//     * @param
//     * @return
//     */
//    public List<LightTypeEntity> getLightTypeEntity();

//    /**
//     * 插入监控记录
//     * @param entity
//     * @return
//     */
//    boolean insertMonitorRecord(OperationMonitorEntity entity) throws IOException;

    /**
     * 插入监控记录
     * @param data
     * @param lightType
     * @return
     */
     boolean insertMonitorRecord(String data, String lightType);


    /**
     * 获取集群列表
     * @param model
     * @return
     */
    List<Cluster> getClusterListByExporter(CasTransExporterModel model);

//    /**
//     * 插入监控记录列表
//     * @param list
//     * @return
//     */
//    boolean insertMonitorRecordList(List<OperationMonitorEntity> list);

    /**
     * 插入监控记录列表
     * @param data
     * @param lightType
     * @return
     */
    boolean insertMonitorRecordList(String data, String lightType);



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

//    /**
//     * 删除监控记录
//     * @param uuid
//     * @return
//     */
//    boolean delMonitorRecord(String uuid);

    /**
     * 删除监控记录
     * @param uuid
     * @param lightType
     * @return
     */
    boolean delMonitorRecord(String uuid, String lightType);


    /**
     * 获取全部的监控记录
     * @return
     */
    List<OperationMonitorEntity> getAllOperationMonitorEntity();

    /**
     *通过extra中查找有该uuid的监控记录(parentId或rootId)
     * @param uuid
     * @return
     */
    List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid);

//    /**
//     * 通过templateid获取监控设备
//     * @param uuid
//     * @return
//     */
//    List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid);


    /**
     * 通过templateid获取监控设备
     * @param uuid
     * @param lightType
     * @return
     */
    String getMonitorRecordByTemplateId(String uuid, String lightType);



    /**
     * 测试接口
     * @return
     */
    public String testgetClusterIp() throws IOException;

    /**
     * 根据monitoruuid和指标名获取指标值
     * @param monitorUUid
     * @param quotaName
     * @return
     */
    String getQuotaValueByName(String monitorUUid, String quotaName) throws IOException;


    /**
     * 根据monitor和名字获取指标名
     * @param monitorType
     * @param name
     * @return
     */
    String getQuotaNameByMonitorAndName(String monitorType,String name) throws IOException;


}
