package com.gy.monitorCore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.common.MonitorEnum;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    public boolean insertMonitorRecord(OperationMonitorEntity entity) {
        return dao.insertMonitorRecord(entity);
    }

    @Override
    public List<Cluster> getClusterListByExporter(CasTransExporterModel model) {
        ResourceData data = casDao.getCasResourceListByExporter(model);
        List<Cluster> clusterList = new ArrayList<>();
        data.getHostPoolList().forEach(hostpool -> {
            String hostpoolId = hostpool.getId();
            List<Cluster> clusters = hostpool.getClusterList();
            clusters.forEach(cluster -> {
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

        // TODO: 2018/10/21  在etcd中删除该监控记录 /resource_monitor/uuid wsrequest.delete()
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
    public boolean updateMonitorRecord(OperationMonitorEntity view) {
        boolean res = dao.insertMonitorRecord(view);
        if (res) {
            // TODO: 2018/10/22 更新etcd，设置新的etcd实体（scrapeInterval，scrapeTimeoutresourceId，resourceType三级规格类型）,
            // 通过监控对象uuid从etcd获取该对象信息是否存在（get /resource_monitor/:uuid），
            // etcd实体（ip，monitortype，uuid）拼装etcd脚本,然后写到该etcd中，该uuid对应的value中(会覆盖掉)
        }
        return false;
    }
}
