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
 * @Description: 数据标签
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@ApiModel(value="device_model对象", description="设备模型")
@Data
@TableName("iot_device_label")
public class DeviceLabel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**设备模型*/
	@ApiModelProperty(value = "设备模型")
	private java.lang.String deviceModelBy;
	/**标识*/
	@Excel(name = "标识", width = 15)
	@ApiModelProperty(value = "标识")
	private java.lang.String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**数据节点*/
	@Excel(name = "数据节点", width = 15, dictTable = "device_data", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "数据节点")
	private java.lang.String deviceDataBy;
	/**条件规则*/
	@Excel(name = "条件规则", width = 15)
	@ApiModelProperty(value = "条件规则")
	private java.lang.String rules;
	/**规则数值*/
	@Excel(name = "规则数值", width = 15)
	@ApiModelProperty(value = "规则数值")
	private java.lang.String rulesValue;
	/**颜色标识*/
	@Excel(name = "颜色标识", width = 15)
	@ApiModelProperty(value = "颜色标识")
	private java.lang.String color;
	/**描述*/
	@Excel(name = "描述", width = 15)
	@ApiModelProperty(value = "描述")
	private java.lang.String description;
}
