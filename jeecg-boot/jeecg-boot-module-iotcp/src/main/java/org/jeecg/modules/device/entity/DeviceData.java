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
 * @Description: 数据节点
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@ApiModel(value="device_model对象", description="设备模型")
@Data
@TableName("iot_device_data")
public class DeviceData implements Serializable {
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

	/**属性类型*/
	@Excel(name = "属性", width = 15)
	@ApiModelProperty(value = "属性")
	private java.lang.String attrType;

	/**读写权限*/
	@Excel(name = "读写权限", width = 15)
	@ApiModelProperty(value = "读写权限")
	@Dict(dicCode = "dm_rw_author")
	private java.lang.String rwAuthor;

	/**数据模型*/
	@Excel(name = "数据", width = 150)
	@ApiModelProperty(value = "数据")
	private java.lang.String valueType;

	/**描述*/
	@Excel(name = "描述", width = 150)
	@ApiModelProperty(value = "描述")
	private java.lang.String description;

}
