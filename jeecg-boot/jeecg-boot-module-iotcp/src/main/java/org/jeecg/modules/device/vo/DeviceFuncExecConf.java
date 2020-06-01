package org.jeecg.modules.device.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeviceFuncExecConf {

    /**
     * corn表达式
     */
    String corn;

    /**
     * 执行模式：
     * 1、debug：调试模式，输入参数可修改，可同步|异步，只执行1次；
     * 2、task：任务模式，输入参数不可修改，只能异步，
     */
    String execMode;

    /**
     * 执行状态
     */
    boolean isRunning;
}
