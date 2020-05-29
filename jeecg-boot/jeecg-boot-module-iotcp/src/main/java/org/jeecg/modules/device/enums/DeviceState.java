package org.jeecg.modules.device.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceState {
    NOT_ACTIVE("未激活", "notActive"),
    OFFLINE("离线", "offline"),
    ONLINE("在线", "online");

    private String name;
    private String code;

}
