package org.jeecg.modules.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.device.entity.DeviceEvent;

import java.util.List;

/**
 * @Description: 事件定义
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface DeviceEventMapper extends BaseMapper<DeviceEvent> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DeviceEvent> selectByMainId(@Param("mainId") String mainId);
}
