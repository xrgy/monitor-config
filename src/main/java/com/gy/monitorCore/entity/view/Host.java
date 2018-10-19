package com.gy.monitorCore.entity.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by gy on 2018/10/19.
 */
@Getter
@Setter
public class Host {

    @JsonProperty("name")
    private String hostName;

    @JsonProperty("id")
    private String hostId;

    @JsonProperty("status")
    private String hostStatus;

    @JsonProperty("ip")
    private String hostIp;

    private String clusterId;

    private String hostpoolId;

    private boolean beenAdd;

    @JsonProperty("vm")
    private List<VirtualMachine> virtualMachineList;
}
