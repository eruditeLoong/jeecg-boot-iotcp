package org.jeecg.modules.device.quartzJob;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.device.service.IDeviceFunctionsService;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.jeecg.modules.device.vo.DeviceFunctionExec;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: jeecg-boot-module-iot
 * @description: 执行实例功能，需调用网络组件，发送请求
 * @author: zhouwr
 * @create: 2020-05-23 11:29
 * @version：1.0
 **/
@Component
@Slf4j
public class ExecInstanceFunc implements Job {
    @Autowired
    private IDeviceInstanceService deviceInstanceService;
    @Autowired
    private IDeviceFunctionsService deviceFunctionsService;

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String deviceInstanceId;
    private String deviceModelId;
    private String deviceFuncCode;

    public void setDeviceInstanceId(String deviceInstanceId) {
        this.deviceInstanceId = deviceInstanceId;
    }

    public void setDeviceModelId(String deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public void setDeviceFuncCode(String deviceFuncCode) {
        this.deviceFuncCode = deviceFuncCode;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("执行实例：{}, 功能：{}", deviceInstanceId, deviceFuncCode);
        // 网络连接
        try {
            DeviceFunctionExec functionExec = new DeviceFunctionExec(this.deviceModelId, this.deviceInstanceId, this.deviceFuncCode);
            deviceFunctionsService.execFunctionAsyn(functionExec);

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
