package org.jeecg.modules.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.utils.TreeNode;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 设备实例
 * @Author: jeecg-boot
 * @Date: 2020-04-11
 * @Version: V1.0
 */
@Slf4j
@Data
public class DeviceInstanceTree extends TreeNode<DeviceInstanceTree> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;
    private String value;
    private String title;
    private String key;
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
     * 设备模型
     */
    @Excel(name = "设备模型", width = 15, dictTable = "iot_device_model", dicText = "name", dicCode = "id")
    @Dict(dictTable = "iot_device_model", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "设备模型")
    private String modelBy;
    /**
     * 所属机构
     */
    @Excel(name = "所属机构", width = 15, dictTable = "iot_org", dicText = "name", dicCode = "id")
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
    private java.util.Date createTime;
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
    private java.util.Date updateTime;
    /**
     * 说明
     */
    @Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String description;

    private List<DeviceInstanceTree> children;

    @Excel(name = "设备类型", width = 15)
    private String deviceType;

    @Excel(name = "设备类型", width = 15)
    private String deviceTypeText;

    private String funcExecConf;

    public DeviceInstanceTree(DeviceInstance deviceInstance) {
        this.key = deviceInstance.getId();
        this.id = deviceInstance.getId();
        this.value = deviceInstance.getId();
        this.parentBy = deviceInstance.getParentBy();
        this.code = deviceInstance.getCode();
        this.name = deviceInstance.getName();
        this.sceneBy = deviceInstance.getSceneBy();
        this.sceneSchemeBy = deviceInstance.getSceneSchemeBy();
        this.title = deviceInstance.getName();
        this.modelBy = deviceInstance.getModelBy();
        this.sysOrgCode = deviceInstance.getSysOrgCode();
        this.status = deviceInstance.getStatus();
        this.extendParams = deviceInstance.getExtendParams();
        this.createBy = deviceInstance.getCreateBy();
        this.createTime = deviceInstance.getCreateTime();
        this.updateBy = deviceInstance.getUpdateBy();
        this.updateTime = deviceInstance.getUpdateTime();
        this.description = deviceInstance.getDescription();
    }

    @Override
    public String getParentId() {
        return this.parentBy;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentBy = parentId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<DeviceInstanceTree> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<DeviceInstanceTree> children) {
        this.children = children;
    }
}
