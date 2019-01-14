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
@Table(name = "tbl_k8scontainer_monitor_record")
public class K8scontainerMonitorEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "pod_name")
    private String pod_name;

    @Column(name = "pod_namespace")
    private String pod_namespace;

    @Column(name = "container_id")
    private String container_id;

    @Column(name = "k8snodeUuid")
    private String k8snodeUuid;
    
    @Column(name = "monitor_type")
    private String monitorType;

    @Column(name = "scrape_interval")
    private String scrapeInterval;

    @Column(name = "scrape_timeout")
    private String scrapeTimeout;

    @Column(name = "monitor_template_id")
    private String templateId;

}
