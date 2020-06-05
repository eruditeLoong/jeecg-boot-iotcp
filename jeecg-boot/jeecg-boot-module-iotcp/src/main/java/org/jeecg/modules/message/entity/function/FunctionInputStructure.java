package org.jeecg.modules.message.entity.function;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.MoreObjects;
import lombok.*;

import java.util.HashMap;

/**
 * @program: jeecg-boot-module-iot
 * @description: 功能输入参数数据结构
 * @author: zhouwr
 * @create: 2020-06-03 15:57
 * @version：1.0
 *
 * @dataStructure：
 * {
 *    "code": "requestCmd",
 *    "dataId": "1253970109857300481",
 *    "inputMode": "1",
 *    "valueType": {
 *        "arrayType": "byte",
 *        "arraySize": 8,
 *        "type": "array"
 *    },
 *    "name": "请求命令",
 *    "id": "1253970109857300481",
 *    "deviceModelBy": "1252595618989654018",
 *    "rwAuthor": "w",
 *    "value": "01 03 00 00 00 7A C4 29 "
 * }
 **/

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FunctionInputStructure {
    private String id;
    private String code;
    private String name;
    private String inputMode;
    private JSONObject valueType;
    private String deviceModelId;
    private String rwAuthor;
    private Object value;

    public String toJsonString(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("code", code);
        json.put("inputMode", inputMode);
        json.put("valueType", valueType.toJSONString());
        json.put("deviceModelId", deviceModelId);
        json.put("rwAuthor", rwAuthor);
        json.put("value", value);
        return json.toJSONString();
    }
}
