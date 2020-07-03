package org.jeecg.modules.scene.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.scene.entity.Scene;
import org.jeecg.modules.scene.entity.SceneScheme;
import org.jeecg.modules.scene.service.ISceneSchemeService;
import org.jeecg.modules.scene.service.ISceneService;
import org.jeecg.modules.scene.vo.ScenePage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 场景管理
 * @Author: jeecg-boot
 * @Date: 2020-06-04
 * @Version: V1.0
 */
@Api(tags = "场景管理")
@RestController
@RequestMapping("/scene/scene")
@Slf4j
public class SceneController {
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private ISceneSchemeService sceneSchemeService;

    /**
     * 分页列表查询
     *
     * @param scene
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "场景管理-分页列表查询")
    @ApiOperation(value = "场景管理-分页列表查询", notes = "场景管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Scene scene,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Scene> queryWrapper = QueryGenerator.initQueryWrapper(scene, req.getParameterMap());
        Page<Scene> page = new Page<Scene>(pageNo, pageSize);
        IPage<Scene> pageList = sceneService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param scenePage
     * @return
     */
    @AutoLog(value = "场景管理-添加")
    @ApiOperation(value = "场景管理-添加", notes = "场景管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Scene scene) {
        sceneService.save(scene);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param scenePage
     * @return
     */
    @AutoLog(value = "场景管理-编辑")
    @ApiOperation(value = "场景管理-编辑", notes = "场景管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Scene scene) {
        sceneService.updateById(scene);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id查询场景下方案数量
     *
     * @param id
     * @return
     */
    @AutoLog(value = "场景管理-通过id查询场景下方案数量")
    @ApiOperation(value = "场景管理-通过id查询场景下方案数量", notes = "场景管理-通过id查询场景下方案数量")
    @DeleteMapping(value = "/countScheme")
    public Result<?> countScheme(@RequestParam(name = "id", required = true) String id) {
        Result result = new Result();
        // 删除前先判断有无方案，有方案询问一起删除
        int count = sceneSchemeService.lambdaQuery()
                .eq(SceneScheme::getSceneBy, id)
                .count();
        return Result.ok(count);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "场景管理-通过id删除")
    @ApiOperation(value = "场景管理-通过id删除", notes = "场景管理-通过id删除")
    @DeleteMapping(value = "/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        int schemeNum = 0;
        String msg = "场景删除成功";
        sceneService.removeById(id);
        List<SceneScheme> list = sceneSchemeService.lambdaQuery()
                .eq(SceneScheme::getSceneBy, id)
                .list();
        schemeNum = list.size();
        if (schemeNum > 0) {
            msg = "场景删除成功，并删除" + schemeNum + "条方案!";
            list.stream().forEach(sceneScheme -> sceneSchemeService.removeById(sceneScheme.getId()));
        }
        return Result.ok(msg);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "场景管理-批量删除")
    @ApiOperation(value = "场景管理-批量删除", notes = "场景管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @Transactional
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        int schemeNum = 0;
        String msg = "场景批量删除成功";
        this.sceneService.removeByIds(Arrays.asList(ids.split(",")));
        List<SceneScheme> list = sceneSchemeService.lambdaQuery()
                .in(SceneScheme::getSceneBy, ids)
                .list();

        schemeNum = list.size();
        if (schemeNum > 0) {
            msg = "场景删除成功，并删除" + schemeNum + "条方案!";
            list.stream().forEach(sceneScheme -> sceneSchemeService.removeById(sceneScheme.getId()));
        }
        return Result.ok(msg);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "场景管理-通过id查询")
    @ApiOperation(value = "场景管理-通过id查询", notes = "场景管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Scene scene = sceneService.getById(id);
        if (scene == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(scene);
    }

    @AutoLog(value = "场景管理-获取当前登录机构下的场景配置信息")
    @ApiOperation(value = "场景管理-获取场景配置信息", notes = "场景管理-获取当前登录机构下的场景配置信息")
    @GetMapping(value = "/getSceneConfig")
    public Result<?> getSceneConfig(@RequestParam(name = "id", required = false) String id) {
        Scene scene = new Scene();
        if(oConvertUtils.isEmpty(id)){
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            log.info("当前用户所属组织：{}",sysUser.getOrgCode());
            List<Scene> list = sceneService.lambdaQuery()
                    .eq(Scene::getSysOrgCode, sysUser.getOrgCode())
                    .list();
            if(list!=null && list.size()>0){
                scene = list.get(0);
            }else{
                return Result.error("未找到对应数据");
            }
        }else{
            scene = sceneService.getById(id);
            if (scene == null) {
                return Result.error("未找到对应数据");
            }
        }

        return Result.ok(scene);
    }

    /**
     * 通过当前组织org查询
     *
     * @param org
     * @return
     */
    @AutoLog(value = "场景管理-通过当前组织org查询")
    @ApiOperation(value = "场景管理-通过当前组织org查询", notes = "场景管理-通过当前组织org查询")
    @GetMapping(value = "/listSceneByCurrentOrg")
    public Result<?> listSceneByCurrentOrg() {

    	LambdaQueryChainWrapper<Scene> lambdaQuery = sceneService.lambdaQuery();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(!"admin".equals(sysUser.getUsername())){
            // 获取用户当前登录组织, 查询自己组织下的实例
            lambdaQuery.eq(Scene::getSysOrgCode, sysUser.getOrgCode());
        }

        List<Scene> sceneList = lambdaQuery.list();
        return Result.ok(sceneList);

    }

    /**
     * 导出excel
     *
     * @param request
     * @param scene
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scene scene) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Scene> queryWrapper = QueryGenerator.initQueryWrapper(scene, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<Scene> queryList = sceneService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<Scene> sceneList = new ArrayList<Scene>();
        if (oConvertUtils.isEmpty(selections)) {
            sceneList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            sceneList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ScenePage> pageList = new ArrayList<ScenePage>();
        for (Scene main : sceneList) {
            ScenePage vo = new ScenePage();
            BeanUtils.copyProperties(main, vo);
            List<SceneScheme> sceneSchemeList = sceneSchemeService.lambdaQuery()
                    .eq(SceneScheme::getSceneBy, main.getId())
                    .list();
            vo.setSceneSchemeList(sceneSchemeList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "场景管理列表");
        mv.addObject(NormalExcelConstants.CLASS, ScenePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("场景管理数据", "导出人:" + sysUser.getRealname(), "场景管理"));
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
                List<ScenePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScenePage.class, params);
                for (ScenePage scenePage : list) {
                    Scene scene = new Scene();
                    BeanUtils.copyProperties(scenePage, scene);
                    sceneService.save(scene);
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
