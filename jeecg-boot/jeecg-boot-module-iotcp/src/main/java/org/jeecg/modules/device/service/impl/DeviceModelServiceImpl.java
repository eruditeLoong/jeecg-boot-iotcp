package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.*;
import org.jeecg.modules.device.mapper.*;
import org.jeecg.modules.device.service.IDeviceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 设备模型
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class DeviceModelServiceImpl extends ServiceImpl<DeviceModelMapper, DeviceModel> implements IDeviceModelService {

	@Autowired
	private DeviceModelMapper deviceModelMapper;
	@Autowired
	private DeviceDataMapper deviceDataMapper;
	@Autowired
	private DeviceFunctionsMapper deviceFunctionsMapper;
	@Autowired
	private DeviceEventMapper deviceEventMapper;
	@Autowired
	private DeviceLabelMapper deviceLabelMapper;

	@Override
	@Transactional
	public void saveMain(DeviceModel deviceModel, List<DeviceData> deviceDataList, List<DeviceFunction> deviceFunctionList, List<DeviceEvent> deviceEventList, List<DeviceLabel> deviceLabelList) {
		deviceModelMapper.insert(deviceModel);
		if (deviceDataList != null && deviceDataList.size() > 0) {
			for (DeviceData entity : deviceDataList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceDataMapper.insert(entity);
			}
		}
		if (deviceFunctionList != null && deviceFunctionList.size() > 0) {
			for (DeviceFunction entity : deviceFunctionList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceFunctionsMapper.insert(entity);
			}
		}
		if (deviceEventList != null && deviceEventList.size() > 0) {
			for (DeviceEvent entity : deviceEventList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceEventMapper.insert(entity);
			}
		}
		if(deviceLabelList!=null && deviceLabelList.size()>0) {
			for(DeviceLabel entity:deviceLabelList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceLabelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(DeviceModel deviceModel, List<DeviceData> deviceDataList, List<DeviceFunction> deviceFunctionList, List<DeviceEvent> deviceEventList, List<DeviceLabel> deviceLabelList) {
		deviceModelMapper.updateById(deviceModel);

		//1.先删除子表数据
		deviceDataMapper.deleteByMainId(deviceModel.getId());
		deviceFunctionsMapper.deleteByMainId(deviceModel.getId());
		deviceEventMapper.deleteByMainId(deviceModel.getId());
		deviceLabelMapper.deleteByMainId(deviceModel.getId());

		//2.子表数据重新插入
		if (deviceDataList != null && deviceDataList.size() > 0) {
			for (DeviceData entity : deviceDataList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceDataMapper.insert(entity);
			}
		}
		if (deviceFunctionList != null && deviceFunctionList.size() > 0) {
			for (DeviceFunction entity : deviceFunctionList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceFunctionsMapper.insert(entity);
			}
		}
		if (deviceEventList != null && deviceEventList.size() > 0) {
			for (DeviceEvent entity : deviceEventList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceEventMapper.insert(entity);
			}
		}
		if(deviceLabelList!=null && deviceLabelList.size()>0) {
			for(DeviceLabel entity:deviceLabelList) {
				//外键设置
				entity.setDeviceModelBy(deviceModel.getId());
				deviceLabelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		deviceDataMapper.deleteByMainId(id);
		deviceFunctionsMapper.deleteByMainId(id);
		deviceEventMapper.deleteByMainId(id);
		deviceLabelMapper.deleteByMainId(id);
		deviceModelMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			deviceDataMapper.deleteByMainId(id.toString());
			deviceFunctionsMapper.deleteByMainId(id.toString());
			deviceEventMapper.deleteByMainId(id.toString());
			deviceLabelMapper.deleteByMainId(id.toString());
			deviceModelMapper.deleteById(id);
		}
	}
	
}
