package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.entity.DeviceModel;
import org.jeecg.modules.device.mapper.DeviceInstanceMapper;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 设备实例
 * @Author: jeecg-boot
 * @Date: 2020-04-11
 * @Version: V1.0
 */
@Slf4j
@Service
public class DeviceInstanceServiceImpl extends ServiceImpl<DeviceInstanceMapper, DeviceInstance> implements IDeviceInstanceService {

    @Autowired
    private DeviceInstanceMapper deviceInstanceMapper;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public List<DeviceInstance> listInstanceDeviceByModelType(String modelType) {
        return deviceInstanceMapper.getInsDeviceByModelType(modelType);
    }

    @Override
    public DeviceInstance getInstanceDeviceByIp(String ip) {
        List<DeviceInstance> deviceInstanceList = this.lambdaQuery()
                .like(DeviceInstance::getExtendParams, ip)
                .list();
        log.info("\n根据IP:{},获取设备实例：\n\t{}", ip, deviceInstanceList.toString());
        if (deviceInstanceList == null) {
            throw new RuntimeException("根据IP地址获取不到设备实例！请将实例配置信息的本地地址设置为：" + ip);
        }
        if (deviceInstanceList.size() < 1) {
            throw new RuntimeException("根据IP地址获取不到设备实例！请将实例配置信息的本地地址设置为：" + ip);
        }
        if (deviceInstanceList.size() != 1) {
            throw new RuntimeException("根据IP地址获取到的设备实例不唯一！请将实例配置信息的本地地址设置为：" + ip);
        }
        return deviceInstanceList.get(0);
    }

    @Override
    public DeviceInstance getInstanceDeviceByAddress(String address) {
        List<DeviceInstance> deviceInstanceList = this.lambdaQuery()
                .like(DeviceInstance::getExtendParams, address.substring(1))
                .list();
        log.info("\n根据TCP地址:{},获取设备实例：\n\t{}", address, deviceInstanceList.toString());
        // sysBaseAPI.addLog("根据TCP地址:" + address + ",获取设备实例", CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_1);

        if (deviceInstanceList == null) {
            log.warn("根据TCP地址获取不到设备实例！请将实例配置信息的本地地址设置为：" + address);
            return null;
        }
        if (deviceInstanceList.size() < 1) {
            log.warn("根据TCP地址获取不到设备实例！请将实例配置信息的本地地址设置为：" + address);
            return null;
        }
        if (deviceInstanceList.size() != 1) {
            log.warn("根据TCP地址获取到的设备实例不唯一！请将实例配置信息的本地地址设置为：" + address);
            return null;
        }
        return deviceInstanceList.get(0);
    }

    /**
     * 根据设备实例id，获取网络连接通道
     *
     * @param instanceId
     * @return
     */
    @Override
    public ChannelHandlerContext getNetworkChannel(String instanceId) throws Exception {
        DeviceInstance deviceInstance = getParantGateway(instanceId);
        if ("notActive".equals(deviceInstance.getStatus())) {
            throw new Exception("网关设备实例尚未激活！");
        } else if ("offline".equals(deviceInstance.getStatus())) {
            throw new Exception("网关设备实例离线！");
        } else {
        }
        Map<String, NetworkConnect> connectMap = NetworkConnectStore.getNetworkConnectMap();
        Set<String> addrSet = connectMap.keySet();
        for (String addr : addrSet) {
            List<DeviceInstance> deviceInstanceList = this.lambdaQuery()
                    .like(DeviceInstance::getExtendParams, addr)
                    .eq(DeviceInstance::getStatus, "online")
                    .list();
            if (deviceInstanceList != null && deviceInstanceList.size() > 0) {
                int count = deviceInstanceList.size();
                if (count > 1) {
                    throw new Exception("网关设备实例服务器地址配置冲突，请修改服务器地址唯一！");
                } else if (count == 1) {
                    if (deviceInstance.getId().equals(deviceInstanceList.get(0).getId())) {
                        return connectMap.get(addr).getChannelHandlerContext();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取设备实例的父级网关设备实例
     *
     * @return
     */
    @Override
    public DeviceInstance getParantGateway(String instanceId) {

        DeviceInstance deviceInstance = this.getById(instanceId);

        DeviceModel deviceModel = deviceInstanceMapper.getDeviceModelByModelId(deviceInstance.getModelBy());
        if ("gateway".equals(deviceModel.getType())) {
            return deviceInstance;
        } else {
            if (StringUtils.isEmpty(deviceInstance.getParentBy())) {
                throw new RuntimeException("找不到父级网关设备，请联系管理员！");
            }
            return getParantGateway(deviceInstance.getParentBy());
        }
    }

}
