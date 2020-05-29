package org.jeecg.modules.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 事件定义
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@ApiModel(value = "device_model对象", description = "设备模型")
@Data
@TableName("iot_device_event")
public class DeviceEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 设备模型
     */
    @ApiModelProperty(value = "设备模型")
    private java.lang.String deviceModelBy;
    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private java.lang.String code;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 事件类型
     */
    @Excel(name = "事件类型", width = 15)
    @ApiModelProperty(value = "事件类型")
    private java.lang.String type;
    /**
     * 事件级别
     */
    @Excel(name = "事件级别", width = 15)
    @ApiModelProperty(value = "事件级别")
    private java.lang.Integer level;

    /**
     * 事件通知
     */
    @Excel(name = "事件通知", width = 15)
    @ApiModelProperty(value = "事件通知")
    private java.lang.String notifyType;


    /**
     * 数据节点
     */
    @Excel(name = "数据节点", width = 15, dictTable = "device_data", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "数据节点")
    private java.lang.String dataBy;

    /**
     * 规则条件
     */
    @Excel(name = "数据规则", width = 15)
    @ApiModelProperty(value = "数据规则")
    private java.lang.String dataRule;

    /**
     * 规则值
     */
    @Excel(name = "规则值", width = 35)
    @ApiModelProperty(value = "规则值")
    private java.lang.String ruleValue;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
}
