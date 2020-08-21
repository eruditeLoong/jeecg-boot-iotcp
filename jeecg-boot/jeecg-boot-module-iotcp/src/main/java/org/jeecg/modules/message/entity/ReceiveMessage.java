package org.jeecg.modules.message.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: jeecg-boot-module-iot
 * @description: 网络入栈数据类
 * @author: zhouwr
 * @create: 2020-05-26 15:09
 * @version：1.0
 **/
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReceiveMessage {

    /**
     * 数据节点
     */
    Map<String, Object> dataMap = new HashMap<>();
    /**
     * 消息类型
     */
    private String type;
    /**
     * 设备实例
     */
    private String deviceInstanceId;
    /**
     * 接收时间
     */
    private long datetime;

    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.put("type", this.type);
        json.put("deviceInstanceId", this.deviceInstanceId);
        json.put("datetime", this.datetime);

        JSONObject dataMapJson = new JSONObject();
        dataMapJson.putAll(this.dataMap == null ? new HashMap<>() : this.dataMap);
        json.put("dataMap", dataMapJson);
        return json.toJSONString();
    }

}
