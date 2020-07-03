package org.jeecg.modules.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.entity.DeviceModel;

import java.util.List;

/**
 * @Description: 设备实例
 * @Author: jeecg-boot
 * @Date: 2020-04-11
 * @Version: V1.0
 */
public interface DeviceInstanceMapper extends BaseMapper<DeviceInstance> {

    /**
     * 获取网关类型的实例设备
     *
     * @param modelType
     * @return
     */
    public List<DeviceInstance> getInsDeviceByModelType(@Param("modelType") String modelType);

    /**
     * 根据设备实例的ModelId，获取设备模型
     *
     * @param modelId
     * @return
     */
    public DeviceModel getDeviceModelByModelId(@Param("modelId") String modelId);

    /**
     * 获取在线实例列表，根据tcp地址
     *
     * @param address
     * @return
     */
    public List<DeviceInstance> listOnlineInstanceByAddress(@Param("address") String address);
}
