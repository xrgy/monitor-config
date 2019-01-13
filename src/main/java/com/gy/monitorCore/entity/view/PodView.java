package com.gy.monitorCore.entity.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by gy on 2019/1/13.
 */
@Getter
@Setter
public class PodView {

    @JsonProperty("name")
    private String name;

    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("containers")
    private List<ContainerView> containers;
}
