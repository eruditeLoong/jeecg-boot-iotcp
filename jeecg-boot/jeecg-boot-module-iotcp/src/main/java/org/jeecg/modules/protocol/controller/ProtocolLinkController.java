package org.jeecg.modules.demo.protocol.controller;

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
import org.jeecg.modules.demo.protocol.entity.ProtocolLink;
import org.jeecg.modules.demo.protocol.service.IProtocolLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 传输协议
 * @Author: jeecg-boot
 * @Date:   2020-04-10
 * @Version: V1.0
 */
@Api(tags="传输协议")
@RestController
@RequestMapping("/protocol/protocolLink")
@Slf4j
public class ProtocolLinkController extends JeecgController<ProtocolLink, IProtocolLinkService> {
	@Autowired
	private IProtocolLinkService protocolLinkService;
	
	/**
	 * 分页列表查询
	 *
	 * @param protocolLink
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "传输协议-分页列表查询")
	@ApiOperation(value="传输协议-分页列表查询", notes="传输协议-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProtocolLink protocolLink,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProtocolLink> queryWrapper = QueryGenerator.initQueryWrapper(protocolLink, req.getParameterMap());
		Page<ProtocolLink> page = new Page<ProtocolLink>(pageNo, pageSize);
		IPage<ProtocolLink> pageList = protocolLinkService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param protocolLink
	 * @return
	 */
	@AutoLog(value = "传输协议-添加")
	@ApiOperation(value="传输协议-添加", notes="传输协议-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProtocolLink protocolLink) {
		protocolLinkService.save(protocolLink);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param protocolLink
	 * @return
	 */
	@AutoLog(value = "传输协议-编辑")
	@ApiOperation(value="传输协议-编辑", notes="传输协议-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProtocolLink protocolLink) {
		protocolLinkService.updateById(protocolLink);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "传输协议-通过id删除")
	@ApiOperation(value="传输协议-通过id删除", notes="传输协议-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		protocolLinkService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "传输协议-批量删除")
	@ApiOperation(value="传输协议-批量删除", notes="传输协议-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.protocolLinkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "传输协议-通过id查询")
	@ApiOperation(value="传输协议-通过id查询", notes="传输协议-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProtocolLink protocolLink = protocolLinkService.getById(id);
		if(protocolLink==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(protocolLink);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param protocolLink
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProtocolLink protocolLink) {
        return super.exportXls(request, protocolLink, ProtocolLink.class, "传输协议");
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
        return super.importExcel(request, response, ProtocolLink.class);
    }

}
