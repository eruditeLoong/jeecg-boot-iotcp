package org.jeecg.modules.device.enums;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceLogType{
    event("事件上报"),
    readProperty("属性读取"),
    writeProperty("属性修改"),
    reportProperty("属性上报"),
    child("子设备消息"),
    childReply("子设备消息回复"),
    functionInvoke("调用功能"),
    readPropertyReply("读取属性回复"),
    writePropertyReply("修改属性回复"),
    functionReply("调用功能回复"),
    register("设备注册"),
    unregister("设备注销"),

    offline("离线"),
    online("上线"),
    other("其它");

    @JSONField(serialize = false)
    private String text;
}
