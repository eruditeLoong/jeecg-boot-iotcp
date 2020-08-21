package org.jeecg.modules.scene.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
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
import org.springframework.stereotype.Controller;
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
@Slf4j
@RestController
@RequestMapping("/scene/deploy")
public class SceneDeployController {
    @Autowired
    private ISceneService sceneService;
    @Autowired
    private ISceneSchemeService sceneSchemeService;

    @RequestMapping("/index")
    public ModelAndView ftl(String token, ModelAndView modelAndView) {
        modelAndView.setViewName("deploy/index");
        modelAndView.addObject("token", token);
        return modelAndView;
    }

    @RequestMapping("/editor")
    public ModelAndView editor(String token, ModelAndView modelAndView) {
        modelAndView.setViewName("deploy/editor");
        modelAndView.addObject("token", token);
        return modelAndView;
    }

    @RequestMapping("/deploy")
    public ModelAndView deploy(ModelAndView modelAndView) {
        modelAndView.setViewName("deploy/deploy");
        return modelAndView;
    }
}
