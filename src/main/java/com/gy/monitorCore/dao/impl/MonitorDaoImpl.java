package com.gy.monitorCore.dao.impl;

import com.gy.monitorCore.dao.MonitorDao;
import com.gy.monitorCore.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public NetworkMonitorEntity getNetworkMonitorEntity(String uuid) {
        String sql = "From NetworkMonitorEntity WHERE uuid = :uuid";
        List<NetworkMonitorEntity> list = em.createQuery(sql, NetworkMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NetworkMonitorEntity> getAllNetworkMonitorEntity() {
        String sql = "From NetworkMonitorEntity";
        return em.createQuery(sql, NetworkMonitorEntity.class)
                .getResultList();
    }

    @Override
    public TomcatMonitorEntity getTomcatMonitorEntity(String uuid) {
        String sql = "From TomcatMonitorEntity WHERE uuid = :uuid";
        List<TomcatMonitorEntity> list = em.createQuery(sql, TomcatMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TomcatMonitorEntity> getAllTomcatMonitorEntity() {
        String sql = "From TomcatMonitorEntity";
        return em.createQuery(sql, TomcatMonitorEntity.class)
                .getResultList();
    }

    @Override
    public DBMonitorEntity getDbMonitorEntity(String uuid) {
        String sql = "From DBMonitorEntity WHERE uuid = :uuid";
        List<DBMonitorEntity> list = em.createQuery(sql, DBMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DBMonitorEntity> getAllDbMonitorEntity() {
        String sql = "From DBMonitorEntity";
        return em.createQuery(sql, DBMonitorEntity.class)
                .getResultList();
    }

    @Override
    public CasMonitorEntity getCasMonitorEntity(String uuid) {
        String sql = "From CasMonitorEntity WHERE uuid = :uuid";
        List<CasMonitorEntity> list = em.createQuery(sql, CasMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<CasMonitorEntity> getAllCasMonitorEntity() {
        String sql = "From CasMonitorEntity";
        return em.createQuery(sql, CasMonitorEntity.class)
                .getResultList();
    }

    @Override
    public HostMonitorEntity getHostMonitorEntity(String uuid) {
        String sql = "From HostMonitorEntity WHERE uuid = :uuid";
        List<HostMonitorEntity> list = em.createQuery(sql, HostMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HostMonitorEntity> getAllHostMonitorEntity() {
        String sql = "From HostMonitorEntity";
        return em.createQuery(sql, HostMonitorEntity.class)
                .getResultList();
    }

    @Override
    public VmMonitorEntity getVmMonitorEntity(String uuid) {
        String sql = "From VmMonitorEntity WHERE uuid = :uuid";
        List<VmMonitorEntity> list = em.createQuery(sql, VmMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<VmMonitorEntity> getAllVmMonitorEntity() {
        String sql = "From VmMonitorEntity";
        return em.createQuery(sql, VmMonitorEntity.class)
                .getResultList();
    }

    @Override
    public K8sMonitorEntity getK8sMonitorEntity(String uuid) {
        String sql = "From K8sMonitorEntity WHERE uuid = :uuid";
        List<K8sMonitorEntity> list = em.createQuery(sql, K8sMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<K8sMonitorEntity> getAllK8sMonitorEntity() {
        String sql = "From K8sMonitorEntity";
        return em.createQuery(sql, K8sMonitorEntity.class)
                .getResultList();
    }

    @Override
    public K8snodeMonitorEntity getK8snodeMonitorEntity(String uuid) {
        String sql = "From K8snodeMonitorEntity WHERE uuid = :uuid";
        List<K8snodeMonitorEntity> list = em.createQuery(sql, K8snodeMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<K8snodeMonitorEntity> getAllK8snodeMonitorEntity() {
        String sql = "From K8snodeMonitorEntity";
        return em.createQuery(sql, K8snodeMonitorEntity.class)
                .getResultList();
    }

    @Override
    public K8scontainerMonitorEntity getK8sContainerMonitorEntity(String uuid) {
        String sql = "From K8scontainerMonitorEntity WHERE uuid = :uuid";
        List<K8scontainerMonitorEntity> list = em.createQuery(sql, K8scontainerMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<K8scontainerMonitorEntity> getAllK8sContainerMonitorEntity() {
        String sql = "From K8scontainerMonitorEntity";
        return em.createQuery(sql, K8scontainerMonitorEntity.class)
                .getResultList();
    }

    @Override
    @Transactional
    public boolean insertNetworkMonitorEntity(NetworkMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertTomcatMonitorEntity(TomcatMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertDbMonitorEntity(DBMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertCasMonitorEntity(CasMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertHostMonitorEntity(HostMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertVmMonitorEntity(VmMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertK8sMonitorEntity(K8sMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertK8snodeMonitorEntity(K8snodeMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean insertK8sContainerMonitorEntity(K8scontainerMonitorEntity entity) {
        try {
            em.merge(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    @Modifying
    public boolean delNetworkMonitorRecord(String uuid) {
        String sql = "DELETE FROM NetworkMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delTomcatMonitorRecord(String uuid) {
        String sql = "DELETE FROM TomcatMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delDbMonitorRecord(String uuid) {
        String sql = "DELETE FROM DBMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delCasMonitorRecord(String uuid) {
        String sql = "DELETE FROM CasMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delCvkMonitorRecord(String uuid) {
        String sql = "DELETE FROM HostMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delVmMonitorRecord(String uuid) {
        String sql = "DELETE FROM VmMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delK8sMonitorRecord(String uuid) {
        String sql = "DELETE FROM K8sMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delK8snodeMonitorRecord(String uuid) {
        String sql = "DELETE FROM K8snodeMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delK8sContainerMonitorRecord(String uuid) {
        String sql = "DELETE FROM K8scontainerMonitorEntity WHERE uuid =:uuid";
        int res = em.createQuery(sql)
                .setParameter("uuid", uuid)
                .executeUpdate();
        return res > 0;
    }

//    @Override
//    public OperationMonitorEntity getOperationMonitorEntity(String uuid) {
//        String sql = "From OperationMonitorEntity WHERE uuid = :uuid";
//        return em.createQuery(sql, OperationMonitorEntity.class)
//                .setParameter("uuid", uuid)
//                .getSingleResult();
//    }
//
//    @Override
//    public List<MiddleTypeEntity> getMiddleTypeEntity() {
//        String sql = "From MiddleTypeEntity";
//        return em.createQuery(sql, MiddleTypeEntity.class)
//                .getResultList();
//    }
//
//    @Override
//    public List<LightTypeEntity> getLightTypeEntity() {
//        String sql = "From LightTypeEntity";
//        return em.createQuery(sql, LightTypeEntity.class)
//                .getResultList();
//    }
//
//    @Override
//    @Transactional
//    public boolean insertMonitorRecord(OperationMonitorEntity entity) {
//        try {
//            em.merge(entity);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public List<OperationMonitorEntity> getAllMonitorByLightType(String lightType) {
//        String sql = "From OperationMonitorEntity WHERE lightTypeId = :lightTypeId";
//        return em.createQuery(sql, OperationMonitorEntity.class)
//                .setParameter("lightTypeId", lightType)
//                .getResultList();
//    }
//
//    @Override
//    public boolean delMonitorRecord(String uuid) {
//        String sql = "UPDATE OperationMonitorEntity SET deleted= 1 WHERE uuid= :uuid";
//         int res = em.createQuery(sql,OperationMonitorEntity.class)
//                .setParameter("uuid",uuid)
//                .executeUpdate();
//         if (res>0){
//             return true;
//         }else {
//             return false;
//         }
//    }
//
//    @Override
//    public List<OperationMonitorEntity> getAllOperationMonitorEntity() {
//        String sql = "From OperationMonitorEntity";
//        return em.createQuery(sql, OperationMonitorEntity.class)
//                .getResultList();
//    }

    @Override
    public List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid) {
        String sql = "From OperationMonitorEntity WHERE extra like ?";
        return em.createQuery(sql, OperationMonitorEntity.class)
                .setParameter(1, "%" + uuid + "%")
                .getResultList();
    }

    @Override
    public List<NetworkMonitorEntity> getNetworkMonitorRecordByTemplateId(String uuid) {
        String sql = "From NetworkMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, NetworkMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<TomcatMonitorEntity> getTomcatMonitorRecordByTemplateId(String uuid) {
        String sql = "From TomcatMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, TomcatMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<DBMonitorEntity> getDbMonitorRecordByTemplateId(String uuid) {
        String sql = "From DBMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, DBMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<CasMonitorEntity> getCasMonitorRecordByTemplateId(String uuid) {
        String sql = "From CasMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, CasMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<HostMonitorEntity> getHostMonitorRecordByTemplateId(String uuid) {
        String sql = "From HostMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, HostMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<VmMonitorEntity> getVmMonitorRecordByTemplateId(String uuid) {
        String sql = "From VmMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, VmMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<K8sMonitorEntity> getK8sMonitorRecordByTemplateId(String uuid) {
        String sql = "From K8sMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, K8sMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<K8snodeMonitorEntity> getK8sNodeMonitorRecordByTemplateId(String uuid) {
        String sql = "From K8snodeMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, K8snodeMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<K8scontainerMonitorEntity> getK8sContainerMonitorRecordByTemplateId(String uuid) {
        String sql = "From K8scontainerMonitorEntity WHERE templateId=:uuid";
        return em.createQuery(sql, K8scontainerMonitorEntity.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }

    @Override
    public List<K8snodeMonitorEntity> getK8sNodeMonitorRecordByK8sUuid(String uuid) {
        String sql = "From K8snodeMonitorEntity WHERE k8sUuid=:k8sUuid";
        return em.createQuery(sql, K8snodeMonitorEntity.class)
                .setParameter("k8sUuid", uuid)
                .getResultList();
    }

    @Override
    public List<K8scontainerMonitorEntity> getK8sContainerMonitorRecordByK8sNodeUuid(String uuid) {
        String sql = "From K8scontainerMonitorEntity WHERE k8snodeUuid=:k8snodeUuid";
        return em.createQuery(sql, K8scontainerMonitorEntity.class)
                .setParameter("k8snodeUuid", uuid)
                .getResultList();
    }

    @Override
    public List<HostMonitorEntity> getCvkMonitorRecordByCasUuid(String uuid) {
        String sql = "From HostMonitorEntity WHERE casUuid=:casUuid";
        return em.createQuery(sql, HostMonitorEntity.class)
                .setParameter("casUuid", uuid)
                .getResultList();
    }

    @Override
    public List<VmMonitorEntity> getVmMonitorRecordByCvkUuid(String uuid) {
        String sql = "From VmMonitorEntity WHERE cvkUuid=:cvkUuid";
        return em.createQuery(sql, VmMonitorEntity.class)
                .setParameter("cvkUuid", uuid)
                .getResultList();
    }

    @Override
    public boolean isNetworkIpDup(String ip) {
        String sql = "From NetworkMonitorEntity WHERE ip=:ip";
        List<NetworkMonitorEntity> entityList = em.createQuery(sql, NetworkMonitorEntity.class)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isK8sIpDup(String ip) {
        String sql = "From K8sMonitorEntity WHERE ip=:ip";
        List<K8sMonitorEntity> entityList = em.createQuery(sql, K8sMonitorEntity.class)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isCasIpDup(String ip) {
        String sql = "From CasMonitorEntity WHERE ip=:ip";
        List<CasMonitorEntity> entityList = em.createQuery(sql, CasMonitorEntity.class)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isNetworkIpDupNotP(String ip, String uuid) {
        String sql = "From NetworkMonitorEntity WHERE not(uuid=:uuid) and ip=:ip";
        List<NetworkMonitorEntity> entityList = em.createQuery(sql, NetworkMonitorEntity.class)
                .setParameter("uuid", uuid)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isCasIpDupNotP(String ip, String uuid) {
        String sql = "From CasMonitorEntity WHERE not(uuid=:uuid) and ip=:ip";
        List<CasMonitorEntity> entityList = em.createQuery(sql, CasMonitorEntity.class)
                .setParameter("uuid", uuid)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isK8sIpDupNotP(String ip, String uuid) {
        String sql = "From K8sMonitorEntity WHERE not(uuid=:uuid) and ip=:ip";
        List<K8sMonitorEntity> entityList = em.createQuery(sql, K8sMonitorEntity.class)
                .setParameter("uuid", uuid)
                .setParameter("ip", ip)
                .getResultList();
        if (entityList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

//    @Override
//    public List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid) {
//        String sql = "From OperationMonitorEntity WHERE templateId=:uuid";
//        return em.createQuery(sql, OperationMonitorEntity.class)
//                .setParameter("uuid", uuid)
//                .getResultList();
//    }


}
