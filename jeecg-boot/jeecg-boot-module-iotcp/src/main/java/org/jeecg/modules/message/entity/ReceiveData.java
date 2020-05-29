package org.jeecg.modules.message.entity;

import lombok.*;

import java.util.Map;

/**
 * @program: jeecg-boot-module-iot
 * @description: 网络入栈数据类
 * @author: zhouwr
 * @create: 2020-05-26 15:09
 * @version：1.0
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReceiveData {

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

    /**
     * 设备数据节点
     */
    Map<String, Object> dataMap;

}
