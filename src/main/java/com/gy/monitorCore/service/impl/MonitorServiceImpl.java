package com.gy.monitorCore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean insertMonitorRecord(OperationMonitorEntity entity) throws IOException {
        boolean res = dao.insertMonitorRecord(entity);
        if (res) {
            // : 2018/10/22 更新etcd，设置新的etcd实体（scrapeInterval，scrapeTimeoutresourceId，resourceType三级规格类型）,
            // 通过监控对象uuid从etcd获取该对象信息是否存在（get /resource_monitor/:uuid），
            // etcd实体（ip，monitortype，uuid）拼装etcd脚本,然后写到该etcd中，该uuid对应的value中(会覆盖掉)

            // : 2018/10/23
            // 通过monitortype获取exporter信息
            String exporterUrl = etcdDao.getExporterInfoByMonitorType(entity.getMonitorType());
            if (null != exporterUrl) {
                insertMonitorIntoEtcd(entity,exporterUrl);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Cluster> getClusterListByExporter(CasTransExporterModel model) {
        ResourceData data = casDao.getCasResourceListByExporter(model);
        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
        Optional<LightTypeEntity> clusterLight = lightTypeList.stream().filter(x -> {
            return x.getName().equals(MonitorEnum.LightTypeEnum.CASCLUSTER.value());
        }).findFirst();
        List<OperationMonitorEntity> clustermonitorList = clusterLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<Cluster> clusterList = new ArrayList<>();
        data.getHostPoolList().forEach(hostpool -> {
            String hostpoolId = hostpool.getId();
            List<Cluster> clusters = hostpool.getClusterList();
            clusters.forEach(cluster -> {
                Optional<OperationMonitorEntity> beenAddCluster = clustermonitorList.stream().filter(c -> {
                    try {
                        CasMonitorInfo clusterMonitorInfo = mapper.readValue(c.getMonitorInfo(), CasMonitorInfo.class);
                        return clusterMonitorInfo.getClusterId().equals(cluster.getClusterId());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).findFirst();
                if (beenAddCluster.isPresent()) {
                    cluster.setUuid(beenAddCluster.get().getUuid());
                    cluster.setBeenAdd(true);
                } else {
                    cluster.setBeenAdd(false);
                }
                cluster.setHostpoolId(hostpoolId);
            });
            clusterList.addAll(clusters);
        });
        return clusterList;
    }

    @Override
    public boolean insertMonitorRecordList(List<OperationMonitorEntity> list) {
        try {
            list.forEach(x -> {
                dao.insertMonitorRecord(x);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Host> getCvkAndVmListByExporter(CasTransExporterModel model) {
        ResourceData data = casDao.getCasResourceListByExporter(model);
        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
        Optional<LightTypeEntity> cvkLight = lightTypeList.stream().filter(x -> {
            return x.getName().equals(MonitorEnum.LightTypeEnum.CVK.value());
        }).findFirst();
        Optional<LightTypeEntity> vmLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())).findFirst();
        List<OperationMonitorEntity> cvkList = cvkLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<OperationMonitorEntity> vmList = vmLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<Host> hosts = new ArrayList<>();
        data.getHostPoolList().forEach(hostPool -> {
            //hostpool下面是集群
            hostPool.getClusterList().forEach(cluster -> {
                List<Host> hostList = cluster.getHostList();
                hostList.forEach(host -> {
                    Optional<OperationMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk -> {
                        try {
                            CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(), CasMonitorInfo.class);
                            return casMonitorInfo.getHostId().equals(host.getHostId());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
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
                        Optional<OperationMonitorEntity> beenAddVm = vmList.stream().filter(vm -> {
                            try {
                                CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(), CasMonitorInfo.class);
                                return vmMonitorInfo.getVmId().equals(virtualMachine.getVmId());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
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
                Optional<OperationMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk -> {
                    try {
                        CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(), CasMonitorInfo.class);
                        return casMonitorInfo.getHostId().equals(host.getHostId());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).findFirst();
                if (beenAddCvk.isPresent()) {
                    host.setUuid(beenAddCvk.get().getUuid());
                    host.setBeenAdd(true);
                } else {
                    host.setBeenAdd(false);
                }
                host.setHostpoolId(hostPool.getId());
                host.getVirtualMachineList().forEach(virtualMachine -> {
                    Optional<OperationMonitorEntity> beenAddVm = vmList.stream().filter(vm -> {
                        try {
                            CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(), CasMonitorInfo.class);
                            return vmMonitorInfo.getVmId().equals(virtualMachine.getVmId());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
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
            node.getPods().forEach(pod -> {
                pod.getContainers().forEach(container -> {
                    container.setNodeIp(node.getNodeIp());
                    container.setNodeName(node.getNodeName());
                    container.setPodName(pod.getPodName());
                    container.setPodNamespace(pod.getPodNamespace());
                });
                containerList.addAll(pod.getContainers());
            });
        });
        return containerList;
    }

    @Override
    public List<Node> getNodeListByExporter(String ip, String port) {
        Resource resource = k8sMonitorDao.getK8sResourceListByExporter(ip, port);
        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
        Optional<LightTypeEntity> nodeLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.K8SNODE.value())).findFirst();
        Optional<LightTypeEntity> containerLight = lightTypeList.stream().filter(x -> x.getName().equals(MonitorEnum.LightTypeEnum.K8SCONTAINER.value())).findFirst();
        List<OperationMonitorEntity> nodeList = nodeLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<OperationMonitorEntity> containerList = containerLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        resource.getNodes().forEach(node -> {
            Optional<OperationMonitorEntity> optNode = nodeList.stream().filter(x -> {
                try {
                    K8snMonitorInfo k8snMonitorInfo = mapper.readValue(x.getMonitorInfo(), K8snMonitorInfo.class);
                    return ip.equals(k8snMonitorInfo.getMasterIp()) && node.getNodeIp().equals(k8snMonitorInfo.getNodeIp())
                            && node.getNodeName().equals(k8snMonitorInfo.getNodeName());
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
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
                    Optional<OperationMonitorEntity> optC = containerList.stream().filter(x -> {
                        try {
                            K8scMonitorInfo k8scMonitorInfo = mapper.readValue(x.getMonitorInfo(), K8scMonitorInfo.class);
                            return ip.equals(k8scMonitorInfo.getMasterIp()) && node.getNodeIp().equals(k8scMonitorInfo.getNodeIp()) && container.getContainerId()
                                    .equals(k8scMonitorInfo.getContainerId());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
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

    @Override
    public boolean delMonitorRecord(String uuid) {

        // : 2018/10/21  在etcd中删除该监控记录 /resource_monitor/uuid wsrequest.delete()
            etcdDao.delEtcdMonitor(uuid);
        //在数据库中将delete字段置1
        return dao.delMonitorRecord(uuid);
    }

    @Override
    public List<OperationMonitorEntity> getAllOperationMonitorEntity() {
        return dao.getAllOperationMonitorEntity();
    }

    @Override
    public List<OperationMonitorEntity> getMonitorRecordByRootId(String uuid) {
        return dao.getMonitorRecordByRootId(uuid);
    }

    @Override
    public List<OperationMonitorEntity> getMonitorRecordByTemplateId(String uuid) {
        return dao.getMonitorRecordByTemplateId(uuid);
    }


    /**
     * 将监控信息插入到etcd
     *
     * @param monitor
     */
    private void insertMonitorIntoEtcd(OperationMonitorEntity monitor, String exporterUrl) throws JsonProcessingException {
        Optional<LightTypeEntity> lightTypeEntity = dao.getLightTypeEntity().stream().filter(x -> x.getUuid().equals(monitor.getLightTypeId())).findFirst();
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
        relabelMap2.put("target_label", "__address__");
        relabelConfigs.add(relabelMap3);
        etcdView.setRelabelConfigs(relabelConfigs);
        List<Map<String, Object>> staticConfigs = new ArrayList<>();
        Map<String, Object> config1 = new HashMap<>();
        List<String> targets = new ArrayList<>();
        targets.add(monitor.getUuid());
        config1.put("targets", targets);
        Map<String, String> labels = new HashMap<>();
        labels.put("instance", monitor.getIp());
        if (lightTypeEntity.isPresent()) {
            labels.put("resource_uuid", lightTypeEntity.get().getParentId());//二级规格id
            labels.put("resource_type", lightTypeEntity.get().getName());//三级规格类型
        }
        config1.put("labels", labels);
        staticConfigs.add(config1);
        etcdView.setStaticConfigs(staticConfigs);
        etcdDao.updateEtcdMonitor(etcdView, monitor.getUuid());

    }
}
