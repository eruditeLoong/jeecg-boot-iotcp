package org.jeecg.modules.protocol.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouwr.protocol.DataProtocolProvider;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.device.entity.DeviceModel;
import org.jeecg.modules.device.mapper.DeviceModelMapper;
import org.jeecg.modules.protocol.entity.ProtocolData;
import org.jeecg.modules.protocol.mapper.ProtocolDataMapper;
import org.jeecg.modules.protocol.service.IProtocolDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Description: 数据解析协议
 * @Author: jeecg-boot
 * @Date: 2020-04-19
 * @Version: V1.0
 */
@Slf4j
@Service
public class ProtocolDataServiceImpl extends ServiceImpl<ProtocolDataMapper, ProtocolData> implements IProtocolDataService {

    @Autowired
    private DeviceModelMapper deviceModelMapper;

    @Value("${jeecg.path.upload}")
    private String uploadPath;

    @Override
    @Cacheable(value = CacheConstant.IOT_MODEL_DATA_PROTOCOL_CACHE)
    public ProtocolData getDeviceDataProtocol(String deviceModelId) {
        log.info("无缓存ProtocolData的时候调用这里！");
        DeviceModel deviceModel = deviceModelMapper.selectById(deviceModelId);
        String dataProtoclId = deviceModel.getDataProtocolBy();
        ProtocolData protocolData = this.getById(dataProtoclId);
        return protocolData;
    }

    @Cacheable(value = CacheConstant.IOT_LOAD_JAR_METHOD_CACHE, key = "#protocolData.id")
    @Override
    public DataProtocolProvider loadDataProtocolProvider(ProtocolData protocolData) throws Exception {
        log.info("无缓存loadDataProtocolProvider的时候调用这里！");
        log.info("上传文件路径：{}", uploadPath);
        File file = new File(uploadPath + "/" + protocolData.getPackagePath());//jar包的路径
        log.info("jar包的路径: {}", file.getAbsolutePath());
        URL url = file.toURI().toURL();
        ClassLoader loader = new URLClassLoader(new URL[]{url});//创建类加载器
        Class<?> cls = loader.loadClass(protocolData.getClassPath());//加载指定类，注意一定要带上类的包名
        log.info("加载数据解析类：{}", cls.toString());
        return (DataProtocolProvider) cls.newInstance();//初始化一个实例
    }
}
