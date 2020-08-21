package org.jeecg.modules.protocol.entity;

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
 * @Description: 数据解析协议
 * @Author: 周文荣
 * @Date:   2020-04-19
 * @Version: V1.0
 */
@Data
@TableName("iot_protocol_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="iot_protocol_data对象", description="数据解析协议")
public class ProtocolData implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**协议标识*/
	@Excel(name = "协议标识", width = 15)
    @ApiModelProperty(value = "协议标识")
    private java.lang.String code;
    /**
     * 协议名称
     */
    @Excel(name = "协议名称", width = 15)
    @ApiModelProperty(value = "协议名称")
    private java.lang.String name;
    /**
     * 解析类型
     */
    @Excel(name = "解析类型", width = 15, dicCode = "pd_type")
    @Dict(dicCode = "pd_type")
    @ApiModelProperty(value = "解析类型")
    private java.lang.String type;
    /**
     * 解析包路径
     */
    @Excel(name = "解析包路径", width = 15)
    @ApiModelProperty(value = "解析包路径")
    private java.lang.String packagePath;
    /**
     * 类名
     */
    @Excel(name = "类名", width = 15)
    @ApiModelProperty(value = "类名")
    private java.lang.String classPath;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "act_state")
    private java.lang.Integer state;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
}
