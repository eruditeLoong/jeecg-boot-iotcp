package org.jeecg.modules.network.controller;

import com.alibaba.fastjson.JSONArray;
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
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.jeecg.modules.network.network.NetworkManager;
import org.jeecg.modules.network.service.INetworkServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 网络服务管理
 * @Author: jeecg-boot
 * @Date: 2020-04-12
 * @Version: V1.0
 */
@Api(tags = "网络服务管理")
@RestController
@RequestMapping("/network/networkService")
@Slf4j
public class NetworkServiceController extends JeecgController<NetworkService, INetworkServiceService> {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private INetworkServiceService networkServiceService;
    @Autowired
    private NetworkManager networkManager;

    /**
     * 分页列表查询
     *
     * @param networkService
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "网络服务管理-分页列表查询")
    @ApiOperation(value = "网络服务管理-分页列表查询", notes = "网络服务管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(NetworkService networkService,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<NetworkService> queryWrapper = QueryGenerator.initQueryWrapper(networkService, req.getParameterMap());
        Page<NetworkService> page = new Page<NetworkService>(pageNo, pageSize);
        IPage<NetworkService> pageList = networkServiceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "网络服务管理-获取服务连接")
    @ApiOperation(value = "网络服务管理-获取服务连接", notes = "网络服务管理-获取服务连接")
    @RequestMapping(value = "/listConnectById")
    public Result<?> listConnectById(@RequestParam(name = "address", required = true) String address) {
        // 获取设备实例连接,
        Map<String, NetworkConnect> connectMap = NetworkConnectStore.getNetworkConnectMap();

        System.out.println(connectMap);
        Set<String> set = connectMap.keySet();

        JSONArray arr = new JSONArray();
        for (String adds : set) {
            NetworkConnect connect = connectMap.get(adds);
            if (!address.equals(connect.getChannelHandlerContext().channel().localAddress().toString().substring(1))) {
                continue;
            }

            DeviceInstance deviceInstance = new DeviceInstance();
            JSONObject json = new JSONObject();
            if (connect.getChannelHandlerContext() == null || !connect.getChannelHandlerContext().channel().isRegistered()) {
                json.put("isConnected", false);
                deviceInstance = null;
            } else {
                json.put("isConnected", true);
                deviceInstance = connect.getDeviceInstance();
            }
            json.put("id", deviceInstance == null ? "" : deviceInstance.getId());
            json.put("code", deviceInstance == null ? "" : deviceInstance.getCode());
            json.put("name", deviceInstance == null ? "" : deviceInstance.getName());
            json.put("addr", connect.getSocketAddress());
            json.put("description", deviceInstance == null ? "" : deviceInstance.getDescription());
            arr.add(json);
        }
        return Result.ok(arr);
    }

    @AutoLog(value = "网络服务管理-启动服务")
    @ApiOperation(value = "网络服务管理-启动服务", notes = "网络服务管理-启动服务，根据服务id")
    @RequestMapping(value = "/startService")
    public Result<?> startService(@RequestParam(name = "id", required = true) String id) {
        NetworkService networkService = networkServiceService.getById(id);
        // 1、启动服务
        networkManager.reload(networkService);
        // 2、设置数据状态
        networkService.setState(true);
        networkServiceService.updateById(networkService);
        return Result.ok("启动服务成功");
    }

    @AutoLog(value = "网络服务管理-停止服务")
    @ApiOperation(value = "网络服务管理-停止服务", notes = "网络服务管理-停止服务，根据服务id")
    @RequestMapping(value = "/stopService")
    public Result<?> stopService(@RequestParam(name = "id", required = true) String id) {
        NetworkService networkService = networkServiceService.getById(id);
        // 1、停止服务
        networkManager.shutdown(networkService);
        // 2、设置数据状态
        networkService.setState(false);
        networkServiceService.updateById(networkService);
        log.info("停止服务成功");
        return Result.ok("停止服务成功");
    }

    /**
     * 添加
     *
     * @param networkService
     * @return
     */
    @AutoLog(value = "网络服务管理-添加")
    @ApiOperation(value = "网络服务管理-添加", notes = "网络服务管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody NetworkService networkService) {
        networkServiceService.save(networkService);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param networkService
     * @return
     */
    @AutoLog(value = "网络服务管理-编辑")
    @ApiOperation(value = "网络服务管理-编辑", notes = "网络服务管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody NetworkService networkService) {
        networkServiceService.updateById(networkService);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "网络服务管理-通过id删除")
    @ApiOperation(value = "网络服务管理-通过id删除", notes = "网络服务管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        networkServiceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "网络服务管理-批量删除")
    @ApiOperation(value = "网络服务管理-批量删除", notes = "网络服务管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.networkServiceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "网络服务管理-通过id查询")
    @ApiOperation(value = "网络服务管理-通过id查询", notes = "网络服务管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        NetworkService networkService = networkServiceService.getById(id);
        if (networkService == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(networkService);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param networkService
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NetworkService networkService) {
        return super.exportXls(request, networkService, NetworkService.class, "网络服务管理");
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
        return super.importExcel(request, response, NetworkService.class);
    }

}
