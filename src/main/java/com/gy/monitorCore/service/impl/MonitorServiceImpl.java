package com.gy.monitorCore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.common.MonitorEnum;
import com.gy.monitorCore.dao.EtcdDao;
import com.gy.monitorCore.dao.K8sMonitorDao;
import com.gy.monitorCore.dao.MonitorDao;
import com.gy.monitorCore.dao.CasMonitorDao;
import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.entity.view.k8sView.Container;
import com.gy.monitorCore.entity.view.k8sView.Node;
import com.gy.monitorCore.entity.view.k8sView.Resource;
import com.gy.monitorCore.service.MonitorService;
import com.gy.monitorCore.service.PrometheusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.VM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    MonitorDao dao;

    @Autowired
    CasMonitorDao casDao;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    K8sMonitorDao k8sMonitorDao;

    @Autowired
    EtcdDao etcdDao;

    @Autowired
    PrometheusService proService;

    @Override
    public TestEntity getJPAInfo() {
        return dao.getJPAInfo();
    }

    @Override
    public String getOperationMonitorEntity(String uuid, String lightType) throws JsonProcessingException {
        if (lightType.equals(MonitorEnum.LightTypeEnum.SWITCH.value()) || lightType.equals(MonitorEnum.LightTypeEnum.ROUTER.value())
                || lightType.equals(MonitorEnum.LightTypeEnum.LB.value()) || lightType.equals(MonitorEnum.LightTypeEnum.FIREWALL.value())) {
            return mapper.writeValueAsString(dao.getNetworkMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.MYSQL.value())) {
            return mapper.writeValueAsString(dao.getDbMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.TOMCAT.value())) {
            return mapper.writeValueAsString(dao.getTomcatMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CAS.value())) {
            return mapper.writeValueAsString(dao.getCasMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CVK.value())) {
            return mapper.writeValueAsString(dao.getHostMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())) {
            return mapper.writeValueAsString(dao.getVmMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8S.value())) {
            return mapper.writeValueAsString(dao.getK8sMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SNODE.value())) {
            return mapper.writeValueAsString(dao.getK8snodeMonitorEntity(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())) {
            return mapper.writeValueAsString(dao.getK8sContainerMonitorEntity(uuid));
        }
        return null;
    }

    boolean insertNetworkMonitorRecord(NetworkMonitorEntity net, String lightType) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(lightType);
        operationMonitorEntity.setUuid(net.getUuid());
        operationMonitorEntity.setIp(net.getIp());
        operationMonitorEntity.setMonitorType(net.getMonitorType());
        operationMonitorEntity.setScrapeInterval(net.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(net.getScrapeTimeout());
        boolean res = dao.insertNetworkMonitorEntity(net);
        return commonInsertEtcd(res, operationMonitorEntity);

    }

    boolean commonInsertEtcd(boolean res, OperationMonitorEntity operationMonitorEntity) throws IOException {
        if (res) {
            String exporterUrl = etcdDao.getExporterInfoByMonitorType(operationMonitorEntity.getMonitorType());
            if (null != exporterUrl) {
                insertMonitorIntoEtcd(operationMonitorEntity, exporterUrl);
                return true;
            }
        }
        return false;

    }

    boolean insertDbMonitorRecord(DBMonitorEntity db) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.MYSQL.value());
        operationMonitorEntity.setUuid(db.getUuid());
        operationMonitorEntity.setIp(db.getIp());
        operationMonitorEntity.setMonitorType(db.getMonitorType());
        operationMonitorEntity.setScrapeInterval(db.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(db.getScrapeTimeout());
        boolean res = dao.insertDbMonitorEntity(db);
        return commonInsertEtcd(res, operationMonitorEntity);
    }

    boolean insertTomcatMonitorRecord(TomcatMonitorEntity tomcat) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.TOMCAT.value());
        operationMonitorEntity.setUuid(tomcat.getUuid());
        operationMonitorEntity.setIp(tomcat.getIp());
        operationMonitorEntity.setMonitorType(tomcat.getMonitorType());
        operationMonitorEntity.setScrapeInterval(tomcat.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(tomcat.getScrapeTimeout());
        boolean res = dao.insertTomcatMonitorEntity(tomcat);
        return commonInsertEtcd(res, operationMonitorEntity);
    }

    boolean insertCasMonitorRecord(CasMonitorEntity cas) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.CAS.value());
        operationMonitorEntity.setUuid(cas.getUuid());
        operationMonitorEntity.setIp(cas.getIp());
        operationMonitorEntity.setMonitorType(cas.getMonitorType());
        operationMonitorEntity.setScrapeInterval(cas.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(cas.getScrapeTimeout());
        boolean res = dao.insertCasMonitorEntity(cas);
        return commonInsertEtcd(res, operationMonitorEntity);
    }

    boolean insertHostMonitorRecord(HostMonitorEntity host) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.CVK.value());
        operationMonitorEntity.setUuid(host.getUuid());
        operationMonitorEntity.setIp(host.getIp());
        operationMonitorEntity.setMonitorType(host.getMonitorType());
        operationMonitorEntity.setScrapeInterval(host.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(host.getScrapeTimeout());
        boolean res = dao.insertHostMonitorEntity(host);
        return commonInsertEtcd(res, operationMonitorEntity);
    }

    boolean insertVmMonitorRecord(VmMonitorEntity vm) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value());
        operationMonitorEntity.setUuid(vm.getUuid());
        operationMonitorEntity.setIp(vm.getIp());
        operationMonitorEntity.setMonitorType(vm.getMonitorType());
        operationMonitorEntity.setScrapeInterval(vm.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(vm.getScrapeTimeout());
        boolean res = dao.insertVmMonitorEntity(vm);
        return commonInsertEtcd(res, operationMonitorEntity);
    }
    boolean insertK8sMonitorRecord(K8sMonitorEntity k8s) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.K8S.value());
        operationMonitorEntity.setUuid(k8s.getUuid());
        operationMonitorEntity.setIp(k8s.getIp());
        operationMonitorEntity.setMonitorType(k8s.getMonitorType());
        operationMonitorEntity.setScrapeInterval(k8s.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(k8s.getScrapeTimeout());
        boolean res = dao.insertK8sMonitorEntity(k8s);
        return commonInsertEtcd(res, operationMonitorEntity);
    }
    boolean insertk8snodeMonitorRecord(K8snodeMonitorEntity k8sn) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.K8SNODE.value());
        operationMonitorEntity.setUuid(k8sn.getUuid());
        operationMonitorEntity.setIp(k8sn.getIp());
        operationMonitorEntity.setMonitorType(k8sn.getMonitorType());
        operationMonitorEntity.setScrapeInterval(k8sn.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(k8sn.getScrapeTimeout());
        boolean res = dao.insertK8snodeMonitorEntity(k8sn);
        return commonInsertEtcd(res, operationMonitorEntity);
    }
    boolean insertk8scontainerMonitorRecord(K8scontainerMonitorEntity k8sc) throws IOException {
        OperationMonitorEntity operationMonitorEntity = new OperationMonitorEntity();
        operationMonitorEntity.setLightTypeId(MonitorEnum.LightTypeEnum.K8SCONTAINER.value());
        operationMonitorEntity.setUuid(k8sc.getUuid());
        operationMonitorEntity.setIp(null);
        operationMonitorEntity.setMonitorType(k8sc.getMonitorType());
        operationMonitorEntity.setScrapeInterval(k8sc.getScrapeInterval());
        operationMonitorEntity.setScrapeTimeout(k8sc.getScrapeTimeout());
        boolean res = dao.insertK8sContainerMonitorEntity(k8sc);
        return commonInsertEtcd(res, operationMonitorEntity);
    }

    @Override
    public boolean insertMonitorRecord(String data, String lightType) throws IOException {
        if (lightType.equals(MonitorEnum.LightTypeEnum.SWITCH.value()) || lightType.equals(MonitorEnum.LightTypeEnum.ROUTER.value())
                || lightType.equals(MonitorEnum.LightTypeEnum.LB.value()) || lightType.equals(MonitorEnum.LightTypeEnum.FIREWALL.value())) {
            NetworkMonitorEntity net = mapper.readValue(data, NetworkMonitorEntity.class);
            return insertNetworkMonitorRecord(net, lightType);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.MYSQL.value())) {
            DBMonitorEntity db = mapper.readValue(data, DBMonitorEntity.class);
            return insertDbMonitorRecord(db);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.TOMCAT.value())) {
            TomcatMonitorEntity tomcat = mapper.readValue(data, TomcatMonitorEntity.class);
            return insertTomcatMonitorRecord(tomcat);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CAS.value())) {
            CasMonitorEntity cas = mapper.readValue(data, CasMonitorEntity.class);
            return insertCasMonitorRecord(cas);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CVK.value())) {
            HostMonitorEntity host = mapper.readValue(data, HostMonitorEntity.class);
            return insertHostMonitorRecord(host);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())) {
            VmMonitorEntity vm = mapper.readValue(data, VmMonitorEntity.class);
            return insertVmMonitorRecord(vm);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8S.value())) {
            K8sMonitorEntity k8s = mapper.readValue(data, K8sMonitorEntity.class);
            return insertK8sMonitorRecord(k8s);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SNODE.value())) {
            K8snodeMonitorEntity k8sn = mapper.readValue(data, K8snodeMonitorEntity.class);
            return insertk8snodeMonitorRecord(k8sn);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())) {
            K8scontainerMonitorEntity k8sc = mapper.readValue(data, K8scontainerMonitorEntity.class);
            return insertk8scontainerMonitorRecord(k8sc);
        }
        return false;
    }

    @Override
    public boolean insertMonitorRecordList(String data, String lightType) throws IOException {
        if (lightType.equals(MonitorEnum.LightTypeEnum.SWITCH.value()) || lightType.equals(MonitorEnum.LightTypeEnum.ROUTER.value())
                || lightType.equals(MonitorEnum.LightTypeEnum.LB.value()) || lightType.equals(MonitorEnum.LightTypeEnum.FIREWALL.value())) {
            List<NetworkMonitorEntity> list = mapper.readValue(data, new TypeReference<List<NetworkMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertNetworkMonitorRecord(x, lightType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.MYSQL.value())) {
            List<DBMonitorEntity> list = mapper.readValue(data, new TypeReference<List<DBMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertDbMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.TOMCAT.value())) {
            List<TomcatMonitorEntity> list = mapper.readValue(data, new TypeReference<List<TomcatMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertTomcatMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CAS.value())) {
            List<CasMonitorEntity> list = mapper.readValue(data, new TypeReference<List<CasMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertCasMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CVK.value())) {
            List<HostMonitorEntity> list = mapper.readValue(data, new TypeReference<List<HostMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertHostMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())) {
            List<VmMonitorEntity> list = mapper.readValue(data, new TypeReference<List<VmMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertVmMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8S.value())) {
            List<K8sMonitorEntity> list = mapper.readValue(data, new TypeReference<List<K8sMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertK8sMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SNODE.value())) {
            List<K8snodeMonitorEntity> list = mapper.readValue(data, new TypeReference<List<K8snodeMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertk8snodeMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())) {
            List<K8scontainerMonitorEntity> list = mapper.readValue(data, new TypeReference<List<K8scontainerMonitorEntity>>() {});
            list.forEach(x->{
                try {
                    insertk8scontainerMonitorRecord(x);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean delMonitorRecord(String uuid, String lightType) {
        // : 2018/10/21  在etcd中删除该监控记录 /resource_monitor/uuid wsrequest.delete()
        etcdDao.delEtcdMonitor(uuid);
        if (lightType.equals(MonitorEnum.LightTypeEnum.SWITCH.value()) || lightType.equals(MonitorEnum.LightTypeEnum.ROUTER.value())
                || lightType.equals(MonitorEnum.LightTypeEnum.LB.value()) || lightType.equals(MonitorEnum.LightTypeEnum.FIREWALL.value())) {
            return dao.delNetworkMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.MYSQL.value())) {
            return dao.delDbMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.TOMCAT.value())) {
            return dao.delTomcatMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CAS.value())) {
            return dao.delCasMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CVK.value())) {
            return dao.delCvkMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())) {
            return dao.delVmMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8S.value())) {
            return dao.delK8sMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SNODE.value())) {
           return dao.delK8snodeMonitorRecord(uuid);
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())) {
            return dao.delK8sContainerMonitorRecord(uuid);
        }
        return false;
    }

    @Override
    public String getMonitorRecordByTemplateId(String uuid, String lightType) throws JsonProcessingException {
        if (lightType.equals(MonitorEnum.LightTypeEnum.SWITCH.value()) || lightType.equals(MonitorEnum.LightTypeEnum.ROUTER.value())
                || lightType.equals(MonitorEnum.LightTypeEnum.LB.value()) || lightType.equals(MonitorEnum.LightTypeEnum.FIREWALL.value())) {
            return mapper.writeValueAsString(dao.getNetworkMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.MYSQL.value())) {
            return mapper.writeValueAsString(dao.getDbMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.TOMCAT.value())) {
            return mapper.writeValueAsString(dao.getTomcatMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CAS.value())) {
            return mapper.writeValueAsString(dao.getCasMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.CVK.value())) {
            return mapper.writeValueAsString(dao.getHostMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())) {
            return mapper.writeValueAsString(dao.getVmMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8S.value())) {
            return mapper.writeValueAsString(dao.getK8sMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SNODE.value())) {
            return mapper.writeValueAsString(dao.getK8sNodeMonitorRecordByTemplateId(uuid));
        } else if (lightType.equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())) {
            return mapper.writeValueAsString(dao.getK8sContainerMonitorRecordByTemplateId(uuid));
        }
        return null;
    }


//    @Override
//    public OperationMonitorEntity getOperationMonitorEntity(String uuid) {
//        return dao.getOperationMonitorEntity(uuid);
//    }

//    @Override
//    public List<MiddleTypeEntity> getMiddleTypeEntity() {
//        return dao.getMiddleTypeEntity();
//    }
//
//    @Override
//    public List<LightTypeEntity> getLightTypeEntity() {
//        return dao.getLightTypeEntity();
//    }
//
//    @Override
//    public boolean insertMonitorRecord(OperationMonitorEntity entity) throws IOException {
//        boolean res = dao.insertMonitorRecord(entity);
//        if (res) {
//            // : 2018/10/22 更新etcd，设置新的etcd实体（scrapeInterval，scrapeTimeoutresourceId，resourceType三级规格类型）,
//            // 通过监控对象uuid从etcd获取该对象信息是否存在（get /resource_monitor/:uuid），
//            // etcd实体（ip，monitortype，uuid）拼装etcd脚本,然后写到该etcd中，该uuid对应的value中(会覆盖掉)
//
//            // : 2018/10/23
//            // 通过monitortype获取exporter信息
//            String exporterUrl = etcdDao.getExporterInfoByMonitorType(entity.getMonitorType());
//            if (null != exporterUrl) {
//                insertMonitorIntoEtcd(entity, exporterUrl);
//                return true;
//            }
//        }
//        return false;
//    }

//    @Override
//    public List<Cluster> getClusterListByExporter(CasTransExporterModel model) {
//        ResourceData data = casDao.getCasResourceListByExporter(model);
//        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
//        Optional<LightTypeEntity> clusterLight = lightTypeList.stream().filter(x -> {
//            return x.getName().equals(MonitorEnum.LightTypeEnum.CASCLUSTER.value());
//        }).findFirst();
//        List<OperationMonitorEntity> clustermonitorList = clusterLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
//                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
//        List<Cluster> clusterList = new ArrayList<>();
//        data.getHostPoolList().forEach(hostpool -> {
//            String hostpoolId = hostpool.getId();
//            List<Cluster> clusters = hostpool.getClusterList();
//            clusters.forEach(cluster -> {
//                Optional<OperationMonitorEntity> beenAddCluster = clustermonitorList.stream().filter(c -> {
//                    try {
//                        CasMonitorInfo clusterMonitorInfo = mapper.readValue(c.getMonitorInfo(), CasMonitorInfo.class);
//                        return clusterMonitorInfo.getClusterId().equals(cluster.getClusterId());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }).findFirst();
//                if (beenAddCluster.isPresent()) {
//                    cluster.setUuid(beenAddCluster.get().getUuid());
//                    cluster.setBeenAdd(true);
//                } else {
//                    cluster.setBeenAdd(false);
//                }
//                cluster.setHostpoolId(hostpoolId);
//            });
//            clusterList.addAll(clusters);
//        });
//        return clusterList;
//    }

//    @Override
//    public boolean insertMonitorRecordList(List<OperationMonitorEntity> list) {
//        try {
//            list.forEach(x -> {
//                dao.insertMonitorRecord(x);
//            });
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    @Override
    public List<Host> getCvkAndVmListByExporter(CasTransExporterModel model) {
        ResourceData data = casDao.getCasResourceListByExporter(model);
//        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
//        Optional<LightTypeEntity> cvkLight = lightTypeList.stream().filter(x -> {
//            return x.getName().equals(MonitorEnum.LightTypeEnum.CVK.value());
//        }).findFirst();
//        Optional<LightTypeEntity> vmLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())).findFirst();
        List<HostMonitorEntity> cvkList = dao.getAllHostMonitorEntity();
        List<VmMonitorEntity> vmList = dao.getAllVmMonitorEntity();
        List<Host> hosts = new ArrayList<>();
        data.getHostPoolList().forEach(hostPool -> {
            //hostpool下面是集群
            hostPool.getClusterList().forEach(cluster -> {
                List<Host> hostList = cluster.getHostList();
                hostList.forEach(host -> {
                    Optional<HostMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk -> {
//                            CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(), CasMonitorInfo.class);
                        return cvk.getHostId().equals(host.getHostId()) && cvk.getHostpoolId().equals(hostPool.getHostpoolId())
                                && cvk.getIp().equals(host.getHostIp()) & cvk.getName().equals(host.getHostName());

                    }).findFirst();
                    if (beenAddCvk.isPresent()) {
                        host.setUuid(beenAddCvk.get().getUuid());
                        host.setBeenAdd(true);
                    } else {
                        host.setBeenAdd(false);
                    }
                    host.setClusterId(cluster.getClusterId());
                    host.setHostpoolId(hostPool.getId());
                    host.getVirtualMachineList().forEach(virtualMachine -> {
                        Optional<VmMonitorEntity> beenAddVm = vmList.stream().filter(vm -> {

                            HostMonitorEntity hostmonitor = dao.getHostMonitorEntity(vm.getCvkUuid());
//                                CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(), CasMonitorInfo.class);
                            return vm.getVmId().equals(virtualMachine.getVmId()) && vm.getIp().equals(virtualMachine.getVmIp())
                                    && vm.getName().equals(virtualMachine.getVmName()) && hostmonitor.getHostId().equals(host.getHostId())
                                    && hostmonitor.getHostpoolId().equals(host.getHostpoolId());

                        }).findFirst();
                        if (beenAddVm.isPresent()) {
                            virtualMachine.setUuid(beenAddVm.get().getUuid());
                            virtualMachine.setBeenAdd(true);
                        } else {
                            virtualMachine.setBeenAdd(false);
                        }
                        virtualMachine.setCvkId(host.getHostId());
                        virtualMachine.setCvkName(host.getHostName());
                        virtualMachine.setHostpoolId(hostPool.getId());
                    });
                });
                hosts.addAll(hostList);
            });
            //hostpool下面是主机
            List<Host> dirHosts = hostPool.getHostList();
            dirHosts.forEach(host -> {
                Optional<HostMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk -> {
//                        CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(), CasMonitorInfo.class);
                    return cvk.getHostId().equals(host.getHostId()) && cvk.getHostpoolId().equals(hostPool.getHostpoolId())
                            && cvk.getIp().equals(host.getHostIp()) & cvk.getName().equals(host.getHostName());
//                        return casMonitorInfo.getHostId().equals(host.getHostId());
                }).findFirst();
                if (beenAddCvk.isPresent()) {
                    host.setUuid(beenAddCvk.get().getUuid());
                    host.setBeenAdd(true);
                } else {
                    host.setBeenAdd(false);
                }
                host.setHostpoolId(hostPool.getId());
                host.getVirtualMachineList().forEach(virtualMachine -> {
                    Optional<VmMonitorEntity> beenAddVm = vmList.stream().filter(vm -> {

                        HostMonitorEntity hostmonitor = dao.getHostMonitorEntity(vm.getCvkUuid());
//                                CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(), CasMonitorInfo.class);
                        return vm.getVmId().equals(virtualMachine.getVmId()) && vm.getIp().equals(virtualMachine.getVmIp())
                                && vm.getName().equals(virtualMachine.getVmName()) && hostmonitor.getHostId().equals(host.getHostId())
                                && hostmonitor.getHostpoolId().equals(host.getHostpoolId());
                    }).findFirst();
                    if (beenAddVm.isPresent()) {
                        virtualMachine.setUuid(beenAddVm.get().getUuid());
                        virtualMachine.setBeenAdd(true);
                    } else {
                        virtualMachine.setBeenAdd(false);
                    }
                    virtualMachine.setCvkId(host.getHostId());
                    virtualMachine.setCvkName(host.getHostName());
                    virtualMachine.setHostpoolId(hostPool.getId());
                });
            });
            hosts.addAll(dirHosts);
        });
        return hosts;
    }

    @Override
    public List<Container> getContainerListByExporter(String ip, String port) {
        Resource resource = k8sMonitorDao.getK8sResourceListByExporter(ip, port);
        List<Container> containerList = new ArrayList<>();
        resource.getNodes().forEach(node -> {
            if (null != node.getPods() && node.getPods().size() > 0) {
                node.getPods().forEach(pod -> {
                    if (null != pod.getContainers() && pod.getContainers().size() > 0) {
                        pod.getContainers().forEach(container -> {
                            container.setNodeIp(node.getNodeIp());
                            container.setNodeName(node.getNodeName());
                            container.setPodName(pod.getPodName());
                            container.setPodNamespace(pod.getPodNamespace());
                        });
                    }
                    containerList.addAll(pod.getContainers());
                });
            }
        });
        return containerList;
    }

    @Override
    public List<Node> getNodeListByExporter(String ip, String port) {
        Resource resource = k8sMonitorDao.getK8sResourceListByExporter(ip, port);
//        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
//        Optional<LightTypeEntity> nodeLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.K8SNODE.value())).findFirst();
//        Optional<LightTypeEntity> containerLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())).findFirst();
        List<K8snodeMonitorEntity> nodeList = dao.getAllK8snodeMonitorEntity();
        List<K8scontainerMonitorEntity> containerList = dao.getAllK8sContainerMonitorEntity();
        resource.getNodes().forEach(node -> {
            Optional<K8snodeMonitorEntity> optNode = nodeList.stream().filter(x -> {
                K8sMonitorEntity k8smonitor = dao.getK8sMonitorEntity(x.getK8sUuid());
//                    K8snMonitorInfo k8snMonitorInfo = mapper.readValue(x.getMonitorInfo(), K8snMonitorInfo.class);
                return ip.equals(k8smonitor.getIp()) && node.getNodeIp().equals(x.getIp())
                        && node.getNodeName().equals(x.getName());

            }).findFirst();
            if (optNode.isPresent()) {
                node.setUuid(optNode.get().getUuid());
                node.setBeenAdd(true);
            } else {
                node.setBeenAdd(false);
            }
            node.getPods().forEach(pod -> {
                pod.setNodeIp(node.getNodeIp());
                pod.setNodeName(node.getNodeName());
                pod.getContainers().forEach(container -> {
                    Optional<K8scontainerMonitorEntity> optC = containerList.stream().filter(x -> {
                        K8snodeMonitorEntity k8snodemonitor = dao.getK8snodeMonitorEntity(x.getK8snodeUuid());
                        K8sMonitorEntity k8smonitor = dao.getK8sMonitorEntity(k8snodemonitor.getK8sUuid());
//                            K8scMonitorInfo k8scMonitorInfo = mapper.readValue(x.getMonitorInfo(), K8scMonitorInfo.class);
                        return ip.equals(k8smonitor.getIp()) && node.getNodeIp().equals(k8snodemonitor.getIp()) && container.getContainerId()
                                .equals(x.getContainer_id());
                    }).findFirst();
                    if (optC.isPresent()) {
                        container.setUuid(optC.get().getUuid());
                        container.setBeenAdd(true);
                    } else {
                        container.setBeenAdd(false);
                    }
                    container.setNodeIp(node.getNodeIp());
                    container.setNodeName(node.getNodeName());
                    container.setPodName(pod.getPodName());
                    container.setPodNamespace(pod.getPodNamespace());
                });
            });
        });
        return resource.getNodes();
    }


//    @Override
//    public boolean delMonitorRecord(String uuid) {
//
//        // : 2018/10/21  在etcd中删除该监控记录 /resource_monitor/uuid wsrequest.delete()
//        etcdDao.delEtcdMonitor(uuid);
//        //在数据库中将delete字段置1
//        return dao.delMonitorRecord(uuid);
//    }

//    @Override
//    public List<OperationMonitorEntity> getAllOperationMonitorEntity() {
//        return dao.getAllOperationMonitorEntity();
//    }

    @Override
    public List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid) {
        return dao.getMonitorRecordByRootId(uuid);
    }


//    @Override
//    public List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid) {
//        return dao.getMonitorRecordByTemplateId(uuid);
//    }

    @Override
    public String testgetClusterIp() throws IOException {
        String exporterUrl = etcdDao.getExporterInfoByMonitorType("mysql");
        return exporterUrl;
    }

    @Override
    public String getQuotaValueByName(String monitorUUid, String quotaName) throws IOException {
//        String value = proService.getQuotaValue(genQuotaExpression(monitorUUid, quotaName));

        Properties properties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/gy/IdeaProjects/monitor-core/src/main/resources/config/testquotadata.properties"));
        properties.load(bufferedReader);
        String value = properties.getProperty(quotaName);

        return value;
    }

    @Override
    public String getQuotaNameByMonitorAndName(String monitorType, String name) throws IOException {

        Properties properties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/gy/IdeaProjects/monitor-core/src/main/resources/config/monitorMap.properties"));
//        BufferedReader bufferedReader = new BufferedReader(new FileReader("/monitorMap.properties"));
        properties.load(bufferedReader);
        String value = properties.getProperty("monitor.api." + monitorType + "." + name);
        return value;
    }

    @Override
    public List<NetworkMonitorEntity> getAllNetworkMonitorEntity() {
        return dao.getAllNetworkMonitorEntity();
    }

    @Override
    public List<TomcatMonitorEntity> getAllTomcatMonitorEntity() {
        return dao.getAllTomcatMonitorEntity();
    }

    @Override
    public List<DBMonitorEntity> getAllDbMonitorEntity() {
        return dao.getAllDbMonitorEntity();
    }

    @Override
    public List<CasMonitorEntity> getAllCasMonitorEntity() {
        return dao.getAllCasMonitorEntity();
    }

    @Override
    public List<HostMonitorEntity> getAllHostMonitorEntity() {
        return dao.getAllHostMonitorEntity();
    }

    @Override
    public List<VmMonitorEntity> getAllVmMonitorEntity() {
        return dao.getAllVmMonitorEntity();
    }

    @Override
    public List<K8sMonitorEntity> getAllK8sMonitorEntity() {
        return dao.getAllK8sMonitorEntity();
    }

    @Override
    public List<K8snodeMonitorEntity> getAllK8snodeMonitorEntity() {
        return dao.getAllK8snodeMonitorEntity();
    }

    @Override
    public List<K8scontainerMonitorEntity> getAllK8sContainerMonitorEntity() {
        return dao.getAllK8sContainerMonitorEntity();
    }

    @Override
    public List<K8sNodeAndContainerView> getAllNodeAndContainerByK8suuid(String uuid) throws JsonProcessingException {
        List<K8sNodeAndContainerView> list = new ArrayList<>();
        List<K8snodeMonitorEntity> k8snodeList = dao.getK8sNodeMonitorRecordByK8sUuid(uuid);
        k8snodeList.forEach(x->{
            K8sNodeAndContainerView view = new K8sNodeAndContainerView();
            view.setK8snode(x);
            List<K8scontainerMonitorEntity> k8sc = null;
            k8sc = dao.getK8sContainerMonitorRecordByK8sNodeUuid(x.getUuid());
            view.setK8sContainerList(k8sc);
            list.add(view);
        });
        return list;
    }

    @Override
    public List<K8scontainerMonitorEntity> getAllContainerByK8sNodeuuid(String uuid) {
        return dao.getK8sContainerMonitorRecordByK8sNodeUuid(uuid);
    }

    @Override
    public List<CvkAndVmView> getAllCvkAndVmByCasuuid(String uuid) throws JsonProcessingException {
        List<CvkAndVmView> list =new ArrayList<>();
        List<HostMonitorEntity> cvkList = dao.getCvkMonitorRecordByCasUuid(uuid);
        cvkList.forEach(x->{
            CvkAndVmView view = new CvkAndVmView();
            view.setHostMonitor(x);
            List<VmMonitorEntity> vm = null;
            vm = dao.getVmMonitorRecordByCvkUuid(x.getUuid());
            view.setVmMonitorList(vm);
            list.add(view);
        });
        return list;
    }

    @Override
    public List<VmMonitorEntity> getAllVmByCvkuuid(String uuid) {
        return dao.getVmMonitorRecordByCvkUuid(uuid);
    }


    private String genQuotaExpression(String monitorUUid, String quotaName) {
        if (quotaName.contains(".")) {
            String[] quotaList = quotaName.split("\\.");
            return quotaList[quotaList.length - 1] + "{instance_id=" + "'" + monitorUUid + "'" + "}";
        } else {
            return quotaName + "{instance_id=" + "'" + monitorUUid + "'" + "}";
        }
    }

    /**
     * 将监控信息插入到etcd
     *
     * @param monitor
     */
    private void insertMonitorIntoEtcd(OperationMonitorEntity monitor, String exporterUrl) throws JsonProcessingException {
//        Optional<LightTypeEntity> lightTypeEntity = dao.getLightTypeEntity().stream().filter(x -> x.getUuid().equals(monitor.getLightTypeId())).findFirst();
        MonitorEtcdView etcdView = new MonitorEtcdView();
        etcdView.setScrapeInterval(monitor.getScrapeInterval() + "s");
        etcdView.setScrapeTimeout(monitor.getScrapeTimeout() + "s");
        etcdView.setMetricsPath("/" + monitor.getMonitorType());
        etcdView.setJobName(monitor.getUuid());
        List<Map<String, Object>> relabelConfigs = new ArrayList<>();
        Map<String, Object> relabelMap1 = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("__address__");
        relabelMap1.put("source_labels", list);
        relabelMap1.put("target_label", "__param_target");
        relabelConfigs.add(relabelMap1);
        Map<String, Object> relabelMap2 = new HashMap<>();
        List<String> list2 = new ArrayList<>();
        list2.add("__param_target");
        relabelMap2.put("source_labels", list2);
        relabelMap2.put("target_label", "instance_id");
        relabelConfigs.add(relabelMap2);
        Map<String, Object> relabelMap3 = new HashMap<>();
        relabelMap3.put("replacement", exporterUrl);
        relabelMap3.put("target_label", "__address__");
        relabelConfigs.add(relabelMap3);
        etcdView.setRelabelConfigs(relabelConfigs);
        List<Map<String, Object>> staticConfigs = new ArrayList<>();
        Map<String, Object> config1 = new HashMap<>();
        List<String> targets = new ArrayList<>();
        targets.add(monitor.getUuid());
        config1.put("targets", targets);
        Map<String, String> labels = new HashMap<>();
        labels.put("instance", monitor.getIp());
//            labels.put("resource_uuid", lightTypeEntity.get().getParentId());//二级规格id
        labels.put("resource_type", monitor.getLightTypeId());//三级规格类型
        config1.put("labels", labels);
        staticConfigs.add(config1);
        etcdView.setStaticConfigs(staticConfigs);
        etcdDao.updateEtcdMonitor(etcdView, monitor.getUuid());

    }
}
