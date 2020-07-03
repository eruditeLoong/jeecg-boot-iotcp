package org.jeecg.modules.scene.entity;

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
 * @Description: 场景方案
 * @Author: jeecg-boot
 * @Date: 2020-06-05
 * @Version: V1.0
 */
@Data
@TableName("iot_scene_scheme")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "iot_scene_scheme对象", description = "场景方案")
public class SceneScheme implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 所属场景
     */
    @Excel(name = "所属场景", width = 15)
    @ApiModelProperty(value = "所属场景")
    private java.lang.String sceneBy;
    /**
     * 方案名称
     */
    @Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
    private java.lang.String name;

    /**
     * 方案配置
     */
    @Excel(name = "方案配置", width = 15)
    @ApiModelProperty(value = "方案配置")
    private java.lang.String config;
    /**
     * 方案状态
     */
    @Excel(name = "方案状态", width = 15, dicCode = "valid_status")
    @Dict(dicCode = "valid_status")
    @ApiModelProperty(value = "方案状态")
    private java.lang.Boolean status;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String description;
}
