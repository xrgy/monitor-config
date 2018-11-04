package com.gy.monitorCore.dao;

import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.ResourceData;

import java.util.List;
import java.util.concurrent.CompletionStage;

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
     * 加入监控记录
     * @param entity
     * @return
     */
    boolean insertMonitorRecord(OperationMonitorEntity entity);

    /**
     * 根据三级规格获取监控对象
     * @param lightType
     * @return
     */
    List<OperationMonitorEntity> getAllMonitorByLightType(String lightType);

    /**
     * 通过uuid删除监控记录
     * @param uuid
     * @return
     */
    boolean delMonitorRecord(String uuid);

    /**
     * 获取所有的监控记录
     * @return
     */
    List<OperationMonitorEntity> getAllOperationMonitorEntity();

    /**
     *在extra中查找有该uuid的监控记录(parentId或rootId)
     * @param uuid
     * @return
     */
    List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid);


    /**
     *通过templateid获取监控设备
     * @param uuid
     * @return
     */
    List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid);
}
