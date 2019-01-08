package com.gy.monitorCore.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.OperationMonitorEntity;
import com.gy.monitorCore.entity.TestEntity;
import com.gy.monitorCore.service.MonitorService;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * Created by gy on 2018/3/31.
 */
@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Autowired
    private MonitorService service;

    @Autowired
    private ObjectMapper mapper;

    @RequestMapping("jpa")
    @ResponseBody
    public TestEntity testJPA(HttpServletRequest request){
//        TestEntity entity = new TestEntity();
//        entity.setId("sasada");
//        entity.setName("gygy");
//        return entity;
        return service.getJPAInfo();
    }


    @RequestMapping("getMonitorRecord")
    @ResponseBody
    public String getOperationMonitorEntity(String uuid) throws Exception {
//        return service.getOperationMonitorEntity(uuid);
                return mapper.writeValueAsString(service.getOperationMonitorEntity(uuid));
    }


    @RequestMapping("getAllMonitorRecord")
    @ResponseBody
    public String getAllOperationMonitorEntity() throws Exception {
//        return service.getOperationMonitorEntity(uuid);
        return mapper.writeValueAsString(service.getAllOperationMonitorEntity());
    }


    @RequestMapping("getMiddleType")
    @ResponseBody
    public String getMiddleTypeEntity() throws Exception {
        return mapper.writeValueAsString(service.getMiddleTypeEntity());
    }

    @RequestMapping("getLightType")
    @ResponseBody
    public String getLightTypeEntity() throws Exception {
        return mapper.writeValueAsString(service.getLightTypeEntity());
    }

    @RequestMapping("addMonitorRecord")
    @ResponseBody
    public boolean insertMonitorRecord(@RequestBody String data) throws IOException {
        OperationMonitorEntity view = mapper.readValue(data,OperationMonitorEntity.class);
        return service.insertMonitorRecord(view);
    }

    @RequestMapping("getClusterList")
    @ResponseBody
    public String getClusterListByExporter(@RequestBody String data) throws Exception {
        CasTransExporterModel model = mapper.readValue(data,CasTransExporterModel.class);
        return mapper.writeValueAsString(service.getClusterListByExporter(model));
    }

    @RequestMapping("addMonitorRecordList")
    @ResponseBody
    public boolean insertMonitorRecordList(@RequestBody String data) throws IOException {

        List<OperationMonitorEntity> list = mapper.readValue(data,new TypeReference<List<OperationMonitorEntity>>(){});
        return service.insertMonitorRecordList(list);
    }

    @RequestMapping("getCvkAndVmList")
    @ResponseBody
    public String getCvkAndVmListByExporter(@RequestBody String data) throws Exception {
        CasTransExporterModel model = mapper.readValue(data,CasTransExporterModel.class);
        return mapper.writeValueAsString(service.getCvkAndVmListByExporter(model));
    }

    @RequestMapping("getContainerList")
    @ResponseBody
    public String getContainerListByExporter(String ip,String port) throws JsonProcessingException {
        return mapper.writeValueAsString(service.getContainerListByExporter(ip,port));
    }
    @RequestMapping("getNodeList")
    @ResponseBody
    public String getNodeListByExporter(String ip,String port) throws JsonProcessingException {
        return mapper.writeValueAsString(service.getNodeListByExporter(ip,port));
    }

    @RequestMapping("/delNetworkMonitorRecord")
    @ResponseBody
    public boolean delMonitorRecord(String uuid){
        return service.delMonitorRecord(uuid);
    }


    @RequestMapping("getMonitorRecordByRootId")
    @ResponseBody
    public String getMonitorRecordByRootId(String uuid) throws Exception {
        return mapper.writeValueAsString(service.getMonitorRecordByRootId(uuid));
    }

    @RequestMapping("getMonitorRecordByTemplateId")
    @ResponseBody
    public String getMonitorRecordByTemplateId(String uuid) throws Exception {
        return mapper.writeValueAsString(service.getMonitorRecordByTemplateId(uuid));
    }


    @RequestMapping("getClusterIP")
    @ResponseBody
    public String testgetClusterIp() throws Exception {
        return service.testgetClusterIp();
    }

    @RequestMapping("getQuotaValueByName")
    @ResponseBody
    public String getQuotaValueByName(String monitorUUid,String quotaName){
        return service.getQuotaValueByName(monitorUUid,quotaName);
    }
}
