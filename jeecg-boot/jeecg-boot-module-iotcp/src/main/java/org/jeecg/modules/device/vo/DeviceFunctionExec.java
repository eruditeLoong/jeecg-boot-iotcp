package org.jeecg.modules.device.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @program: jeecg-boot-module-iot
 * @description: 设备功能执行
 * @author: zhouwr
 * @create: 2020-05-27 11:29
 * @version：1.0
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeviceFunctionExec implements Serializable {
    private static final long serialVersionUID = 1L;

    // 设备模型id
    String deviceModelId;

    // 设备实例id
    String deviceInstanceId;

    // 功能名
    String funcCode;

    // 输入参数
    Map<String, Object> inputParams;

    // 输出参数
    Object outputParam;

    // 执行配置
    DeviceFuncExecConf execConfig;

    public DeviceFunctionExec(String deviceModelId, String deviceInstanceId, String deviceFuncCode) {
        this.deviceInstanceId = deviceInstanceId;
        this.deviceModelId = deviceModelId;
        this.funcCode = deviceFuncCode;
    }
}
