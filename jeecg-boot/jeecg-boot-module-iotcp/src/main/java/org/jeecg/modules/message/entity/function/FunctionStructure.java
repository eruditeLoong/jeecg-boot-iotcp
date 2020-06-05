package org.jeecg.modules.message.entity.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: jeecg-boot-module-iot
 * @description: 功能执行数据结构
 * @author: zhouwr
 * @create: 2020-06-03 15:52
 * @version：1.0
 *
 * @dataStructure:
 * {
 *     "deviceInstanceId": "concentrator-1",
 *     "function": {
 *         "output": "",
 *         "code": "requestData",
 *         "inputs": [
 *             {
 *                 "code": "requestCmd",
 *                 "dataId": "1253970109857300481",
 *                 "inputMode": "1",
 *                 "valueType": {
 *                     "arrayType": "byte",
 *                     "arraySize": 8,
 *                     "type": "array"
 *                 },
 *                 "name": "请求命令",
 *                 "id": "1253970109857300481",
 *                 "deviceModelBy": "1252595618989654018",
 *                 "rwAuthor": "w",
 *                 "value": "01 03 00 00 00 7A C4 29 "
 *             }
 *         ],
 *         "name": "请求数据",
 *         "id": "1253971247969107970"
 *     },
 *     "deviceModelId": "1252595618989654018"
 * }
 **/

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FunctionStructure {
    private String id;
    private String name;
    private String code;
    private List<FunctionInputStructure> inputs;
    private String output;

    public String toJsonString(){
        JSONArray inputArray = new JSONArray();
        inputArray.addAll(inputs);

        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("code", code);
        json.put("inputs", inputArray.toJSONString());
        json.put("output", output);
        return json.toJSONString();
    }

}
