package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.DeviceData;
import org.jeecg.modules.device.mapper.DeviceDataMapper;
import org.jeecg.modules.device.service.IDeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 数据节点
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceData> implements IDeviceDataService {
	
	@Autowired
	private DeviceDataMapper deviceDataMapper;
	
	@Override
	public List<DeviceData> selectByMainId(String mainId) {
		return deviceDataMapper.selectByMainId(mainId);
	}
}
