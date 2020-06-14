package org.jeecg.modules.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.netty.channel.ChannelHandlerContext;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.vo.DeviceFunctionExec;

import java.util.List;

/**
 * @Description: 设备实例
 * @Author: jeecg-boot
 * @Date: 2020-04-11
 * @Version: V1.0
 */
public interface IDeviceInstanceService extends IService<DeviceInstance> {

    public List<DeviceInstance> listInstanceDeviceByModelType(String modelType);

    /**
     * 根据IP地址获取设备实例
     *
     * @param ip
     * @return 设备实例
     */
    public DeviceInstance getInstanceDeviceByIp(String ip);

    /**
     * 根据TCP地址(IPL:PORT)获取设备实例
     *
     * @param address <p>IP:PORT</p>
     * @return 设备实例
     */
    public DeviceInstance getInstanceDeviceByAddress(String address);

    /**
     * 根据设备实例id，获取网络连接通道
     *
     * @param instanceId
     * @return
     */
    public ChannelHandlerContext getNetworkChannel(String instanceId) throws Exception;

    /**
     * 获取设备实例的父级网关设备实例
     *
     * @return
     */
    public DeviceInstance getParantGateway(String instanceId);

    /**
     * 设置实例设备的功能执行配置信息
     * @param deviceFunctionExec
     * @param isRunning
     */
    public void setFuncExecConf(DeviceFunctionExec deviceFunctionExec, boolean isRunning);

    /**
     * 删除执行任务
     * @param functionExec
     */
    public void deleteExecJob(DeviceFunctionExec functionExec);

    /**
     * 添加执行任务
     * @param functionExec
     */
    public void addExecJob(DeviceFunctionExec functionExec);

    /**
     * 获取子实例列表，根据实例id
     * @param id
     * @return
     */
    public List<DeviceInstance> listInstanceChild(String id);
}
