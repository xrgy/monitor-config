package com.gy.monitorCore.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.dao.CasMonitorDao;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.HostPool;
import com.gy.monitorCore.entity.view.ResourceData;
import com.gy.monitorCore.entity.view.VirtualMachine;
import com.gy.monitorCore.entity.view.k8sView.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gy on 2018/10/19.
 */
@Repository
public class CasMonitorDaoImpl implements CasMonitorDao{
//    private static final String IP = "http://127.0.0.1";
//    private static final String CAS_PORT = "9107";
    private static final String IP = "http://172.31.105.232";
//    private static final String IP = "http://172.17.5.135";
    private static final String CAS_PORT = "30107";
    private static final String PATH_GET_RESOURCE_LIST = "/v1/cas/resourceList";
    private static final String PATH_LIGHT="/v1/cas/clusterList";
    private static final String PATH_GET_KUBERNETES_RESOURCE_LIST="/api/v1/resources";

    private String virtualExporterPrefix() {
        return IP + ":" + CAS_PORT;
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public ResourceData getCasResourceListByExporter(CasTransExporterModel model) {
        List<Host> hosts = new ArrayList<>();
        Host host1 = new Host();
        host1.setHostName("cvk1");
        host1.setHostId("111111");
        host1.setHostStatus("1");
        host1.setHostIp("172.17.5.135");
        host1.setBeenAdd(false);
        host1.setClusterId("1");
        host1.setHostpoolId("1");
        List<VirtualMachine> vms1 = new ArrayList<>();
        VirtualMachine vm1 = new VirtualMachine();
        vm1.setVmName("vm1");
        vm1.setVmId("1");
        vm1.setVmStatus("1");
        vm1.setVmOs("linux");
        vm1.setVmIp("172.17.5.133");
        vm1.setBeenAdd(false);
        vm1.setCvkId("111111");
        vms1.add(vm1);
        VirtualMachine vm2 = new VirtualMachine();
        vm2.setVmName("vm2");
        vm2.setVmId("2");
        vm2.setVmStatus("1");
        vm2.setVmOs("linux");
        vm2.setVmIp("172.17.5.134");
        vm2.setBeenAdd(false);
        vm2.setCvkId("111111");
        vms1.add(vm2);
        host1.setVirtualMachineList(vms1);
        hosts.add(host1);

        Host host2 = new Host();
        host2.setHostName("cvk2");
        host2.setHostId("111112");
        host2.setHostStatus("1");
        host2.setHostIp("172.17.5.133");
        host2.setBeenAdd(false);
        host2.setClusterId("1");
        host2.setHostpoolId("1");
        List<VirtualMachine> vms2 = new ArrayList<>();
        VirtualMachine vm3 = new VirtualMachine();
        vm3.setVmName("vm3");
        vm3.setVmId("3");
        vm3.setVmStatus("1");
        vm3.setVmOs("linux");
        vm3.setVmIp("172.17.5.139");
        vm3.setCvkId("111112");
        vm3.setBeenAdd(false);
        vms2.add(vm3);
        host2.setVirtualMachineList(vms2);
        hosts.add(host2);

        ResourceData data =new ResourceData();
        List<HostPool> hostPoolList = new ArrayList<>();
        HostPool hostPool = new HostPool();
        hostPool.setHostpoolId("1");
        hostPool.setHostList(hosts);
        hostPoolList.add(hostPool);
        data.setHostPoolList(hostPoolList);
        return data;
//        ResponseEntity<String> response = rest().getForEntity(virtualExporterPrefix()+PATH_GET_RESOURCE_LIST+
//                "?ip={1}&port={2}&username={3}&password={4}",String.class,model.getIp(),model.getPort(),model.getUsername(),model.getPassword());
//        try {
//            return objectMapper.readValue(response.getBody(),ResourceData.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    public Resource getK8sResourceListByExporter(String ip, String port) {

        return null;
    }
}
