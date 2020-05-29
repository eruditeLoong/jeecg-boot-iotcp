package org.jeecg.modules.protocol.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouwr.protocol.DataProtocolProvider;
import org.jeecg.modules.protocol.entity.ProtocolData;

/**
 * @Description: 数据解析协议
 * @Author: jeecg-boot
 * @Date: 2020-04-19
 * @Version: V1.0
 */
public interface IProtocolDataService extends IService<ProtocolData> {

    /**
     * 根据设备模型id，获取设备的数据解析对象
     *
     * @param deviceModelId
     * @return
     */
    public ProtocolData getDeviceDataProtocol(String deviceModelId);

    /**
     * 动态加载指定jar包调用其中某个类的方法
     *
     * @param protocolData
     * @return
     * @throws Exception
     */
    public DataProtocolProvider loadDataProtocolProvider(ProtocolData protocolData) throws Exception;
}
