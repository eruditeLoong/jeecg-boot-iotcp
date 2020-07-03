package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.DeviceLabel;
import org.jeecg.modules.device.mapper.DeviceLabelMapper;
import org.jeecg.modules.device.service.IDeviceLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 数据标签
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class DeviceLabelServiceImpl extends ServiceImpl<DeviceLabelMapper, DeviceLabel> implements IDeviceLabelService {
	
	@Autowired
	private DeviceLabelMapper deviceLabelMapper;
	
	@Override
	public List<DeviceLabel> selectByMainId(String mainId) {
		return deviceLabelMapper.selectByMainId(mainId);
	}
}
