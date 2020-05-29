package org.jeecg.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 设备上传数据
 * @Author: jeecg-boot
 * @Date: 2020-05-14
 * @Version: V1.0
 */
@Data
@TableName("iot_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "iot_data对象", description = "设备上传数据")
public class DataReport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 实例设备
     */
    @Excel(name = "实例设备", width = 15, dictTable = "iot_device_instance", dicText = "name", dicCode = "id")
    @Dict(dictTable = "iot_device_instance", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "实例设备")
    private java.lang.String instanceDeviceBy;
    /**
     * 上报数据
     */
    @Excel(name = "上报数据", width = 15)
    @ApiModelProperty(value = "上报数据")
    private java.lang.String data;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 录入方式
     */
    @Excel(name = "录入方式", width = 15, dicCode = "dm_type")
    @Dict(dicCode = "dm_type")
    @ApiModelProperty(value = "录入方式")
    private java.lang.String inputMode;
}
