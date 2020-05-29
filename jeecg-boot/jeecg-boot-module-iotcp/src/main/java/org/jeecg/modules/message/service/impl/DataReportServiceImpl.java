package org.jeecg.modules.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.DeviceData;
import org.jeecg.modules.device.mapper.DeviceDataMapper;
import org.jeecg.modules.device.mapper.DeviceFunctionsMapper;
import org.jeecg.modules.device.mapper.DeviceInstanceMapper;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.mapper.DataReportMapper;
import org.jeecg.modules.message.service.IDataReportService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备上传数据
 * @Author: jeecg-boot
 * @Date: 2020-05-14
 * @Version: V1.0
 */
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

    @Override
    public JSONObject verifyBuildData(String deviceModelId, Map<String, Object> dataMap) {

        JSONObject json = new JSONObject();
        LambdaQueryWrapper<DeviceData> query = new LambdaQueryWrapper<DeviceData>();
        query.eq(DeviceData::getDeviceModelBy, deviceModelId);
        List<DeviceData> deviceDataList = deviceDataMapper.selectList(query);

        for (DeviceData deviceData : deviceDataList) {
            json.put(deviceData.getCode(), dataMap.get(deviceData.getCode()));
        }
        return json;
    }
}
