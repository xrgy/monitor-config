package com.gy.monitorCore.entity.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gy on 2019/1/13.
 */
@Getter
@Setter
public class ContainerView {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;
}
