package org.jeecg.modules.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.device.entity.DeviceLabel;

import java.util.List;

/**
 * @Description: 数据标签
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IDeviceLabelService extends IService<DeviceLabel> {

	public List<DeviceLabel> selectByMainId(String mainId);
}
