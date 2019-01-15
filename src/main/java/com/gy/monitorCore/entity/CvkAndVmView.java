package com.gy.monitorCore.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by gy on 2019/1/15.
 */
@Getter
@Setter
public class CvkAndVmView {

    HostMonitorEntity hostMonitor;

    List<VmMonitorEntity> vmMonitorList;
}
