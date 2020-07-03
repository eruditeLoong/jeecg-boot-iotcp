package org.jeecg.modules.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.entity.DeviceModel;
import org.jeecg.modules.device.mapper.DeviceInstanceMapper;
import org.jeecg.modules.device.quartzJob.ExecInstanceFunc;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.jeecg.modules.device.vo.DeviceFuncExecConf;
import org.jeecg.modules.device.vo.DeviceFunctionExec;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ApplicationEventPublisher publisher;

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

    @Override
    public void setFuncExecConf(DeviceFunctionExec deviceFunctionExec, boolean isRunning) {
        String deviceInstanceId = deviceFunctionExec.getDeviceInstanceId();
        DeviceInstance deviceInstance = this.getById(deviceInstanceId);
        DeviceFuncExecConf deviceFuncExecConf = new DeviceFuncExecConf(
                deviceFunctionExec.getExecConfig().getCorn(),
                deviceFunctionExec.getExecConfig().getExecMode(),
                isRunning);

        JSONObject json = new JSONObject();
        json = JSONObject.parseObject(deviceInstance.getFuncExecConf());
        json.put(deviceFunctionExec.getFuncCode(), deviceFuncExecConf);

        deviceInstance.setFuncExecConf(json.toJSONString());
        log.info("更新执行配置：{}", this.updateById(deviceInstance));

    }

    /**
     * 删除执行任务
     *
     * @param functionExec
     */
    @Override
    public void deleteExecJob(DeviceFunctionExec functionExec) {
        try {
            String triggerKey = ExecInstanceFunc.class.getCanonicalName()
                    + "-" + functionExec.getDeviceInstanceId()
                    + "-" + functionExec.getFuncCode();
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerKey));
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerKey));
            scheduler.deleteJob(JobKey.jobKey(triggerKey));
            // scheduler.clear();
        } catch (Exception e) {
            this.setFuncExecConf(functionExec, false);
            e.printStackTrace();
        }
    }

    /**
     * 添加执行任务
     *
     * @param functionExec
     */
    @Override
    public void addExecJob(DeviceFunctionExec functionExec) {
        try {
            String triggerKey = ExecInstanceFunc.class.getCanonicalName() + "-" + functionExec.getDeviceInstanceId() + "-" + functionExec.getFuncCode();
            scheduler.start();
            // 构建job信息
            JobDetail jobDetail = JobBuilder
                    .newJob(ExecInstanceFunc.class)
                    .withIdentity(triggerKey)
                    .usingJobData("deviceInstanceId", functionExec.getDeviceInstanceId())
                    .usingJobData("deviceModelId", functionExec.getDeviceModelId())
                    .usingJobData("deviceFuncCode", functionExec.getFuncCode())
                    .build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(functionExec.getExecConfig().getCorn());
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            this.setFuncExecConf(functionExec, true);
        } catch (Exception e) {
            this.setFuncExecConf(functionExec, false);
            e.printStackTrace();
        }
    }

    @Override
    public List<DeviceInstance> listInstanceChild(String id) {
        List<DeviceInstance> list = this.list();
        List<DeviceInstance> out = new ArrayList<DeviceInstance>();
        this.getInstanceChild(out, list, id);
        return out;
    }

    private void getInstanceChild(List<DeviceInstance> outList, List<DeviceInstance> list, String id){
        Iterator<DeviceInstance> iterator = list.iterator();
        while(iterator.hasNext()){
            DeviceInstance instance = iterator.next();
            if(id.equals(instance.getParentBy())){
                // iterator.remove();   //注意这个地方
                // list.remove(instance);
                List<DeviceInstance> list1 = new ArrayList<>();
                list1.addAll(list);
                list1.remove(instance);
                outList.add(instance);
                getInstanceChild(outList, list1, instance.getId());
            }
        }
    }
}
