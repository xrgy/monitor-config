package com.gy.monitorCore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.common.MonitorEnum;
import com.gy.monitorCore.dao.MonitorDao;
import com.gy.monitorCore.dao.MonitorExporterDao;
import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;


/**
 * Created by gy on 2018/3/31.
 */
@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    MonitorDao dao;

    @Autowired
    MonitorExporterDao exporterDao;

    @Autowired
    ObjectMapper mapper;

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
        ResourceData data = exporterDao.getCasResourceListByExporter(model);
        List<Cluster> clusterList = new ArrayList<>();
        data.getHostPoolList().forEach(hostpool->{
            String hostpoolId= hostpool.getId();
            List<Cluster> clusters = hostpool.getClusterList();
            clusters.forEach(cluster->{
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
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Host> getCvkAndVmListByExporter(CasTransExporterModel model) {
        ResourceData data = exporterDao.getCasResourceListByExporter(model);
        List<LightTypeEntity> lightTypeList = dao.getLightTypeEntity();
        Optional<LightTypeEntity> cvkLight = lightTypeList.stream().filter(x -> {
            return x.getName().equals(MonitorEnum.LightTypeEnum.CVK.value());
        }).findFirst();
        Optional<LightTypeEntity> vmLight = lightTypeList.stream().filter(x->x.getName().equals(MonitorEnum.LightTypeEnum.VIRTUALMACHINE.value())).findFirst();
        List<OperationMonitorEntity> cvkList = cvkLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<OperationMonitorEntity> vmList = vmLight.map(lightTypeEntity -> dao.getAllMonitorByLightType(
                lightTypeEntity.getUuid())).orElseGet(ArrayList::new);
        List<Host> hosts = new ArrayList<>();
        data.getHostPoolList().forEach(hostPool -> {
            //hostpool下面是集群
            hostPool.getClusterList().forEach(cluster -> {
                List<Host> hostList = cluster.getHostList();
                hostList.forEach(host->{
                    Optional<OperationMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk->{
                        try {
                            CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(),CasMonitorInfo.class);
                            return casMonitorInfo.getHostId().equals(host.getHostId());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).findFirst();
                    if (beenAddCvk.isPresent()){
                        host.setBeenAdd(true);
                    }else {
                        host.setBeenAdd(false);
                    }
                    host.setClusterId(cluster.getClusterId());
                    host.setHostpoolId(hostPool.getId());
                    host.getVirtualMachineList().forEach(virtualMachine -> {
                        Optional<OperationMonitorEntity> beenAddVm = vmList.stream().filter(vm->{
                            try {
                                CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(),CasMonitorInfo.class);
                                return vmMonitorInfo.getVmId().equals(virtualMachine.getVmId());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }).findFirst();
                        if (beenAddVm.isPresent()){
                            virtualMachine.setBeenAdd(true);
                        }else {
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
                Optional<OperationMonitorEntity> beenAddCvk = cvkList.stream().filter(cvk->{
                    try {
                        CasMonitorInfo casMonitorInfo = mapper.readValue(cvk.getMonitorInfo(),CasMonitorInfo.class);
                        return casMonitorInfo.getHostId().equals(host.getHostId());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }).findFirst();
                if (beenAddCvk.isPresent()){
                    host.setBeenAdd(true);
                }else {
                    host.setBeenAdd(false);
                }
                host.setHostpoolId(hostPool.getId());
                host.getVirtualMachineList().forEach(virtualMachine -> {
                    Optional<OperationMonitorEntity> beenAddVm = vmList.stream().filter(vm->{
                        try {
                            CasMonitorInfo vmMonitorInfo = mapper.readValue(vm.getMonitorInfo(),CasMonitorInfo.class);
                            return vmMonitorInfo.getVmId().equals(virtualMachine.getVmId());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).findFirst();
                    if (beenAddVm.isPresent()){
                        virtualMachine.setBeenAdd(true);
                    }else {
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
}
