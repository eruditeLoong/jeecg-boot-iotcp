package org.jeecg.modules.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.device.entity.DeviceData;

import java.util.List;

/**
 * @Description: 数据节点
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface DeviceDataMapper extends BaseMapper<DeviceData> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<DeviceData> selectByMainId(@Param("mainId") String mainId);
}
