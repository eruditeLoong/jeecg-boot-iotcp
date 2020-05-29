package org.jeecg.modules.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 功能定义
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@ApiModel(value = "device_model对象", description = "设备模型")
@Data
@TableName("iot_device_functions")
public class DeviceFunction implements Serializable {
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
     * 功能名称
     */
    @Excel(name = "功能名称", width = 20)
    @ApiModelProperty(value = "功能名称")
    private java.lang.String name;
    /**
     * 功能类型
     */
    @Excel(name = "功能类型", width = 20)
    @ApiModelProperty(value = "功能类型")
    @Dict(dicCode = "dm_func_type")
    private java.lang.String type;
    /**
     * 是否同步
     */
    @Excel(name = "是否同步", width = 15)
    @ApiModelProperty(value = "是否同步")
    @Dict(dicCode = "dm_is_sync")
    private java.lang.Boolean isSync;
    /**
     * 输入参数
     */
    @Excel(name = "输入参数", width = 150)
    @ApiModelProperty(value = "输入参数")
    private java.lang.String inputParams;
    /**
     * 输出数据
     */
    @Excel(name = "输出数据", width = 150)
    @ApiModelProperty(value = "输出数据")
    private java.lang.String outputData;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 150)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
}
