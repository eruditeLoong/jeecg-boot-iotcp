package org.jeecg.modules.message.entity;

import lombok.*;
import org.jeecg.modules.device.entity.DeviceData;

import java.util.List;

/**
 * @program: jeecg-boot-module-iot
 * @description: 网络发送数据
 * @author: zhouwr
 * @create: 2020-05-26 16:34
 * @version：1.0
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendData {
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
    private List<DeviceData> deviceDataList;
}
