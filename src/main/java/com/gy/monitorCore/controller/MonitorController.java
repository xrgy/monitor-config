package com.gy.monitorCore.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gy.monitorCore.entity.OperationMonitorEntity;
import com.gy.monitorCore.entity.TestEntity;
import com.gy.monitorCore.service.MonitorService;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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
}
