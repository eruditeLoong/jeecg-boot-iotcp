package org.jeecg.modules.device.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceType {
    DEVICE("设备", "device"),
    GATEWAY("网关", "gateway"),
    CONCENTRATOR("集中器", "concentrator"),
    OTHER("其它", "other");

    private String name;
    private String code;

}



