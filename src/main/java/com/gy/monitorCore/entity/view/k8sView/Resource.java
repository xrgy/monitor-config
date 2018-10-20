package com.gy.monitorCore.entity.view.k8sView;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by gy on 2018/10/20.
 */
@Getter
@Setter
public class Resource {
    private List<Node> nodes;
}
