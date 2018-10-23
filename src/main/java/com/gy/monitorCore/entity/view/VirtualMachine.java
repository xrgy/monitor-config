package com.gy.monitorCore.entity.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gy on 2018/10/19.
 */
@Getter
@Setter
public class VirtualMachine {

    private String uuid;

    @JsonProperty("id")
    private String vmId;

    @JsonProperty("name")
    private String vmName;

    @JsonProperty("status")
    private String vmStatus;

    @JsonProperty("os")
    private String vmOs;

    @JsonProperty("ip")
    private String vmIp;

    private String cvkId;

    private String cvkName;

    private String hostpoolId;

    private boolean beenAdd;
}
