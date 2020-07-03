package org.jeecg.modules.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: jeecg-boot-module-iot
 * @description: 上级设备实例下拉树
 * @author: zhouwr
 * @create: 2020-06-24 11:07
 * @version：1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentInstanceTreeSelect {

    private String key;
    private String id;
    private String pId;
    private String rootPId = null;
    private String title;
    private String name;
    private String type;
    private String typeText;
}
