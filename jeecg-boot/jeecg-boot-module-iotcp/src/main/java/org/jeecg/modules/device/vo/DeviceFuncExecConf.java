package org.jeecg.modules.device.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceFuncExecConf {

    /**
     * corn表达式
     */
    String corn;

    /**
     * 执行次数
     * 0：不停止
     */
    Integer execTime;

    /**
     * 是否同步执行
     * 1、true：同步
     * 2、false：异步
     */
    boolean isSync;

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
