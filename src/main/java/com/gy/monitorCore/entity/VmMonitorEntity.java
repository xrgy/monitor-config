package com.gy.monitorCore.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by gy on 2018/5/5.
 */
@Data
@Entity
@Table(name = "tbl_vm_monitor_record")
public class VmMonitorEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "ip")
    private String ip;

    @Column(name = "vmId")
    private String vmId;

    @Column(name = "cvkUuid")
    private String cvkUuid;

    @Column(name = "monitor_type")
    private String monitorType;

    @Column(name = "scrape_interval")
    private String scrapeInterval;

    @Column(name = "scrape_timeout")
    private String scrapeTimeout;

    @Column(name = "monitor_template_id")
    private String templateId;

}
