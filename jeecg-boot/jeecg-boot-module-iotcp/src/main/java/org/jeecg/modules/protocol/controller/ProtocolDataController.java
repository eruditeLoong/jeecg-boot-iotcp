package org.jeecg.modules.protocol.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.protocol.entity.ProtocolData;
import org.jeecg.modules.protocol.service.IProtocolDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;

/**
 * @Description: 数据解析协议
 * @Author: jeecg-boot
 * @Date: 2020-04-19
 * @Version: V1.0
 */
@Api(tags = "数据解析协议")
@RestController
@RequestMapping("/protocol/protocolData")
@Slf4j
public class ProtocolDataController extends JeecgController<ProtocolData, IProtocolDataService> {
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IProtocolDataService protocolDataService;

    /**
     * 分页列表查询
     *
     * @param protocolData
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "数据解析协议-分页列表查询")
    @ApiOperation(value = "数据解析协议-分页列表查询", notes = "数据解析协议-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ProtocolData protocolData,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ProtocolData> queryWrapper = QueryGenerator.initQueryWrapper(protocolData, req.getParameterMap());
        Page<ProtocolData> page = new Page<ProtocolData>(pageNo, pageSize);
        IPage<ProtocolData> pageList = protocolDataService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * @return
     * @功能：刷新缓存
     */
    @RequestMapping(value = "/refleshCache")
    public Result<?> refleshCache() {
        //清空字典缓存
        Set keys = redisTemplate.keys(CacheConstant.IOT_LOAD_JAR_METHOD_CACHE + "*");
        Set keys2 = redisTemplate.keys(CacheConstant.IOT_MODEL_DATA_PROTOCOL_CACHE + "*");
        redisTemplate.delete(keys);
        redisTemplate.delete(keys2);
        return Result.ok("刷新缓存成功！");
    }

    /**
     * 添加
     *
     * @param protocolData
     * @return
     */
    @AutoLog(value = "数据解析协议-添加")
    @ApiOperation(value = "数据解析协议-添加", notes = "数据解析协议-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ProtocolData protocolData) {
        protocolDataService.save(protocolData);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param protocolData
     * @return
     */
    @AutoLog(value = "数据解析协议-编辑")
    @ApiOperation(value = "数据解析协议-编辑", notes = "数据解析协议-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ProtocolData protocolData) {
        protocolDataService.updateById(protocolData);
        Set keys = redisTemplate.keys(CacheConstant.IOT_LOAD_JAR_METHOD_CACHE);
        Set keys2 = redisTemplate.keys(CacheConstant.IOT_MODEL_DATA_PROTOCOL_CACHE);
        redisTemplate.delete(keys);
        redisTemplate.delete(keys2);
        return Result.ok("编辑成功，并刷新缓存!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据解析协议-通过id删除")
    @ApiOperation(value = "数据解析协议-通过id删除", notes = "数据解析协议-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        protocolDataService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "数据解析协议-批量删除")
    @ApiOperation(value = "数据解析协议-批量删除", notes = "数据解析协议-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.protocolDataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "数据解析协议-通过id查询")
    @ApiOperation(value = "数据解析协议-通过id查询", notes = "数据解析协议-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ProtocolData protocolData = protocolDataService.getById(id);
        if (protocolData == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(protocolData);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param protocolData
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProtocolData protocolData) {
        return super.exportXls(request, protocolData, ProtocolData.class, "数据解析协议");
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
        return super.importExcel(request, response, ProtocolData.class);
    }

}
