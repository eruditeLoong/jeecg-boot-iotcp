package org.jeecg.modules.device.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.entity.DeviceInstance;

@Getter
@Setter
@ToString
public class DeviceFuncExecConf {

    /**
     * corn表达式
     */
    String corn;

    /**
     * 执行模式：
     * 1、debug：调试模式，输入参数可修改，可同步|异步，只执行1次；
     * 2、task：任务模式，输入参数不可修改，只能异步，
     */
    String execMode;

    /**
     * 执行状态
     */
    boolean isRunning;

    public DeviceFuncExecConf(){
        super();
    }

    public DeviceFuncExecConf(String corn, String execMode, boolean isRunning) {
        this.corn = corn;
        this.execMode = execMode;
        this.isRunning = isRunning;
    }

    /**
     * 从设备实例的所有功能执行配置中，根据功能编码，构造设备特定功能配置
     * @param deviceFuncExecConf
     * @param deviceFuncCode
     */
    public DeviceFuncExecConf(String deviceFuncExecConf, String deviceFuncCode){
        super();
        JSONObject json = JSONObject.parseObject(
                oConvertUtils.isEmpty(deviceFuncExecConf) ? "{}" : deviceFuncExecConf
        ).getJSONObject(deviceFuncCode);

        if(json == null)
            json = new JSONObject();

        String execMode = oConvertUtils.isEmpty(json.getString("execMode")) ? "task" : json.getString("execMode");
        String corn = oConvertUtils.isEmpty(json.getString("corn")) ? "* * * * ? *" : json.getString("corn");
        Boolean isRunning = json.getBoolean("running") == null ? false : json.getBoolean("running");

        this.isRunning = isRunning;
        this.execMode = execMode;
        this.corn = corn;
    }
}
