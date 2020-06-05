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
     * 设备数据节点
     */
    Map<String, Object> dataMap = new HashMap<>();
    /**
     * 消息类型
     */
    private String type;
    /**
     * 设备模型
     */
    private String deviceModelId;
    /**
     * 设备实例
     */
    private String deviceInstanceId;
    /**
     * 接收时间
     */
    private long datetime;

    public ReceiveMessage(String jsonString) {

        try {
            JSONObject json = new JSONObject();
            json = JSONObject.parseObject(jsonString);

            this.type = json.getString("type");
            this.deviceModelId = json.getString("deviceModelId");
            this.deviceInstanceId = json.getString("deviceInstanceId");
            this.datetime = json.getLongValue("datetime");

            JSONObject dataObj = json.getJSONObject("dataMap");

            Iterator<Map.Entry<String, Object>> iterator = dataObj.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                this.dataMap.put(entry.getKey().toString(), entry.getValue());
            }
        } catch (Exception e) {
            log.error("接收的数据无法完成转换，请检查数据格式，或数据解析逻辑！{}", e.getMessage());
            return;
        }
    }

    public ReceiveMessage(String type, String deviceModelId, String deviceInstanceId, long datetime, Map<String, Object> dataMap) {
        this.type = type;
        this.deviceModelId = deviceModelId;
        this.deviceInstanceId = deviceInstanceId;
        this.datetime = datetime;
        this.dataMap = dataMap;
    }

    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.put("type", this.type);
        json.put("deviceModelId", this.deviceModelId);
        json.put("deviceInstanceId", this.deviceInstanceId);
        json.put("datetime", this.datetime);

        JSONObject dataMapJson = new JSONObject();
        dataMapJson.putAll(this.dataMap == null ? new HashMap<>() : this.dataMap);
        json.put("dataMap", dataMapJson);
        return json.toJSONString();
    }
}
