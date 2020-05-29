package org.jeecg.modules.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.device.entity.DeviceFunction;

import java.util.List;

/**
 * @Description: 功能定义
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
public interface DeviceFunctionsMapper extends BaseMapper<DeviceFunction> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<DeviceFunction> selectByMainId(@Param("mainId") String mainId);
}
