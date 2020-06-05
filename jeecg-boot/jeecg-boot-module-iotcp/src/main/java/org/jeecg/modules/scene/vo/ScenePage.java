package org.jeecg.modules.scene.vo;

import java.util.List;

import org.jeecg.modules.scene.entity.SceneScheme;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 场景管理
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Data
@ApiModel(value="iot_scenePage对象", description="场景管理")
public class ScenePage {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**标识*/
	@Excel(name = "标识", width = 15)
	@ApiModelProperty(value = "标识")
	private java.lang.String code;
	/**3D模型文件*/
	@Excel(name = "3D模型文件", width = 15)
	@ApiModelProperty(value = "3D模型文件")
	private java.lang.String modelFiles;
	/**模型比例*/
	@Excel(name = "模型比例", width = 15)
	@ApiModelProperty(value = "模型比例")
	private java.lang.String modelScale;
	/**软件缩放*/
	@Excel(name = "软件缩放", width = 15)
	@ApiModelProperty(value = "软件缩放")
	private java.lang.String softwareZoom;
	/**地理位置*/
	@Excel(name = "地理位置", width = 15)
	@ApiModelProperty(value = "地理位置")
	private java.lang.String address;
	/**经纬度*/
	@Excel(name = "经纬度", width = 15)
	@ApiModelProperty(value = "经纬度")
	private java.lang.String latLon;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**所属机构*/
	@ApiModelProperty(value = "所属机构")
	private java.lang.String sysOrgCode;
	/**描述*/
	@Excel(name = "描述", width = 15)
	@ApiModelProperty(value = "描述")
	private java.lang.String description;
	
	@ExcelCollection(name="场景方案")
	@ApiModelProperty(value = "场景方案")
	private List<SceneScheme> sceneSchemeList;
	
}
