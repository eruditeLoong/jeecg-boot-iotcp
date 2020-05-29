package org.jeecg.modules.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.device.entity.DeviceData;
import org.jeecg.modules.device.entity.DeviceEvent;
import org.jeecg.modules.device.entity.DeviceFunction;
import org.jeecg.modules.device.entity.DeviceLabel;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 设备模型
 * @Author: jeecg-boot
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Data
@ApiModel(value="device_modelPage对象", description="设备模型")
public class DeviceModelPage {
	
	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**编码*/
	@Excel(name = "编码", width = 15)
	@ApiModelProperty(value = "编码")
	private java.lang.String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**类型*/
	@Excel(name = "类型", width = 15)
	@ApiModelProperty(value = "类型")
	private java.lang.String type;
	/**模型文件*/
	@Excel(name = "模型文件", width = 15)
	@ApiModelProperty(value = "模型文件")
	private java.lang.String threeModelFile;
	/**传输协议*/
	@Excel(name = "传输协议", width = 15)
	@ApiModelProperty(value = "传输协议")
	private java.lang.String linkProtocolBy;
	/**数据协议*/
	@Excel(name = "数据协议", width = 15)
	@ApiModelProperty(value = "数据协议")
	private java.lang.String dataProtocolBy;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	@Dict(dicCode = "act_state")
	private java.lang.Integer state;
	/**描述*/
	@Excel(name = "描述", width = 15)
	@ApiModelProperty(value = "描述")
	private java.lang.String description;
	@ExcelCollection(name = "数据节点")
	@ApiModelProperty(value = "数据节点")
	private List<DeviceData> deviceDataList;
	@ExcelCollection(name = "功能定义")
	@ApiModelProperty(value = "功能定义")
	private List<DeviceFunction> deviceFunctionList;
	@ExcelCollection(name = "事件定义")
	@ApiModelProperty(value = "事件定义")
	private List<DeviceEvent> deviceEventList;
	@ExcelCollection(name="数据标签")
	@ApiModelProperty(value = "数据标签")
	private List<DeviceLabel> deviceLabelList;
	
}
