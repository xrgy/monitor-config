package com.gy.monitorCore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.snmp.InterfaceInfo;
import com.gy.monitorCore.entity.snmp.LldpInfos;
import com.gy.monitorCore.entity.view.*;
import com.gy.monitorCore.entity.view.k8sView.Container;
import com.gy.monitorCore.entity.view.k8sView.Node;

import java.io.IOException;
import java.util.List;

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

    public String getOperationMonitorEntity(String uuid,String lightType) throws JsonProcessingException;

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
     boolean insertMonitorRecord(String data, String lightType) throws IOException;


    /**
     * 获取集群列表
     * @param model
     * @return
     */
//    List<Cluster> getClusterListByExporter(CasTransExporterModel model);

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
    boolean insertMonitorRecordList(String data, String lightType) throws IOException;



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
//    List<OperationMonitorEntity> getAllOperationMonitorEntity();

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
    String getMonitorRecordByTemplateId(String uuid, String lightType) throws JsonProcessingException;



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



    /**
     * 获取所有的网络设备 即根据三级规格是一样的
     * @return
     */
    public List<NetworkMonitorEntity> getAllNetworkMonitorEntity();



    /**
     * 获取所有的tomcat
     * @return
     */
    public List<TomcatMonitorEntity> getAllTomcatMonitorEntity();


    /**
     * 获取所有的mysql
     * @return
     */
    public List<DBMonitorEntity> getAllDbMonitorEntity();


    /**
     * 获取所有的cas
     * @return
     */
    public List<CasMonitorEntity> getAllCasMonitorEntity();


    /**
     * 获取所有的cvk
     * @return
     */
    public List<HostMonitorEntity> getAllHostMonitorEntity();


    /**
     * 获取所有的vm
     * @return
     */
    public List<VmMonitorEntity> getAllVmMonitorEntity();


    /**
     * 获取所有的k8s
     * @return
     */
    public List<K8sMonitorEntity> getAllK8sMonitorEntity();



    /**
     * 获取所有的k8snode
     * @return
     */
    public List<K8snodeMonitorEntity> getAllK8snodeMonitorEntity();



    /**
     * 获取所有的k8scontainer
     * @return
     */

    public List<K8scontainerMonitorEntity> getAllK8sContainerMonitorEntity();


    /**
     * 通过k8suuid获取其下所有的node和container
     * @param uuid
     * @return
     * @throws JsonProcessingException
     */
    public List<K8sNodeAndContainerView> getAllNodeAndContainerByK8suuid(String uuid) throws JsonProcessingException;

    /**
     * 通过k8snodeuuid获取其下的container列表
     * @param uuid
     * @return
     */
    public List<K8scontainerMonitorEntity> getAllContainerByK8sNodeuuid(String uuid);


    /**
     * 根据casuuid获取其下所有的cvk和vm
     * @param uuid
     * @return
     * @throws JsonProcessingException
     */
    public List<CvkAndVmView> getAllCvkAndVmByCasuuid(String uuid) throws JsonProcessingException;


    /**
     * 根据cvkuuid获取其下所有的vm
     * @param uuid
     * @return
     */
    public List<VmMonitorEntity> getAllVmByCvkuuid(String uuid);


    /**
     * 获取lldp信息 为topo提供接口
     * @return
     */
    LldpInfos getExporterLldpInfo();

    /**
     * 获取设备的网口信息
     * @param monitoruuid
     * @return
     */
    InterfaceInfo getExporterInterfaceInfo(String monitoruuid);


    NetworkMonitorEntity getNetworkMonitorEntity(String uuid);



    /**
     * ip是否重复
     * @param ip
     * @return
     */
    boolean isMonitorRecordIpDup(String ip,String lightType);


    /**
     * 获取设备端口流量
     * @param monitorUuid
     * @param quotaName
     * @return
     */
    QuotaInfo getInterfaceRate(String monitorUuid, String quotaName);

    PageBean getBusMonitorListByPage(PageData view);

    boolean isMonitorRecordIpDupNotP(String ip, String lightType, String uuid);

    AccessBackView dbCanAccess(DbAccessView view) throws JsonProcessingException;

    AccessBackView k8sCanAccess(K8sAccessView view) throws JsonProcessingException;

    AccessBackView TomcatAccessView(TomcatAccessView view) throws JsonProcessingException;
}
