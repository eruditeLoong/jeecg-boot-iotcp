package org.jeecg.modules.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.device.entity.DeviceData;
import org.jeecg.modules.device.mapper.DeviceDataMapper;
import org.jeecg.modules.device.mapper.DeviceFunctionsMapper;
import org.jeecg.modules.device.mapper.DeviceInstanceMapper;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.mapper.DataReportMapper;
import org.jeecg.modules.message.service.IDataReportService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备上传数据
 * @Author: jeecg-boot
 * @Date: 2020-05-14
 * @Version: V1.0
 */
@Slf4j
@Service
public class DataReportServiceImpl extends ServiceImpl<DataReportMapper, DataReport> implements IDataReportService {
    @Autowired
    private DeviceFunctionsMapper deviceFunctionsMapper;
    @Autowired
    private DeviceInstanceMapper deviceInstanceMapper;
    @Autowired
    private DeviceDataMapper deviceDataMapper;
    @Autowired
    private Scheduler scheduler;

    @Cacheable(value = CacheConstant.IOT_DEVICE_INSTANCE_DATA_NODES_CACHE, key = "#deviceInstanceId")
    @Override
    public JSONObject verifyBuildData(String deviceInstanceId, Map<String, Object> dataMap) {
        log.info("设备实例数据节点，无缓存调用。。。");
        JSONObject json = new JSONObject();

        List<DeviceData> deviceDataList = deviceDataMapper.listDeviceDataByInstanceId(deviceInstanceId);

        for (DeviceData deviceData : deviceDataList) {
            json.put(deviceData.getCode(), dataMap.get(deviceData.getCode()));
        }
        return json;
    }
}
