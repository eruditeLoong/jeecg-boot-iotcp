package org.jeecg.modules.device.entity;

import lombok.Data;

@Data
public class InputParam {

    /**
     * 设备数据id
     */
    private String dataId;

    /**
     * 输入方式
     */
    private String inputMode;
}
