package org.jeecg.modules.device.entity;

import lombok.Data;

@Data
public class InputData {

    /**
     * 参数集合
     */
    private DeviceData deviceData;

    /**
     * 输入方式
     */
    private String inputMode;
}
