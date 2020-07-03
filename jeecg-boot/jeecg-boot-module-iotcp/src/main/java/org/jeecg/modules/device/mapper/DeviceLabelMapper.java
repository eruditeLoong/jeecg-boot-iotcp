package org.jeecg.modules.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.device.entity.DeviceLabel;

import java.util.List;

/**
 * @Description: 数据标签
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface DeviceLabelMapper extends BaseMapper<DeviceLabel> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DeviceLabel> selectByMainId(@Param("mainId") String mainId);
}
