package org.jeecg.modules.network.entity;

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
import java.util.Date;

/**
 * @Description: 网络服务管理
 * @Author: jeecg-boot
 * @Date:   2020-04-12
 * @Version: V1.0
 */
@Data
@TableName("iot_network_service")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="iot_network_service对象", description="网络服务管理")
public class NetworkService implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
    /**服务类型*/
    @Excel(name = "服务类型", width = 15)
    @ApiModelProperty(value = "服务类型")
    @Dict(dicCode = "ns_type")
    private String type;
    /**服务图标*/
    @Excel(name = "服务图标", width = 15)
    @ApiModelProperty(value = "服务图标")
    private String icon;
	/**HOST*/
	@Excel(name = "HOST", width = 15)
    @ApiModelProperty(value = "HOST")
    private String host;
    /**
     * 端口号
     */
    @Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private Integer port;
    /**
     * 线程数
     */
    @Excel(name = "线程数", width = 15)
    @ApiModelProperty(value = "线程数")
    private Integer threadNum;
    // = Runtime.getRuntime().availableProcessors();
    /**
     * 是否同步处理
     */
    @Excel(name = "是否同步处理", width = 15)
    @ApiModelProperty(value = "是否同步处理")
    private Integer isSync;
    /**
     * 解析方式
     */
    @Excel(name = "解析方式", width = 15)
    @ApiModelProperty(value = "解析方式")
    private String resolMethod;
    /**
     * mqtt客户端id
     */
    @Excel(name = "mqtt客户端id", width = 15)
    @ApiModelProperty(value = "mqtt客户端id")
    private String clientId;
    /**用户名*/
    @Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private String username;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private String password;
	/**订阅主题*/
	@Excel(name = "订阅主题", width = 15)
    @ApiModelProperty(value = "订阅主题")
    private String topics;
    /**启停状态*/
    @Excel(name = "启停状态", width = 15)
    @ApiModelProperty(value = "启停状态")
    private Boolean state;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;

}
