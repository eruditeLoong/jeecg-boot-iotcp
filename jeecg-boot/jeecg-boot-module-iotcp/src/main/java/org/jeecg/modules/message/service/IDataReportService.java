package org.jeecg.modules.message.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.message.entity.DataReport;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

/**
 * @Description: 设备上传数据
 * @Author: jeecg-boot
 * @Date: 2020-05-14
 * @Version: V1.0
 */
public interface IDataReportService extends IService<DataReport> {

    /**
     * 校验并构建数据节点的数据
     *
     * @param deviceInstanceId
     * @param dataMap
     * @return
     */
    public JSONObject verifyBuildData(String deviceInstanceId, Map<String, Object> dataMap);
}
