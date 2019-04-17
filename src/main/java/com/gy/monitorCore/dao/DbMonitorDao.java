package com.gy.monitorCore.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gy.monitorCore.entity.view.AccessBackResult;
import com.gy.monitorCore.entity.view.AccessBackView;
import com.gy.monitorCore.entity.view.DbAccessView;
import com.gy.monitorCore.entity.view.k8sView.Resource;

/**
 * Created by gy on 2018/10/19.
 */
public interface DbMonitorDao {




    AccessBackResult dbCanAccess(DbAccessView view) throws JsonProcessingException;
}
