package org.jeecg.modules.device.entity;

import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.vo.DeviceFuncExecConf;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备实例
 * @Author: jeecg-boot
 * @Date: 2020-04-11
 * @Version: V1.0
 */
@Data
@TableName("iot_device_instance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "iot_device_instance对象", description = "设备实例")
public class DeviceInstance implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 上级实例
     */
    @Excel(name = "上级实例", width = 15, dictTable = "iot_device_instance", dicText = "name", dicCode = "id")
    @Dict(dictTable = "iot_device_instance", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "上级实例")
    private String parentBy;
    /**
     * 实例标识
     */
    @Excel(name = "实例标识", width = 15)
    @ApiModelProperty(value = "实例标识")
    private String code;
    /**
     * 实例名称
     */
    @Excel(name = "实例名称", width = 15)
    @ApiModelProperty(value = "实例名称")
    private String name;
    /**
     * 设备模型
     */
    @Excel(name = "设备模型", width = 15, dictTable = "iot_device_model", dicText = "name", dicCode = "id")
    @Dict(dictTable = "iot_device_model", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "设备模型")
    private String modelBy;
    /**
     * 场景
     */
    @Excel(name = "场景", width = 15)
    @ApiModelProperty(value = "场景")
    private String sceneBy;
    /**
     * 场景方案
     */
    @Excel(name = "场景方案", width = 15)
    @ApiModelProperty(value = "场景方案")
    private String sceneSchemeBy;
    /**
     * 所属机构
     */
    @Excel(name = "所属机构", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属机构")
    private String sysOrgCode;
    /**
     * 设备实例状态
     */
    @Excel(name = "实例状态", width = 15, dictTable = "iot_org", dicText = "name", dicCode = "id")
    @Dict(dicCode = "di_state")
    @ApiModelProperty(value = "实例状态")
    private String status;

    @Excel(name = "扩展参数", width = 30)
    @ApiModelProperty(value = "扩展参数")
    private String extendParams;

    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String description;

    private String funcExecConf;
}
