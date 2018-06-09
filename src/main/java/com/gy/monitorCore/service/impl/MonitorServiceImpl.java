package com.gy.monitorCore.service.impl;

import com.gy.monitorCore.dao.MonitorDao;
import com.gy.monitorCore.entity.LightTypeEntity;
import com.gy.monitorCore.entity.MiddleTypeEntity;
import com.gy.monitorCore.entity.OperationMonitorEntity;
import com.gy.monitorCore.entity.TestEntity;
import com.gy.monitorCore.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletionStage;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    MonitorDao dao;

    @Override
    public TestEntity getJPAInfo() {
        return dao.getJPAInfo();
    }

    @Override
    public OperationMonitorEntity getOperationMonitorEntity(String uuid) {
        return dao.getOperationMonitorEntity(uuid);
    }

    @Override
    public List<MiddleTypeEntity> getMiddleTypeEntity() {
        return dao.getMiddleTypeEntity();
    }

    @Override
    public List<LightTypeEntity> getLightTypeEntity() {
        return dao.getLightTypeEntity();
    }
}
