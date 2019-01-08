package com.gy.monitorCore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by gy on 2019/1/8.
 */
@Getter
@Setter
public class InstantValue {


    @JsonProperty("metric")
    private Map<String,String> metric;

    @JsonProperty("value")
    private List<String> value;
}
