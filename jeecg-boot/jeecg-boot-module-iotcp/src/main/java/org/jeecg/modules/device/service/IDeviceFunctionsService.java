package org.jeecg.modules.device.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.device.entity.DeviceFunction;
import org.jeecg.modules.device.entity.InputData;
import org.jeecg.modules.device.entity.InputParam;
import org.jeecg.modules.device.vo.DeviceFunctionExec;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @Description: 功能定义
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
public interface IDeviceFunctionsService extends IService<DeviceFunction> {

	public List<DeviceFunction> selectByMainId(String mainId);

	/**
	 * 获取设备功能定义的输入参数，根据功能定义id
	 * @param funcId
	 * @return
	 */
	public List<InputParam> listInputParamByFuncId(String funcId);

	/**
	 * 获取设备功能定义的输入参数，根据功能定义id, 和输入方式
	 * @param funcId
	 * @param inputMode
	 * @return
	 */
	public List<InputData> listInputDataByFuncId(String funcId, String inputMode);

	/**
	 * 获取设备功能定义的输入参数数据，根据功能定义id
	 * @param funcId
	 * @return
	 */
	public List<InputData> listInputDataByFuncId(String funcId);

	/**
	 * 获取所有功能定义的输入参数
	 * @return
	 */
	public List<InputParam> listInputParam();

	/**
	 * 获取所有功能定义的输入参数数据
	 * @return
	 */
	public List<InputData> listInputData();

	/**
	 * 输入参数重复校验
	 *
	 * @param deviceFunc
	 * @return
	 */
	public Boolean inputParamsCheckUnique(DeviceFunction deviceFunc);

	/**
	 * 获得具有参数值的所有功能列表
	 *
	 * @param deviceModelId
	 * @param deviceInstanceId
	 * @return
	 */
    public List<DeviceFunction> getFuncWithParamValue(String deviceModelId, String deviceInstanceId, String deviceInstanceExtendParams);

    /**
     * 构建器函数数据结构
     *
     * @param deviceModelId
     * @param deviceInstanceId
     * @param deviceFuncCode
     * @return
     */
    public JSONObject buildFuncDataStructure(DeviceFunctionExec functionExec);

    /**
     * 异步执行功能
     *
     * @param deviceModelId
     * @param deviceInstanceId
     * @param deviceFuncCode
     * @return
     */
    public void execFunctionAsyn(DeviceFunctionExec functionExec);

    /**
     * 同步执行功能
     *
     * @param deviceModelId
     * @param deviceInstanceId
     * @param deviceFuncCode
     * @return
     */
    public Object execFunctionSync(DeviceFunctionExec functionExec);

}
