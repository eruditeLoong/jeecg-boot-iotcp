package org.jeecg.modules.device.controller;import com.alibaba.fastjson.JSON;import com.alibaba.fastjson.JSONArray;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.baomidou.mybatisplus.core.metadata.IPage;import com.baomidou.mybatisplus.extension.plugins.pagination.Page;import io.swagger.annotations.Api;import io.swagger.annotations.ApiOperation;import lombok.extern.slf4j.Slf4j;import org.jeecg.common.api.vo.Result;import org.jeecg.common.aspect.annotation.AutoLog;import org.jeecg.common.constant.CacheConstant;import org.jeecg.common.system.api.ISysBaseAPI;import org.jeecg.common.system.base.controller.JeecgController;import org.jeecg.common.system.query.QueryGenerator;import org.jeecg.common.util.DateUtils;import org.jeecg.common.util.oConvertUtils;import org.jeecg.modules.device.entity.*;import org.jeecg.modules.device.enums.FuncExecMode;import org.jeecg.modules.device.quartzJob.ExecInstanceFunc;import org.jeecg.modules.device.service.*;import org.jeecg.modules.device.vo.DeviceFuncExecConf;import org.jeecg.modules.device.vo.DeviceFunctionExec;import org.jeecg.modules.device.vo.DeviceInstanceDetail;import org.jeecg.modules.device.vo.DeviceInstanceTree;import org.jeecg.modules.message.entity.DataReport;import org.jeecg.modules.message.service.IDataReportService;import org.jeecg.modules.network.network.NetworkManager;import org.jeecg.modules.network.service.INetworkServiceService;import org.jeecg.utils.TreeUtil;import org.quartz.*;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.redis.core.RedisTemplate;import org.springframework.util.StringUtils;import org.springframework.web.bind.annotation.*;import org.springframework.web.servlet.ModelAndView;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import java.util.ArrayList;import java.util.Arrays;import java.util.List;import java.util.Set;import java.util.stream.Collectors;import java.util.stream.Stream;/** * @Description: 设备实例 * @Author: jeecg-boot * @Date: 2020-04-11 * @Version: V1.0 */@Api(tags = "设备实例")@RestController@RequestMapping("/device/deviceInstance")@Slf4jpublic class DeviceInstanceController extends JeecgController<DeviceInstance, IDeviceInstanceService> {    @Autowired    private IDeviceInstanceService deviceInstanceService;    @Autowired    private IDeviceModelService deviceModelService;    @Autowired    private IDeviceDataService deviceDataService;    @Autowired    private IDeviceFunctionsService deviceFunctionsService;    @Autowired    private IDeviceEventService deviceEventService;    @Autowired    private IDeviceLabelService deviceLabelService;    @Autowired    private IDataReportService dataReportService;    @Autowired    private INetworkServiceService networkServiceService;    @Autowired    private NetworkManager networkManager;    @Autowired    private ISysBaseAPI sysBaseAPI;    @Autowired    private Scheduler scheduler;    @Autowired    private RedisTemplate redisTemplate;    /**     * 分页列表查询     *     * @param deviceInstance     * @param pageNo     * @param pageSize     * @param req     * @return     */    @AutoLog(value = "设备实例-分页列表查询")    @ApiOperation(value = "设备实例-分页列表查询", notes = "设备实例-分页列表查询")    @GetMapping(value = "/list")    public Result<?> queryPageList(DeviceInstance deviceInstance,                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,                                   HttpServletRequest req) {        QueryWrapper<DeviceInstance> queryWrapper = QueryGenerator.initQueryWrapper(deviceInstance, req.getParameterMap());        Page<DeviceInstance> page = new Page<DeviceInstance>(pageNo, pageSize);        IPage<DeviceInstance> pageList = deviceInstanceService.page(page, queryWrapper);        return Result.ok(pageList);    }    @RequestMapping(value = "/listTree", method = RequestMethod.GET)    public Result<List<DeviceInstanceTree>> listTree(DeviceInstance deviceInstance, HttpServletRequest req) {        Result<List<DeviceInstanceTree>> result = new Result<>();        try {            // LambdaQueryWrapper<DeviceInstance> query = new LambdaQueryWrapper<DeviceInstance>();            // query            //         .eq(DeviceInstance::getStatus, deviceInstance.getStatus())            //         .eq(DeviceInstance::getModelBy, deviceInstance.getModelBy())            //         .eq(DeviceInstance::getName, deviceInstance.getName())            //         .eq(DeviceInstance::getSceneBy, deviceInstance.getSceneBy())            //         .eq(DeviceInstance::getSceneSchemeBy, deviceInstance.getSceneSchemeBy())            //         .eq(DeviceInstance::getSysOrgCode, deviceInstance.getSysOrgCode())            //         .orderByAsc(DeviceInstance::getCode);            // List<DeviceInstance> list = deviceInstanceService.list(query);            QueryWrapper<DeviceInstance> queryWrapper = QueryGenerator.initQueryWrapper(deviceInstance, req.getParameterMap());            List<DeviceInstance> list = deviceInstanceService.list(queryWrapper);            List<DeviceInstanceTree> treeList = list                    .stream()                    .map(instance -> {                        DeviceInstanceTree deviceInstanceTree = new DeviceInstanceTree(instance);                        String type = deviceModelService.getById(instance.getModelBy()).getType();                        String typeText = sysBaseAPI.queryDictTextByKey("dm_type", type);                        deviceInstanceTree.setDeviceTypeText(typeText);                        deviceInstanceTree.setDeviceType(type);                        return deviceInstanceTree;                    })                    .collect(Collectors.toList());            List<DeviceInstanceTree> trees = TreeUtil.listToTree(treeList);            result.setResult(trees);            result.setSuccess(true);        } catch (Exception e) {            log.error(e.getMessage(), e);        }        return result;    }    @AutoLog(value = "设备实例-获取输入参数")    @ApiOperation(value = "设备实例-获取输入参数", notes = "设备实例-获取设备模型的输入参数")    @GetMapping(value = "/listInputParams")    public Result<?> listInputParams(@RequestParam(name = "deviceModelId", required = true) String deviceModelId, HttpServletRequest req) {        // 1、根据deviceModelId 获取设备模型的功能定义        List<DeviceFunction> deviceFuncList = deviceFunctionsService.lambdaQuery()                .eq(DeviceFunction::getDeviceModelBy, deviceModelId)                .list();        JSONArray funcArr = new JSONArray();        for (DeviceFunction deviceFunc : deviceFuncList) {            List<InputData> inputDataList = deviceFunctionsService.listInputDataByFuncId(deviceFunc.getId());            if (inputDataList != null && inputDataList.size() > 0) {                JSONObject funcJson = new JSONObject();                funcJson.put("code", deviceFunc.getCode());                funcJson.put("name", deviceFunc.getName());                JSONArray inputParamArr = new JSONArray();                for (InputData inputData : inputDataList) {                    JSONObject deviceDataJson = new JSONObject();                    deviceDataJson.put("id", inputData.getDeviceData().getId());                    deviceDataJson.put("code", inputData.getDeviceData().getCode());                    deviceDataJson.put("name", inputData.getDeviceData().getName());                    deviceDataJson.put("rwAuthor", inputData.getDeviceData().getRwAuthor());                    deviceDataJson.put("valueType", JSON.parseObject(inputData.getDeviceData().getValueType()));                    deviceDataJson.put("description", inputData.getDeviceData().getDescription());                    inputParamArr.add(deviceDataJson);                }                funcJson.put("inputParams", inputParamArr);                funcArr.add(funcJson);            }        }        System.out.println(funcArr.toString());        return Result.ok(funcArr);    }    @AutoLog(value = "设备实例-获取所有网关实例设备")    @ApiOperation(value = "设备实例-获取所有网关实例设备数", notes = "设备实例-获取所有网关实例设备")    @GetMapping(value = "/listGatewayDevice")    public Result<?> listGatewayDevice(HttpServletRequest req) {        List<DeviceInstance> list = deviceInstanceService.listInstanceDeviceByModelType("gateway");        return Result.ok(list);    }    @AutoLog(value = "设备实例-获取实例设备的详情信息")    @ApiOperation(value = "设备实例-获取实例设备的详情信息", notes = "设备实例-获取实例设备的详情信息")    @GetMapping(value = "/instanceDetail")    public Result<?> instanceDetail(@RequestParam(name = "deviceInstanceId", required = true) String deviceInstanceId, HttpServletRequest req) {        if (StringUtils.isEmpty(deviceInstanceId)) {            return Result.error("设备实例id为空，请与管理联系！");        }        DeviceInstanceDetail detail = new DeviceInstanceDetail();        // 设备实例        DeviceInstance deviceInstance = deviceInstanceService.getById(deviceInstanceId);        detail.setDeviceInstance(deviceInstance);        // 模型信息        DeviceModel deviceModel = deviceModelService.getById(deviceInstance.getModelBy());        detail.setDeviceModel(deviceModel);        // 设备数据节点        List<DeviceData> deviceDataList = deviceDataService.lambdaQuery()                .eq(DeviceData::getDeviceModelBy, deviceModel.getId())                // .like(DeviceData::getRwAuthor, "r")                .list();        deviceDataList.stream()                .map(deviceData -> {                    JSONObject jsonObject = JSONObject.parseObject(deviceData.getValueType());                    // 给数据节点，插入上报数据                    List<DataReport> dataReportList = dataReportService.lambdaQuery()                            .eq(DataReport::getInstanceDeviceBy, deviceInstanceId)                            .like(DataReport::getData, deviceData.getCode())                            .orderByDesc(DataReport::getCreateTime)                            .list();                    if (dataReportList != null && dataReportList.size() > 0) {                        DataReport dataReport = dataReportList.get(0);                        String value = JSONObject.parseObject(dataReport.getData()).getString(deviceData.getCode());                        jsonObject.put("value", value); // 上报数据                        jsonObject.put("reportTime",                                DateUtils.formatDate(dataReport.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));  // 上报时间                    }                    deviceData.setValueType(jsonObject.toJSONString());                    return deviceData;                }).collect(Collectors.toList());        detail.setDeviceDataList(deviceDataList);        // 设备功能        List<DeviceFunction> deviceFunctionList = deviceFunctionsService.getFuncWithParamValue(deviceModel.getId(), deviceInstanceId, deviceInstance.getExtendParams());        // 任务执行信息        deviceFunctionList = deviceFunctionList.stream()                .flatMap(deviceFunction -> {                    try {                        deviceFunction.setExecConfig(new DeviceFuncExecConf(deviceInstance.getFuncExecConf(), deviceFunction.getCode()));                    } catch (Exception e) {                        e.printStackTrace();                    }                    return Stream.of(deviceFunction);                }).collect(Collectors.toList());        detail.setDeviceFunctionList(deviceFunctionList);        // 设备事件        List<DeviceEvent> deviceEventList = deviceEventService.lambdaQuery()                .eq(DeviceEvent::getDeviceModelBy, deviceModel.getId())                .list();        detail.setDeviceEventList(deviceEventList);        // 上报数据        List<DataReport> dataReportList = dataReportService.lambdaQuery()                .eq(DataReport::getInstanceDeviceBy, deviceInstanceId)                .list();        detail.setDataReportList(dataReportList);        return Result.ok(detail);    }    /**     * @return     * @功能：刷新缓存     */    @RequestMapping(value = "/refleshCache")    public Result<?> refleshCache() {        //清空缓存        Set keys1 = redisTemplate.keys(CacheConstant.IOT_BUILD_FUNC_DATA_STRUCTURE_CACHE + "*");        Set keys2 = redisTemplate.keys(CacheConstant.IOT_DEVICE_INSTANCE_DATA_NODES_CACHE + "*");        redisTemplate.delete(keys1);        redisTemplate.delete(keys2);        return Result.ok("刷新缓存成功！");    }    @AutoLog(value = "设备实例-执行功能开始")    @ApiOperation(value = "设备实例-执行功能开始", notes = "设备实例-执行功能开始")    @PostMapping(value = "/execFuncStart")    public Result<?> execFuncStart(@RequestBody DeviceFunctionExec functionExec) {        // 清除缓存        Set key = redisTemplate.keys(CacheConstant.IOT_BUILD_FUNC_DATA_STRUCTURE_CACHE);        redisTemplate.delete(key);        if (FuncExecMode.TASK.getCode().equals(functionExec.getExecConfig().getExecMode())) {            deviceInstanceService.deleteExecJob(functionExec);            if (!functionExec.getExecConfig().isRunning()) {                deviceInstanceService.setFuncExecConf(functionExec, false);                return Result.ok("停止功能成功!");            }            deviceInstanceService.addExecJob(functionExec);        } else {            // 调试模式：直接运行            Object obj = deviceFunctionsService.execFunctionSync(functionExec);            return Result.ok(obj);        }        return Result.ok("执行功能成功!");    }    /**     * 添加     *     * @param deviceInstance     * @return     */    @AutoLog(value = "设备实例-添加")    @ApiOperation(value = "设备实例-添加", notes = "设备实例-添加")    @PostMapping(value = "/add")    public Result<?> add(@RequestBody DeviceInstance deviceInstance) {        deviceInstance.setId(deviceInstance.getCode());        deviceInstanceService.save(deviceInstance);        return Result.ok("添加成功！");    }    /**     * 编辑     *     * @param deviceInstance     * @return     */    @AutoLog(value = "设备实例-编辑")    @ApiOperation(value = "设备实例-编辑", notes = "设备实例-编辑")    @PutMapping(value = "/edit")    public Result<?> edit(@RequestBody DeviceInstance deviceInstance) {        deviceInstance.setId(deviceInstance.getCode());        deviceInstanceService.updateById(deviceInstance);        deviceInstanceService.listInstanceChild(deviceInstance.getId()).stream().forEach(instance -> {            instance.setSceneBy(deviceInstance.getSceneBy());            instance.setSceneSchemeBy(deviceInstance.getSceneSchemeBy());            deviceInstanceService.updateById(instance);        });        return Result.ok("编辑成功!");    }    @AutoLog(value = "设备实例-修改状态")    @ApiOperation(value = "设备实例-修改状态", notes = "设备实例-修改状态")    @RequestMapping(value = "/setStatus")    public Result<?> setStatus(@RequestParam(name = "id", required = true) String id) {        DeviceInstance ins = deviceInstanceService.getById(id);        String status = ins.getStatus();        String state = "激活";        if ("notActive".equals(status)) {            ins.setStatus("offline");        } else {            state = "注销";            ins.setStatus("notActive");        }        deviceInstanceService.updateById(ins);        return Result.ok("设备实例" + state + "成功!");    }    /**     * 通过id删除     *     * @param id     * @return     */    @AutoLog(value = "设备实例-通过id删除")    @ApiOperation(value = "设备实例-通过id删除", notes = "设备实例-通过id删除")    @DeleteMapping(value = "/delete")    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {        deviceInstanceService.removeById(id);        return Result.ok("删除成功!");    }    /**     * 批量删除     *     * @param ids     * @return     */    @AutoLog(value = "设备实例-批量删除")    @ApiOperation(value = "设备实例-批量删除", notes = "设备实例-批量删除")    @DeleteMapping(value = "/deleteBatch")    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {        this.deviceInstanceService.removeByIds(Arrays.asList(ids.split(",")));        return Result.ok("批量删除成功!");    }    /**     * 通过id查询     *     * @param id     * @return     */    @AutoLog(value = "设备实例-通过id查询")    @ApiOperation(value = "设备实例-通过id查询", notes = "设备实例-通过id查询")    @GetMapping(value = "/queryById")    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {        DeviceInstance deviceInstance = deviceInstanceService.getById(id);        if (deviceInstance == null) {            return Result.error("未找到对应数据");        }        return Result.ok(deviceInstance);    }    /**     * 导出excel     *     * @param request     * @param deviceInstance     */    @RequestMapping(value = "/exportXls")    public ModelAndView exportXls(HttpServletRequest request, DeviceInstance deviceInstance) {        return super.exportXls(request, deviceInstance, DeviceInstance.class, "设备实例");    }    /**     * 通过excel导入数据     *     * @param request     * @param response     * @return     */    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {        return super.importExcel(request, response, DeviceInstance.class);    }}