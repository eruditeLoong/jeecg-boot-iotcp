package org.jeecg.modules.message.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.device.entity.DeviceData;
import org.jeecg.modules.device.mapper.DeviceDataMapper;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.service.IDataReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 设备上传数据
 * @Author: jeecg-boot
 * @Date: 2020-05-14
 * @Version: V1.0
 */
@Api(tags = "设备上传数据")
@RestController
@RequestMapping("/message/data")
@Slf4j
public class DataReportController extends JeecgController<DataReport, IDataReportService> {
    @Autowired
    private IDataReportService dataService;
    @Autowired
    private DeviceDataMapper deviceDataMapper;

    /**
     * 分页列表查询
     *
     * @param data
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "设备上传数据-分页列表查询")
    @ApiOperation(value = "设备上传数据-分页列表查询", notes = "设备上传数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DataReport data,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<DataReport> queryWrapper = QueryGenerator.initQueryWrapper(data, req.getParameterMap());
        Page<DataReport> page = new Page<DataReport>(pageNo, pageSize);
        IPage<DataReport> pageList = dataService.page(page, queryWrapper);
        pageList.setRecords(
                pageList.getRecords().stream()
                .flatMap(dataReport -> {
                    JSONObject dataJson = new JSONObject();
                    String instanceId = dataReport.getInstanceDeviceBy();
                    // 根据设备实例id，获取设备的数据节点
                    List<DeviceData> deviceDataList = deviceDataMapper.listDeviceDataByInstanceId(instanceId);
                    for(DeviceData deviceData: deviceDataList) {
                        // dataJson.put("key", deviceData.getCode());
                        // dataJson.put("name", deviceData.getName());
                        // dataJson.put("value", JSONObject.parseObject(dataReport.getData()).get(deviceData.getCode()));
                        // dataJson.put("unit", JSONObject.parseObject(deviceData.getValueType()));
                        dataJson.put(deviceData.getName(),
                                JSONObject.parseObject(dataReport.getData()).get(deviceData.getCode()));
                    }
                    dataReport.setData(dataJson.toJSONString());
                    return Stream.of(dataReport);
                }).collect(Collectors.toList())
        );
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param data
     * @return
     */
    @AutoLog(value = "设备上传数据-添加")
    @ApiOperation(value = "设备上传数据-添加", notes = "设备上传数据-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DataReport data) {
        dataService.save(data);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param data
     * @return
     */
    @AutoLog(value = "设备上传数据-编辑")
    @ApiOperation(value = "设备上传数据-编辑", notes = "设备上传数据-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DataReport data) {
        dataService.updateById(data);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备上传数据-通过id删除")
    @ApiOperation(value = "设备上传数据-通过id删除", notes = "设备上传数据-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        dataService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "设备上传数据-批量删除")
    @ApiOperation(value = "设备上传数据-批量删除", notes = "设备上传数据-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.dataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设备上传数据-通过id查询")
    @ApiOperation(value = "设备上传数据-通过id查询", notes = "设备上传数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DataReport data = dataService.getById(id);
        if (data == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(data);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param data
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DataReport data) {
        return super.exportXls(request, data, DataReport.class, "设备上传数据");
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
        return super.importExcel(request, response, DataReport.class);
    }

}
