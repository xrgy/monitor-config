package com.gy.monitorCore.dao;

import com.gy.monitorCore.entity.*;

import java.util.List;

/**
 * Created by gy on 2018/3/31.
 */
public interface MonitorDao {
    public TestEntity getJPAInfo();


    /**
     * uuid获取监控实体
     * @param uuid
     * @return
     */
//    public OperationMonitorEntity getOperationMonitorEntity(String uuid);


    /**
     * uuid获取网络设备监控实体
     * @param uuid
     * @return
     */
    public NetworkMonitorEntity getNetworkMonitorEntity(String uuid);


    /**
     * 获取所有的网络设备 即根据三级规格是一样的
     * @return
     */
    public List<NetworkMonitorEntity> getAllNetworkMonitorEntity();


    /**
     * uuid获取Tomcat设备监控实体
     * @param uuid
     * @return
     */
    public TomcatMonitorEntity getTomcatMonitorEntity(String uuid);


    /**
     * 获取所有的tomcat
     * @return
     */
    public List<TomcatMonitorEntity> getAllTomcatMonitorEntity();

    /**
     * uuid获取Mysql设备监控实体
     * @param uuid
     * @return
     */
    public DBMonitorEntity getDbMonitorEntity(String uuid);


    /**
     * 获取所有的mysql
     * @return
     */
    public List<DBMonitorEntity> getAllDbMonitorEntity();


    /**
     * uuid获取Cas监控实体
     * @param uuid
     * @return
     */
    public CasMonitorEntity getCasMonitorEntity(String uuid);


    /**
     * 获取所有的cas
     * @return
     */
    public List<CasMonitorEntity> getAllCasMonitorEntity();


    /**
     * uuid获取Host cvk监控实体
     * @param uuid
     * @return
     */
    public HostMonitorEntity getHostMonitorEntity(String uuid);


    /**
     * 获取所有的cvk
     * @return
     */
    public List<HostMonitorEntity> getAllHostMonitorEntity();



    /**
     * uuid获取vm监控实体
     * @param uuid
     * @return
     */
    public VmMonitorEntity getVmMonitorEntity(String uuid);


    /**
     * 获取所有的vm
     * @return
     */
    public List<VmMonitorEntity> getAllVmMonitorEntity();



    /**
     * uuid获取k8a监控实体
     * @param uuid
     * @return
     */
    public K8sMonitorEntity getK8sMonitorEntity(String uuid);


    /**
     * 获取所有的k8s
     * @return
     */
    public List<K8sMonitorEntity> getAllK8sMonitorEntity();



    /**
     * uuid获取k8snode监控实体
     * @param uuid
     * @return
     */
    public K8snodeMonitorEntity getK8snodeMonitorEntity(String uuid);


    /**
     * 获取所有的k8snode
     * @return
     */
    public List<K8snodeMonitorEntity> getAllK8snodeMonitorEntity();



    /**
     * uuid获取k8scontainer监控实体
     * @param uuid
     * @return
     */
    public K8scontainerMonitorEntity getK8sContainerMonitorEntity(String uuid);


    /**
     * 获取所有的k8scontainer
     * @return
     */

    public List<K8scontainerMonitorEntity> getAllK8sContainerMonitorEntity();




//    /**
//     * 获取二级规格实体
//     * @param
//     * @return
//     */
//    public List<MiddleTypeEntity> getMiddleTypeEntity();
//
//    /**
//     * 通过uuid获取三级规格实体
//     * @param
//     * @return
//     */
//    public List<LightTypeEntity> getLightTypeEntity();
//

    /**
     * 加入监控记录
     * @param entity
     * @return
     */
//    boolean insertMonitorRecord(OperationMonitorEntity entity);


    /**
     * 网络设备加入监控记录
     * @param entity
     * @return
     */
    boolean insertNetworkMonitorEntity(NetworkMonitorEntity entity);

    /**
     * Tomcat加入监控记录
     * @param entity
     * @return
     */
    boolean insertTomcatMonitorEntity(TomcatMonitorEntity entity);


    /**
     * mysql加入监控记录
     * @param entity
     * @return
     */
    boolean insertDbMonitorEntity(DBMonitorEntity entity);

    /**
     * Cas加入监控记录
     * @param entity
     * @return
     */
    boolean insertCasMonitorEntity(CasMonitorEntity entity);


    /**
     * cvk加入监控记录
     * @param entity
     * @return
     */
    boolean insertHostMonitorEntity(HostMonitorEntity entity);

    /**
     * vm加入监控记录
     * @param entity
     * @return
     */
    boolean insertVmMonitorEntity(VmMonitorEntity entity);


    /**
     * k8s加入监控记录
     * @param entity
     * @return
     */
    boolean insertK8sMonitorEntity(K8sMonitorEntity entity);


    /**
     * k8snode加入监控记录
     * @param entity
     * @return
     */
    boolean insertK8snodeMonitorEntity(K8snodeMonitorEntity entity);


    /**
     * k8sContainer加入监控记录
     * @param entity
     * @return
     */
    boolean insertK8sContainerMonitorEntity(K8scontainerMonitorEntity entity);




//    /**
//     * 根据三级规格获取监控对象
//     * @param lightType
//     * @return
//     */
//    List<OperationMonitorEntity> getAllMonitorByLightType(String lightType);

    /**
     * 通过uuid删除监控记录
     * @param uuid
     * @return
     */
//    boolean delMonitorRecord(String uuid);


    /**
     * 删除网络设备监控记录
     * @param uuid
     * @return
     */
    boolean delNetworkMonitorRecord(String uuid);


    /**
     * 删除Tomcat监控记录
     * @param uuid
     * @return
     */
    boolean delTomcatMonitorRecord(String uuid);


    /**
     * 删除mysql监控记录
     * @param uuid
     * @return
     */
    boolean delDbMonitorRecord(String uuid);


    /**
     * 删除Cas监控记录
     * @param uuid
     * @return
     */
    boolean delCasMonitorRecord(String uuid);


    /**
     * 删除cvk监控记录
     * @param uuid
     * @return
     */
    boolean delCvkMonitorRecord(String uuid);

    /**
     * 删除vm监控记录
     * @param uuid
     * @return
     */
    boolean delVmMonitorRecord(String uuid);

    /**
     * 删除K8s监控记录
     * @param uuid
     * @return
     */
    boolean delK8sMonitorRecord(String uuid);

    /**
     * 删除k8snode监控记录
     * @param uuid
     * @return
     */
    boolean delK8snodeMonitorRecord(String uuid);

    /**
     * 删除K8sContainer监控记录
     * @param uuid
     * @return
     */
    boolean delK8sContainerMonitorRecord(String uuid);



    /**
     * 获取所有的监控记录
     * @return
     */
//    List<OperationMonitorEntity> getAllOperationMonitorEntity();

    /**
     *在extra中查找有该uuid的监控记录(parentId或rootId)
     * @param uuid
     * @return
     */
    List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid);


//    /**
//     *通过templateid获取监控设备
//     * @param uuid
//     * @return
//     */
//    List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<NetworkMonitorEntity> getNetworkMonitorRecordByTemplateId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<TomcatMonitorEntity> getTomcatMonitorRecordByTemplateId(String uuid);



    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<DBMonitorEntity> getDbMonitorRecordByTemplateId(String uuid);



    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<CasMonitorEntity> getCasMonitorRecordByTemplateId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<HostMonitorEntity> getHostMonitorRecordByTemplateId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<VmMonitorEntity> getVmMonitorRecordByTemplateId(String uuid);

    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<K8sMonitorEntity> getK8sMonitorRecordByTemplateId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<K8snodeMonitorEntity> getK8sNodeMonitorRecordByTemplateId(String uuid);

    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<K8scontainerMonitorEntity> getK8sContainerMonitorRecordByTemplateId(String uuid);


    List<K8snodeMonitorEntity> getK8sNodeMonitorRecordByK8sUuid(String uuid);


    List<K8scontainerMonitorEntity> getK8sContainerMonitorRecordByK8sNodeUuid(String uuid);

    List<HostMonitorEntity> getCvkMonitorRecordByCasUuid(String uuid);

    List<VmMonitorEntity> getVmMonitorRecordByCvkUuid(String uuid);


    /**
     * 网络设备的ip是否重复
     * @param ip
     * @return
     */
    boolean isNetworkIpDup(String ip);

    /**
     * k8s的ip是否重复
     * @param ip
     * @return
     */
    boolean isK8sIpDup(String ip);


    /**
     * Cas的ip是否重复
     * @param ip
     * @return
     */
    boolean isCasIpDup(String ip);


    boolean isNetworkIpDupNotP(String ip, String uuid);

    boolean isCasIpDupNotP(String ip, String uuid);

    boolean isK8sIpDupNotP(String ip, String uuid);
}
