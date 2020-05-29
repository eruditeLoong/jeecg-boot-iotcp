package org.jeecg.modules.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.device.entity.DeviceEvent;

import java.util.List;

/**
 * @Description: 事件定义
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IDeviceEventService extends IService<DeviceEvent> {

	public List<DeviceEvent> selectByMainId(String mainId);
}
