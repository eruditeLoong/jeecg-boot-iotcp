package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.DeviceEvent;
import org.jeecg.modules.device.mapper.DeviceEventMapper;
import org.jeecg.modules.device.service.IDeviceEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 事件定义
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class DeviceEventServiceImpl extends ServiceImpl<DeviceEventMapper, DeviceEvent> implements IDeviceEventService {
	
	@Autowired
	private DeviceEventMapper deviceEventMapper;
	
	@Override
	public List<DeviceEvent> selectByMainId(String mainId) {
		return deviceEventMapper.selectByMainId(mainId);
	}
}
