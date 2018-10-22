package com.gy.monitorCore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by gy on 2018/10/22.
 */
@Getter
@Setter
public class MonitorEtcdView {

    @JsonProperty("relabel_configs")
    private List<Map<String,Object>> relabelConfigs;

    @JsonProperty("job_name")
    private String jobName;

    @JsonProperty("scrape_timeout")
    private String scrapeTimeout;

    @JsonProperty("scrape_interval")
    private String scrapeInterval;

    @JsonProperty("metrics_path")
    private String metricsPath;

    @JsonProperty("static_configs")
    private List<Map<String,Object>> staticConfigs;
}
