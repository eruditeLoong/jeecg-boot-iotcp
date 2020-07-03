package org.jeecg.modules.device.listener;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InstanceStateListener {

    @Autowired
    private IDeviceInstanceService deviceInstanceService;

    // 异步监听器
    @Async
    @EventListener(DeviceInstance.class)
    public void deviceStateUpdateEven(DeviceInstance deviceInstance) {

        log.info("设备状态更新事件：{}", deviceInstance);

        batchUpdateInstDeviceState(deviceInstance);
    }

    private void batchUpdateInstDeviceState(DeviceInstance deviceInstance) {
        // 1、更新当前设备状态
        DeviceInstance temp = deviceInstanceService.getById(deviceInstance.getId());
        temp.setStatus(deviceInstance.getStatus());
        deviceInstanceService.updateById(temp);

        // 2、查询子集设备
        List<DeviceInstance> list = deviceInstanceService.lambdaQuery()
                .eq(DeviceInstance::getParentBy, deviceInstance.getId())
                .ne(DeviceInstance::getStatus, "notActive")
                .list();

        if (list != null && list.size() > 0) {
            for (DeviceInstance di : list) {
                di.setStatus(deviceInstance.getStatus());
                batchUpdateInstDeviceState(di);
            }
        }
    }

}
