package com.gy.monitorCore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.transform.Result;

/**
 * Created by gy on 2019/1/8.
 */
@Getter
@Setter
public class InstantData {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private InstantResult data;
}
