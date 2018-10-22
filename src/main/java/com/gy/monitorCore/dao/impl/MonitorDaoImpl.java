package com.gy.monitorCore.dao.impl;

import com.gy.monitorCore.dao.MonitorDao;
import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.ResourceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by gy on 2018/3/31.
 */
@Repository
public class MonitorDaoImpl implements MonitorDao {

//    @Autowired
//    @Qualifier("database")
//    Executor executor;

    @Autowired
    @PersistenceContext
    EntityManager em;

    @Override
    public TestEntity getJPAInfo() {
        List<TestEntity> result = em.createQuery("FROM TestEntity", TestEntity.class)
                .getResultList();
        if (result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public OperationMonitorEntity getOperationMonitorEntity(String uuid) {
        String sql = "From OperationMonitorEntity WHERE uuid = :uuid";
        return em.createQuery(sql, OperationMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
    }

    @Override
    public List<MiddleTypeEntity> getMiddleTypeEntity() {
        String sql = "From MiddleTypeEntity";
        return em.createQuery(sql, MiddleTypeEntity.class)
                .getResultList();
    }

    @Override
    public List<LightTypeEntity> getLightTypeEntity() {
        String sql = "From LightTypeEntity";
        return em.createQuery(sql, LightTypeEntity.class)
                .getResultList();
    }

    @Override
    @Transactional
    public boolean insertMonitorRecord(OperationMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OperationMonitorEntity> getAllMonitorByLightType(String lightType) {
        String sql = "From OperationMonitorEntity WHERE lightTypeId = :lightTypeId";
        return em.createQuery(sql, OperationMonitorEntity.class)
                .setParameter("lightTypeId", lightType)
                .getResultList();
    }

    @Override
    public boolean delMonitorRecord(String uuid) {
        String sql = "UPDATE OperationMonitorEntity SET deleted= 1 WHERE uuid= :uuid";
         int res = em.createQuery(sql,OperationMonitorEntity.class)
                .setParameter("uuid",uuid)
                .executeUpdate();
         if (res>0){
             return true;
         }else {
             return false;
         }
    }

    @Override
    public List<OperationMonitorEntity> getAllOperationMonitorEntity() {
        String sql = "From OperationMonitorEntity";
        return em.createQuery(sql, OperationMonitorEntity.class)
                .getResultList();
    }

    @Override
    public List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid) {
        String sql = "From OperationMonitorEntity WHERE extra like ?";
        return em.createQuery(sql, OperationMonitorEntity.class)
                .setParameter(1, "%"+uuid+"%")
                .getResultList();
    }
}
