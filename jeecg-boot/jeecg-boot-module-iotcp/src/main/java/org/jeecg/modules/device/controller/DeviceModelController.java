package org.jeecg.modules.device.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.entity.*;
import org.jeecg.modules.device.service.*;
import org.jeecg.modules.device.vo.DeviceModelPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 设备模型
 * @Author: jeecg-boot
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Api(tags = "设备模型")
@RestController
@RequestMapping("/device/deviceModel")
@Slf4j
public class DeviceModelController {
    @Autowired
    private IDeviceModelService deviceModelService;
    @Autowired
    private IDeviceDataService deviceDataService;
    @Autowired
    private IDeviceFunctionsService deviceFunctionsService;
    @Autowired
    private IDeviceEventService deviceEventService;
    @Autowired
    private IDeviceLabelService deviceLabelService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 分页列表查询
     *
     * @param deviceModel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备模型-分页列表查询")
    @ApiOperation(value = "设备模型-分页列表查询", notes = "设备模型-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceModel deviceModel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DeviceModel> queryWrapper = QueryGenerator.initQueryWrapper(deviceModel, req.getParameterMap());
        Page<DeviceModel> page = new Page<DeviceModel>(pageNo, pageSize);
        IPage<DeviceModel> pageList = deviceModelService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "设备模型-获取枚举类型列表")
    @ApiOperation(value = "设备模型-获取枚举类型列表", notes = "设备模型-获取枚举类型列表")
    @GetMapping(value = "/listEnum")
    public Result<?> listEnum(@RequestParam(name = "enumName") String enumName, HttpServletRequest req) {

        return Result.ok();
    }

    /**
     * @return
     * @功能：刷新缓存
     */
    @RequestMapping(value = "/refleshCache")
    public Result<?> refleshCache() {
        //清空缓存
        Set keys1 = redisTemplate.keys(CacheConstant.IOT_BUILD_FUNC_DATA_STRUCTURE_CACHE + "*");
        Set keys2 = redisTemplate.keys(CacheConstant.IOT_DEVICE_INSTANCE_DATA_NODES_CACHE + "*");

        redisTemplate.delete(keys1);
        redisTemplate.delete(keys2);
        return Result.ok("刷新缓存成功！");
    }

    /**
     * 获取输入参数集合，排除已选数据id，但保留修改时当前的id
     *
     * @param deviceModelId 设备模型的id
     * @param deviceDataId  当前设备模型的数据节点id
     * @param req
     * @return
     */
    @AutoLog(value = "设备模型-获取输入参数集合")
    @ApiOperation(value = "设备模型-获取输入参数集合", notes = "设备模型-获取输入参数集合")
    @GetMapping(value = "/listInputParams")
    public Result<?> listInputParams(
            @RequestParam(name = "deviceModelId") String deviceModelId,
            @RequestParam(name = "deviceDataId", defaultValue = "") String deviceDataId,
            HttpServletRequest req) {
        JSONArray array = new JSONArray();
        // 1、获取当前model的功能列表
        List<DeviceFunction> funcList = deviceFunctionsService.lambdaQuery().eq(DeviceFunction::getDeviceModelBy, deviceModelId).list();
        System.out.println(funcList.toString());
        // 2、遍历func，获取数据id的集合
        StringBuffer dataIds = new StringBuffer();
        for (DeviceFunction func : funcList) {
            List<InputParam> inputParamList = deviceFunctionsService.listInputParamByFuncId(func.getId());
            System.out.println(inputParamList.toString());
            JSONArray inputParams = JSONArray.parseArray(func.getInputParams());
            for (InputParam inputParam : inputParamList) {
                if (dataIds.length() > 0)
                    dataIds.append(",").append(inputParam.getDataId());
                else
                    dataIds.append(inputParam.getDataId());
            }
        }
        System.out.println(dataIds.toString());
        // select * from device_data where rw_author != 'r' and id not in dataIds;
        List<DeviceData> dataList = deviceDataService.lambdaQuery()
                .ne(DeviceData::getRwAuthor, "r")
                .eq(DeviceData::getDeviceModelBy, deviceModelId)
                // .notIn(DeviceData::getId, dataIds.toString().split(","))
                .list();
        System.out.println(dataList.toString());
        for (DeviceData data : dataList) {
            JSONObject json = new JSONObject();
            json.put("value", data.getId());
            json.put("text", data.getName());
            json.put("title", data.getName());
            json.put("disabled", (!data.getId().equals(deviceDataId)) && (dataIds.length() != 0) && (dataIds.indexOf(data.getId()) != -1));
            array.add(json);
        }
        return Result.ok(array);
    }

    @AutoLog(value = "设备模型-检测功能标识")
    @ApiOperation(value = "设备模型-检测功能标识", notes = "设备模型-检测功能标识")
    @GetMapping(value = "/validateDataCode")
    public Result<?> validateDataCode(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "dataCode") String dataCode,
                                      @RequestParam(name = "dataId", defaultValue = "") String dataId) {
        List<DeviceData> list = deviceDataService.lambdaQuery()
                .eq(DeviceData::getDeviceModelBy, deviceModelId)
                .eq(DeviceData::getCode, dataCode)
                .list();
        if(list.size() > 0) {
            if(list.size() == 1 && list.get(0).getId().equals(dataId))
                return Result.ok("校验通过！");
            else
                return Result.error("功能标识重复");
        }else
            return Result.ok("校验通过！");
    }

    @AutoLog(value = "设备模型-检测功能名称")
    @ApiOperation(value = "设备模型-检测功能名称", notes = "设备模型-检测功能名称")
    @GetMapping(value = "/validateDataName")
    public Result<?> validateDataName(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "dataName") String dataName,
                                      @RequestParam(name = "dataId", defaultValue = "") String dataId) {
        List<DeviceData> list = deviceDataService.lambdaQuery()
                .eq(DeviceData::getDeviceModelBy, deviceModelId)
                .eq(DeviceData::getName, dataName)
                .list();
        if(list.size() > 0) {
            if(list.size() == 1 && list.get(0).getId().equals(dataId))
                return Result.ok("校验通过！");
            else
                return Result.error("功能名称重复");
        }else
            return Result.ok("校验通过！");
    }

    @AutoLog(value = "设备模型-检测功能标识")
    @ApiOperation(value = "设备模型-检测功能标识", notes = "设备模型-检测功能标识")
    @GetMapping(value = "/validateFuncCode")
    public Result<?> validateFuncCode(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "funcCode") String funcCode,
                                      @RequestParam(name = "funcId", defaultValue = "") String funcId) {
        List<DeviceFunction> list = deviceFunctionsService.lambdaQuery()
                .eq(DeviceFunction::getDeviceModelBy, deviceModelId)
                .eq(DeviceFunction::getCode, funcCode)
                .list();
        if (list.size() > 0) {
            if (list.size() == 1 && list.get(0).getId().equals(funcId))
                return Result.ok("校验通过！");
            else
                return Result.error("功能标识重复");
        } else
            return Result.ok("校验通过！");
    }

    @AutoLog(value = "设备模型-检测功能名称")
    @ApiOperation(value = "设备模型-检测功能名称", notes = "设备模型-检测功能名称")
    @GetMapping(value = "/validateFuncName")
    public Result<?> validateFuncName(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "funcName") String funcName,
                                      @RequestParam(name = "funcId", defaultValue = "") String funcId) {
        List<DeviceFunction> list = deviceFunctionsService.lambdaQuery()
                .eq(DeviceFunction::getDeviceModelBy, deviceModelId)
                .eq(DeviceFunction::getName, funcName)
                .list();
        if (list.size() > 0) {
            if (list.size() == 1 && list.get(0).getId().equals(funcId))
                return Result.ok("校验通过！");
            else
                return Result.error("功能名称重复");
        } else
            return Result.ok("校验通过！");
    }

    @AutoLog(value = "设备模型-校验事件标识")
    @ApiOperation(value = "设备模型-校验事件标识", notes = "设备模型-校验事件标识")
    @GetMapping(value = "/validateEventCode")
    public Result<?> validateEventCode(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "eventCode") String eventCode,
                                      @RequestParam(name = "eventId", defaultValue = "") String eventId) {
        List<DeviceEvent> list = deviceEventService.lambdaQuery()
                .eq(DeviceEvent::getDeviceModelBy, deviceModelId)
                .eq(DeviceEvent::getCode, eventCode)
                .list();
        if(list.size() > 0) {
            if(list.size() == 1 && list.get(0).getId().equals(eventId))
                return Result.ok("校验通过！");
            else
                return Result.error("事件标识重复");
        }else
            return Result.ok("校验通过！");
    }

    @AutoLog(value = "设备模型-校验事件名称")
    @ApiOperation(value = "设备模型-校验事件名称", notes = "设备模型-校验事件名称")
    @GetMapping(value = "/validateEventName")
    public Result<?> validateEventName(@RequestParam(name = "deviceModelId") String deviceModelId,
                                      @RequestParam(name = "eventName") String eventName,
                                      @RequestParam(name = "eventId", defaultValue = "") String eventId) {
        List<DeviceEvent> list = deviceEventService.lambdaQuery()
                .eq(DeviceEvent::getDeviceModelBy, deviceModelId)
                .eq(DeviceEvent::getName, eventName)
                .list();
        if(list.size() > 0) {
            if(list.size() == 1 && list.get(0).getId().equals(eventId))
                return Result.ok("校验通过！");
            else
                return Result.error("事件名称重复");
        }else
            return Result.ok("校验通过！");
    }

    /**
     * 添加
     *
     * @param deviceModelPage
     * @return
     */
    @AutoLog(value = "设备模型-添加")
    @ApiOperation(value = "设备模型-添加", notes = "设备模型-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceModel deviceModel) {
        deviceModelService.save(deviceModel);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "设备模型-数据节点-添加")
    @ApiOperation(value = "设备模型-数据节点-添加", notes = "设备模型-数据节点-添加")
    @PostMapping(value = "/addData")
    public Result<?> addData(@RequestBody DeviceData deviceData) {
        deviceDataService.save(deviceData);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "设备模型-功能定义-添加")
    @ApiOperation(value = "设备模型-功能定义-添加", notes = "设备模型-功能定义-添加")
    @PostMapping(value = "/addFunc")
    public Result<?> addFunc(@RequestBody DeviceFunction deviceFunc) {
        // 1、输入参数防重复验证
        Boolean b = deviceFunctionsService.inputParamsCheckUnique(deviceFunc);
        if (!b)
            return Result.error("输入参数重复！");
        // 2、保存
        deviceFunctionsService.save(deviceFunc);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "设备模型-事件定义-添加")
    @ApiOperation(value = "设备模型-事件定义-添加", notes = "设备模型-事件定义-添加")
    @PostMapping(value = "/addEvent")
    public Result<?> addEvent(@RequestBody DeviceEvent deviceEvent) {
        deviceEventService.save(deviceEvent);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "设备模型-标签定义-添加")
    @ApiOperation(value = "设备模型-标签定义-添加", notes = "设备模型-标签定义-添加")
    @PostMapping(value = "/addLabel")
    public Result<?> addLabel(@RequestBody DeviceLabel deviceLabel) {
        deviceLabelService.save(deviceLabel);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "设备模型-数据节点-更新")
    @ApiOperation(value = "设备模型-数据节点-更新", notes = "设备模型-数据节点-更新")
    @PutMapping(value = "/editData")
    public Result<?> editData(@RequestBody DeviceData deviceData) {
        System.out.println(deviceData.toString());
        deviceDataService.saveOrUpdate(deviceData);
        return Result.ok("更新成功！");
    }

    @AutoLog(value = "设备模型-功能定义-更新")
    @ApiOperation(value = "设备模型-功能定义-更新", notes = "设备模型-功能定义-更新")
    @PutMapping(value = "/editFunc")
    public Result<?> editFunc(@RequestBody DeviceFunction deviceFunc) {
        deviceFunctionsService.saveOrUpdate(deviceFunc);
        return Result.ok("更新成功！");
    }

    @AutoLog(value = "设备模型-事件定义-更新")
    @ApiOperation(value = "设备模型-事件定义-更新", notes = "设备模型-事件定义-更新")
    @PutMapping(value = "/editEvent")
    public Result<?> editEvent(@RequestBody DeviceEvent deviceEvent) {
        deviceEventService.saveOrUpdate(deviceEvent);
        return Result.ok("更新成功！");
    }

    @AutoLog(value = "设备模型-标签定义-更新")
    @ApiOperation(value = "设备模型-标签定义-更新", notes = "设备模型-标签定义-更新")
    @PutMapping(value = "/editLabel")
    public Result<?> editLabel(@RequestBody DeviceLabel deviceLabel) {
        deviceLabelService.saveOrUpdate(deviceLabel);
        return Result.ok("更新成功！");
    }

    /**
     * 获取属性子集
     *
     * @param id    属性的id
     * @param aName 属性类型
     * @return
     */
    @AutoLog(value = "设备模型-根据id，获取数据节点-添加")
    @ApiOperation(value = "设备模型-根据id，获取数据节点-添加", notes = "设备模型-根据id，获取数据节点-添加")
    @GetMapping(value = "/getAttributeById")
    public Result<?> getAttributeById(
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "aName", required = true) String aName
    ) {
        if ("data".equals(aName)) {
            return Result.ok(deviceDataService.getById(id));
        } else if ("func".equals(aName)) {
            return Result.ok(deviceFunctionsService.getById(id));
        } else if ("event".equals(aName)) {
            return Result.ok(deviceEventService.getById(id));
        } else if ("label".equals(aName)) {
            return Result.ok(deviceLabelService.getById(id));
        } else {
            return Result.error("获取子集失败");

        }
    }

    /**
     * 编辑
     *
     * @param deviceModelPage
     * @return
     */
    @AutoLog(value = "设备模型-编辑")
    @ApiOperation(value = "设备模型-编辑", notes = "设备模型-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DeviceModel deviceModel) {
        if (deviceModel.getId() != null) {
            deviceModelService.updateById(deviceModel);
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备模型-通过id删除")
    @ApiOperation(value = "设备模型-通过id删除", notes = "设备模型-通过id删除")
    @DeleteMapping(value = "/delete")
    @CacheEvict(value = CacheConstant.IOT_DEVICE_INSTANCE_DATA_NODES_CACHE, allEntries = true)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        deviceModelService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备模型-批量删除")
    @ApiOperation(value = "设备模型-批量删除", notes = "设备模型-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @CacheEvict(value = CacheConstant.IOT_DEVICE_INSTANCE_DATA_NODES_CACHE, allEntries = true)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceModelService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @DeleteMapping(value = "/data/deleteBatch")
    public Result<?> dataDeleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceDataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @DeleteMapping(value = "/func/deleteBatch")
    public Result<?> funcDeleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceFunctionsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @DeleteMapping(value = "/event/deleteBatch")
    public Result<?> eventDeleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceEventService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    @DeleteMapping(value = "/label/deleteBatch")
    public Result<?> labelDeleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceLabelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备模型-通过id查询")
    @ApiOperation(value = "设备模型-通过id查询", notes = "设备模型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceModel deviceModel = deviceModelService.getById(id);
        if (deviceModel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(deviceModel);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据节点集合-通过id查询")
    @ApiOperation(value = "数据节点集合-通过id查询", notes = "数据节点-通过id查询")
    @GetMapping(value = "/queryDeviceDataByMainId")
    public Result<?> queryDeviceDataListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DeviceData> deviceDataList = deviceDataService.selectByMainId(id);
        return Result.ok(deviceDataList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "功能定义集合-通过id查询")
    @ApiOperation(value = "功能定义集合-通过id查询", notes = "功能定义-通过id查询")
    @GetMapping(value = "/queryDeviceFuncByMainId")
    public Result<?> queryDeviceFuncListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DeviceFunction> deviceFunctionList = deviceFunctionsService.selectByMainId(id);
        return Result.ok(deviceFunctionList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "事件定义集合-通过id查询", operateType = 1, logType = 0)
    @ApiOperation(value = "事件定义集合-通过id查询", notes = "事件定义-通过id查询")
    @GetMapping(value = "/queryDeviceEventByMainId")
    public Result<?> queryDeviceEventListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DeviceEvent> deviceEventList = deviceEventService.selectByMainId(id);
        return Result.ok(deviceEventList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据标签集合-通过id查询")
    @ApiOperation(value = "数据标签集合-通过id查询", notes = "数据标签-通过id查询")
    @GetMapping(value = "/queryDeviceLabelByMainId")
    public Result<?> queryDeviceLabelListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DeviceLabel> deviceLabelList = deviceLabelService.selectByMainId(id);
        return Result.ok(deviceLabelList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceModel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceModel deviceModel) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<DeviceModel> queryWrapper = QueryGenerator.initQueryWrapper(deviceModel, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<DeviceModel> queryList = deviceModelService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<DeviceModel> deviceModelList = new ArrayList<DeviceModel>();
        if (oConvertUtils.isEmpty(selections)) {
            deviceModelList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            deviceModelList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<DeviceModelPage> pageList = new ArrayList<DeviceModelPage>();
        for (DeviceModel main : deviceModelList) {
            DeviceModelPage vo = new DeviceModelPage();
            BeanUtils.copyProperties(main, vo);
            List<DeviceData> deviceDataList = deviceDataService.selectByMainId(main.getId());
            vo.setDeviceDataList(deviceDataList);
            List<DeviceFunction> deviceFunctionList = deviceFunctionsService.selectByMainId(main.getId());
            vo.setDeviceFunctionList(deviceFunctionList);
            List<DeviceEvent> deviceEventList = deviceEventService.selectByMainId(main.getId());
            vo.setDeviceEventList(deviceEventList);
            List<DeviceLabel> deviceLabelList = deviceLabelService.selectByMainId(main.getId());
            vo.setDeviceLabelList(deviceLabelList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "设备模型列表");
        mv.addObject(NormalExcelConstants.CLASS, DeviceModelPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("设备模型数据", "导出人:" + sysUser.getRealname(), "设备模型"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<DeviceModelPage> list = ExcelImportUtil.importExcel(file.getInputStream(), DeviceModelPage.class, params);
                for (DeviceModelPage page : list) {
                    DeviceModel po = new DeviceModel();
                    BeanUtils.copyProperties(page, po);
                    deviceModelService.saveMain(po, page.getDeviceDataList(), page.getDeviceFunctionList(), page.getDeviceEventList(), page.getDeviceLabelList());
                }
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
