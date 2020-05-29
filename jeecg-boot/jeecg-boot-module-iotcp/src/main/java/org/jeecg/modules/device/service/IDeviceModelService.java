package org.jeecg.modules.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.device.entity.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 设备模型
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IDeviceModelService extends IService<DeviceModel> {

	/**
	 * 添加一对多
	 */
	public void saveMain(DeviceModel deviceModel, List<DeviceData> deviceDataList, List<DeviceFunction> deviceFunctionList, List<DeviceEvent> deviceEventList, List<DeviceLabel> deviceLabelList);

	/**
	 * 修改一对多
	 */
	public void updateMain(DeviceModel deviceModel, List<DeviceData> deviceDataList, List<DeviceFunction> deviceFunctionList, List<DeviceEvent> deviceEventList, List<DeviceLabel> deviceLabelList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
