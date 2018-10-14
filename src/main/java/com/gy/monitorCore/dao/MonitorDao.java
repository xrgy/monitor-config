package com.gy.monitorCore.dao;

import com.gy.monitorCore.entity.LightTypeEntity;
import com.gy.monitorCore.entity.MiddleTypeEntity;
import com.gy.monitorCore.entity.OperationMonitorEntity;
import com.gy.monitorCore.entity.TestEntity;

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
}
