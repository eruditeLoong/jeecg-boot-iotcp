package org.jeecg.modules.scene.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.jeecg.modules.device.vo.DeviceInstanceTree;
import org.jeecg.modules.scene.entity.SceneScheme;
import org.jeecg.modules.scene.service.ISceneSchemeService;
import org.jeecg.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 场景方案
 * @Author: jeecg-boot
 * @Date: 2020-06-05
 * @Version: V1.0
 */
@Api(tags = "场景方案")
@RestController
@RequestMapping("/scene/sceneScheme")
@Slf4j
public class SceneSchemeController extends JeecgController<SceneScheme, ISceneSchemeService> {
    @Autowired
    private ISceneSchemeService sceneSchemeService;
	@Autowired
	private IDeviceInstanceService instanceService;
	@Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param sceneScheme
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "场景方案-分页列表查询")
    @ApiOperation(value = "场景方案-分页列表查询", notes = "场景方案-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SceneScheme sceneScheme,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SceneScheme> queryWrapper = QueryGenerator.initQueryWrapper(sceneScheme, req.getParameterMap());
        Page<SceneScheme> page = new Page<SceneScheme>(pageNo, pageSize);
        IPage<SceneScheme> pageList = sceneSchemeService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "场景方案-分页列表查询")
    @ApiOperation(value = "场景方案-分页列表查询", notes = "场景方案-分页列表查询")
    @GetMapping(value = "/listBySceneId")
    public Result<?> listBySceneId(@RequestParam(name = "sceneId") String sceneId, HttpServletRequest req) {
        List<SceneScheme> schemeList = sceneSchemeService.lambdaQuery()
                .eq(SceneScheme::getSceneBy, sceneId)
                .list();
        return Result.ok(schemeList);
    }

	@AutoLog(value = "场景方案-获取组织下所有的设备实例")
	@ApiOperation(value = "场景方案-获取组织下所有的设备实例", notes = "场景方案-获取组织下所有的设备实例")
	@GetMapping(value = "/listInsDeviceByOrg")
	public Result<?> listInsDeviceByOrg(HttpServletRequest req) {
        Result<List<DeviceInstanceTree>> result = new Result<>();

        List<DeviceInstance> instanceList = instanceService.lambdaQuery()
				// .eq(DeviceInstance::getOrgBy, "")
				.list();

        List<DeviceInstanceTree> treeList = instanceList
                .stream()
                .map(deviceInstance -> {
                    DeviceInstanceTree deviceInstanceTree = new DeviceInstanceTree(deviceInstance);
                    return deviceInstanceTree;
                })
                .collect(Collectors.toList());

        List<DeviceInstanceTree> trees = TreeUtil.listToTree(treeList);
        result.setResult(trees);
        result.setSuccess(true);
		return result;
	}

    /**
     *   添加
     *
     * @param sceneScheme
     * @return
     */
    @AutoLog(value = "场景方案-添加")
    @ApiOperation(value = "场景方案-添加", notes = "场景方案-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SceneScheme sceneScheme) {
        sceneSchemeService.save(sceneScheme);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sceneScheme
     * @return
     */
    @AutoLog(value = "场景方案-编辑")
    @ApiOperation(value = "场景方案-编辑", notes = "场景方案-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SceneScheme sceneScheme) {
        sceneSchemeService.updateById(sceneScheme);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "场景方案-通过id删除")
    @ApiOperation(value = "场景方案-通过id删除", notes = "场景方案-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sceneSchemeService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "场景方案-批量删除")
    @ApiOperation(value = "场景方案-批量删除", notes = "场景方案-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sceneSchemeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "场景方案-通过id查询")
    @ApiOperation(value = "场景方案-通过id查询", notes = "场景方案-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SceneScheme sceneScheme = sceneSchemeService.getById(id);
        if (sceneScheme == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(sceneScheme);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sceneScheme
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SceneScheme sceneScheme) {
        return super.exportXls(request, sceneScheme, SceneScheme.class, "场景方案");
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
        return super.importExcel(request, response, SceneScheme.class);
    }

}
