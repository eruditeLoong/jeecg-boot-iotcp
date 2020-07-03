package org.jeecg.modules.device.vo;

import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.device.entity.*;
import org.jeecg.modules.message.entity.DataReport;

import java.util.List;

@Getter
@Setter
public class DeviceInstanceDetail {

    public DeviceModel deviceModel;

    public DeviceInstance deviceInstance;

    public List<DeviceData> deviceDataList;

    public List<DeviceFunction> deviceFunctionList;

    public List<DeviceEvent> deviceEventList;

    public List<DataReport> dataReportList;

    @Override
    public String toString() {
        return "DeviceInstanceDetail{" +
                "deviceModel=" + deviceModel +
                ", deviceInstance=" + deviceInstance +
                ", deviceDataList=" + deviceDataList +
                ", deviceFunctionList=" + deviceFunctionList +
                ", deviceEventList=" + deviceEventList +
                ", dataReportList=" + dataReportList +
                '}';
    }
}
