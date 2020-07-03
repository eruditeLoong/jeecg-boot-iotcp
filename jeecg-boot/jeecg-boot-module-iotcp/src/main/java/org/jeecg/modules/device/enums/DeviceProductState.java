package org.jeecg.modules.device.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceProductState {
    unregistered("未发布", (byte) 0),
    registered("已发布", (byte) 1),
    other("其它", (byte) -100),
    forbidden("禁用", (byte) -1);

    private String text;
    private Byte value;
}
