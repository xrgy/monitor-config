package com.gy.monitorCore.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.CasTransExporterModel;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.ResourceData;

import java.util.List;

/**
 * Created by gy on 2018/10/19.
 */
public interface MonitorExporterDao {


    /**
     * 获取cas资源列表
     * @param model
     * @return
     */
    ResourceData getCasResourceListByExporter(CasTransExporterModel model);

}
