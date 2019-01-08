package com.gy.monitorCore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by gy on 2019/1/8.
 */
@Getter
@Setter
public class InstantResult {

    @JsonProperty("resultType")
    private String resultType;

    @JsonProperty("result")
    private List<InstantValue> result;
}
