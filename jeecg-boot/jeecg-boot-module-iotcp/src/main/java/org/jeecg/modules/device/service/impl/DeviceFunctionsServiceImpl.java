package org.jeecg.modules.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.device.entity.*;
import org.jeecg.modules.device.enums.FuncExecMode;
import org.jeecg.modules.device.mapper.DeviceDataMapper;
import org.jeecg.modules.device.mapper.DeviceFunctionsMapper;
import org.jeecg.modules.device.mapper.DeviceInstanceMapper;
import org.jeecg.modules.device.quartzJob.ExecInstanceFunc;
import org.jeecg.modules.device.service.IDeviceFunctionsService;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.jeecg.modules.device.vo.DeviceFunctionExec;
import org.jeecg.modules.message.entity.function.FunctionInputStructure;
import org.jeecg.modules.message.entity.function.FunctionStructure;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.jeecg.modules.network.network.tcp.future.SyncWrite;
import org.jeecg.modules.network.network.tcp.msg.Request;
import org.jeecg.modules.network.network.tcp.msg.Response;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 功能定义
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Slf4j
@Service
public class DeviceFunctionsServiceImpl extends ServiceImpl<DeviceFunctionsMapper, DeviceFunction> implements IDeviceFunctionsService {
    @Autowired
    private DeviceFunctionsMapper deviceFunctionsMapper;
    @Autowired
    private DeviceInstanceMapper deviceInstanceMapper;
    @Autowired
    private DeviceDataMapper deviceDataMapper;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private FunctionData functionData;

    @Override
    public List<DeviceFunction> selectByMainId(String mainId) {
        return deviceFunctionsMapper.selectByMainId(mainId);
    }

    @Override
    public List<InputParam> listInputParamByFuncId(String funcId) {
        DeviceFunction func = deviceFunctionsMapper.selectById(funcId);
        if (func == null) {
            return new ArrayList<>();
        }
        String inputParams = func.getInputParams();
        return JSONArray.parseArray(StringUtils.isEmpty(inputParams) ? "[{}]" : inputParams, InputParam.class);
    }

    @Override
    public List<InputData> listInputDataByFuncId(String funcId, String inputMode) {
        return listInputDataByFuncId(funcId).stream()
                .filter(inputData -> inputData.getInputMode().indexOf(inputMode) != -1)
                .collect(Collectors.toList());
    }

    @Override
    public List<InputData> listInputDataByFuncId(String funcId) {
        List<InputParam> inputParamList = listInputParamByFuncId(funcId);
        List<InputData> inputDataList = new ArrayList<>();
        for (InputParam inputParam : inputParamList) {
            InputData inputData = new InputData();
            DeviceData deviceData = deviceDataMapper.selectById(inputParam.getDataId());
            inputData.setDeviceData(deviceData);
            inputData.setInputMode(inputParam.getInputMode());
            inputDataList.add(inputData);
        }
        return inputDataList;
    }

    @Override
    public List<InputParam> listInputParam() {
        return null;
    }

    @Override
    public List<InputData> listInputData() {
        return null;
    }

    @Override
    public Boolean inputParamsCheckUnique(DeviceFunction deviceFunc) {
        List<InputParam> inputParamList = this.listInputParamByFuncId(deviceFunc.getId());
        for (InputParam inputParam : inputParamList) {
            int count = this.lambdaQuery()
                    .eq(DeviceFunction::getDeviceModelBy, deviceFunc.getDeviceModelBy())
                    .like(DeviceFunction::getInputParams, inputParam.getDataId())
                    .count();
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<DeviceFunction> getFuncWithParamValue(
            String deviceModelId,
            String deviceInstanceId,
            String deviceInstanceExtendParams) {
        List<DeviceFunction> deviceFunctionList = this.lambdaQuery()
                .eq(DeviceFunction::getDeviceModelBy, deviceModelId)
                .ne(DeviceFunction::getType, "auto")    // 过滤掉自动执行的功能function
                .list();

        // 将输入参数的数据节点id，换成数据节点对象信息
        for (DeviceFunction deviceFunction : deviceFunctionList) {
            JSONArray inputPatamsArr = JSONObject.parseArray(deviceFunction.getInputParams());
            // 遍历设备模型功能定义的输入参数
            JSONArray inputPatamsArrTemp = new JSONArray();
            for (Object o : inputPatamsArr) {
                JSONObject inputPatamsJson = (JSONObject) o;
                DeviceData deviceData = deviceDataMapper.selectById(inputPatamsJson.getString("dataId"));

                if (deviceData == null) {
                    continue;
                }

                // 给deviceData对象注入数据，从设备实例中获取
                JSONObject instanceExtendParams = JSONObject.parseObject(deviceInstanceExtendParams);
                // 判断设备实例扩展参数有无
                if (instanceExtendParams.size() <= 0) {
                    continue;
                }

                JSONObject instanceInputParams = instanceExtendParams.getJSONObject(deviceFunction.getCode());
                Object inputValue = instanceInputParams.get(deviceData.getCode());
                JSONObject deviceDataJson = (JSONObject) JSONObject.toJSON(deviceData);
                // 转换valueType
                deviceDataJson.put("valueType", JSONObject.parseObject(deviceDataJson.getString("valueType")));
                deviceDataJson.put("value", inputValue);
                // 添加 deviceData 对象
                inputPatamsJson.putAll(deviceDataJson);
                inputPatamsArrTemp.add(inputPatamsJson);
            }
            deviceFunction.setInputParams(inputPatamsArrTemp.toJSONString());
        }
        return deviceFunctionList;
    }

    /**
     * 异步执行功能
     * @param functionExec
     */
    @Override
    public void execFunctionAsyn(DeviceFunctionExec functionExec) {
        try {
            ChannelHandlerContext channelHandlerContext = getNetworkChannel(functionExec.getDeviceInstanceId());
            if (channelHandlerContext == null) {
                log.error("没有获取连接通道，终止执行功能！");
                DeviceInstance deviceInstance = deviceInstanceMapper.selectById(functionExec.getDeviceInstanceId());
                log.info(deviceInstance.toString());
                if (deviceInstance != null) {
                    deviceInstance.setStatus("offline");
                    publisher.publishEvent(deviceInstance);
                }

                // String triggerKey = ExecInstanceFunc.class.getCanonicalName() + "-" + deviceFuncCode;
                // log.info("触发键: {}", triggerKey);
                // try {
                //     scheduler.pauseTrigger(TriggerKey.triggerKey(triggerKey));
                //     scheduler.unscheduleJob(TriggerKey.triggerKey(triggerKey));
                //     scheduler.deleteJob(JobKey.jobKey(triggerKey));
                // }catch (Exception e){
                //     e.printStackTrace();
                // }
                return;
            }

            // 将参数转换成json
            JSONObject writeJson = new JSONObject();
            writeJson.put("deviceInstanceId", functionExec.getDeviceInstanceId());
            writeJson.put("deviceModelId", functionExec.getDeviceModelId());
            writeJson.put("function", functionData.buildFuncDataStructure(functionExec));

            log.info("writeJson: {}", writeJson);
            // 发送数据
            channelHandlerContext.channel().writeAndFlush(writeJson.toJSONString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 同步执行功能
     * @param functionExec
     * @return
     */
    @Override
    public Object execFunctionSync(DeviceFunctionExec functionExec) {
        try {
            ChannelHandlerContext channelHandlerContext = getNetworkChannel(functionExec.getDeviceInstanceId());
            if (channelHandlerContext == null) {
                log.error("没有获取连接通道，终止执行功能！");
                return null;
            }

            // 将参数转换成json
            JSONObject writeJson = new JSONObject();
            writeJson.put("deviceInstanceId", functionExec.getDeviceInstanceId());
            writeJson.put("deviceModelId", functionExec.getDeviceModelId());

            writeJson.put("function", functionData.buildFuncDataStructure(functionExec).toJsonString());

            log.info("writeJson: {}", writeJson);

            // 发送数据
            ChannelFuture channelFuture = channelHandlerContext.channel().writeAndFlush(writeJson.toJSONString());

            return "555555";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据设备实例id，获取网络连接通道
     *
     * @param instanceId
     * @return
     */
    public ChannelHandlerContext getNetworkChannel(String instanceId) throws Exception {
        DeviceInstance deviceInstance = getParantGateway(instanceId);
        if ("notActive".equals(deviceInstance.getStatus())) {
            log.error("网关设备实例尚未激活！");
            return null;
        } else if ("offline".equals(deviceInstance.getStatus())) {
            log.error("网关设备实例离线！");
            return null;
        } else {
        }
        Map<String, NetworkConnect> connectMap = NetworkConnectStore.getNetworkConnectMap();
        Set<String> addrSet = connectMap.keySet();
        for (String addr : addrSet) {
            List<DeviceInstance> deviceInstanceList = deviceInstanceMapper.listOnlineInstanceByAddress(addr);
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
    public DeviceInstance getParantGateway(String instanceId) {
        DeviceInstance deviceInstance = deviceInstanceMapper.selectById(instanceId);
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

@Slf4j
@Component
class FunctionData{
    @Autowired
    private DeviceInstanceMapper deviceInstanceMapper;
    @Autowired
    private IDeviceFunctionsService deviceFunctionsService;

    /**
     * 构建函数数据结构
     * @param functionExec
     * @return
     */
    @Cacheable(value = CacheConstant.IOT_BUILD_FUNC_DATA_STRUCTURE_CACHE,
            key = "#functionExec.deviceModelId+'-'+#functionExec.deviceInstanceId+'-'+#functionExec.funcCode")
    public FunctionStructure buildFuncDataStructure(DeviceFunctionExec functionExec) {
        log.info("构建器函数数据结构, 重新缓存。。。");

        DeviceInstance deviceInstance = deviceInstanceMapper.selectById(functionExec.getDeviceInstanceId());
        List<DeviceFunction> deviceFunctionList =
                deviceFunctionsService.getFuncWithParamValue(functionExec.getDeviceModelId(), functionExec.getDeviceInstanceId(), deviceInstance.getExtendParams())
                        .stream()
                        .filter(deviceFunction -> deviceFunction.getCode().equals(functionExec.getFuncCode()))
                        .collect(Collectors.toList());

        DeviceFunction deviceFunction = deviceFunctionList.get(0);

        JSONArray inputArray = JSONArray.parseArray(deviceFunction.getInputParams().replaceAll("\\\\", ""));

        List<FunctionInputStructure> inputStructures = inputArray.stream()
                .flatMap(object -> {
                    JSONObject json = (JSONObject) object;
                    FunctionInputStructure inputStructure = new FunctionInputStructure(
                            json.getString("id"),
                            json.getString("code"),
                            json.getString("name"),
                            json.getString("inputMode"),
                            json.getJSONObject("valueType"),
                            json.getString("deviceModelId"),
                            json.getString("rwAuthor"),
                            json.getString("value")
                    );
                    System.out.println(inputStructure.toString());
                    return Stream.of(inputStructure);
                }).collect(Collectors.toList());

        FunctionStructure functionStructure = new FunctionStructure(
                deviceFunction.getId(),
                deviceFunction.getName(),
                deviceFunction.getCode(),
                inputStructures,
                deviceFunction.getOutputData());


        // JSONObject json = new JSONObject();
        // json.put("id", deviceFunction.getId());
        // json.put("name", deviceFunction.getName());
        // json.put("code", deviceFunction.getCode());
        // json.put("inputs", JSONArray.parseArray(deviceFunction.getInputParams().replaceAll("\\\\", "")));
        // json.put("output", deviceFunction.getOutputData());

        return functionStructure;
    }


}
