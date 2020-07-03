package org.jeecg.modules.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.device.entity.DeviceData;

import java.util.List;

/**
 * @Description: 数据节点
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IDeviceDataService extends IService<DeviceData> {

	public List<DeviceData> selectByMainId(String mainId);
}
