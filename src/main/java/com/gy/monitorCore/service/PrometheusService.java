package com.gy.monitorCore.service;

import com.gy.monitorCore.entity.*;
import com.gy.monitorCore.entity.view.Cluster;
import com.gy.monitorCore.entity.view.Host;
import com.gy.monitorCore.entity.view.k8sView.Container;
import com.gy.monitorCore.entity.view.k8sView.Node;

import java.io.IOException;
import java.util.List;

/**
 * Created by gy on 2018/3/31.
 */
public interface PrometheusService {


    String getQuotaValue(String url);
}
