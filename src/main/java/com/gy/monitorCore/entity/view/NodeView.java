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
public class NodeView {

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("name")
    private String name;

    @JsonProperty("pods")
    private List<PodView> pods;
}
