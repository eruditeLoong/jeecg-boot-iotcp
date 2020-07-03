package org.jeecg.modules.scene.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 场景管理
 * @Author: jeecg-boot
 * @Date: 2020-06-04
 * @Version: V1.0
 */
@ApiModel(value = "iot_scene对象", description = "场景管理")
@Data
@TableName("iot_scene")
public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private java.lang.String code;
    /**
     * 3D模型文件
     */
    @Excel(name = "3D模型文件", width = 15)
    @ApiModelProperty(value = "3D模型文件")
    private java.lang.String modelFiles;
    /**
     * 模型比例
     */
    @Excel(name = "模型比例", width = 15)
    @ApiModelProperty(value = "模型比例")
    private java.lang.String modelScale;
    /**
     * 软件缩放
     */
    @Excel(name = "软件缩放", width = 15)
    @ApiModelProperty(value = "软件缩放")
    private java.lang.String softwareZoom;
    /**
     * 地理位置
     */
    @Excel(name = "地理位置", width = 15)
    @ApiModelProperty(value = "地理位置")
    private java.lang.String address;
    /**
     * 经纬度
     */
    @Excel(name = "经纬度", width = 15)
    @ApiModelProperty(value = "经纬度")
    private java.lang.String latLon;
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
     * 所属机构
     */
    @Excel(name = "所属机构", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "所属机构")
    private java.lang.String sysOrgCode;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
}
