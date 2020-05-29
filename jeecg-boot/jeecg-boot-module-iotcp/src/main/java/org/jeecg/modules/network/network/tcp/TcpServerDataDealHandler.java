package org.jeecg.modules.network.network.tcp;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.service.impl.DeviceInstanceServiceImpl;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.entity.ReceiveData;
import org.jeecg.modules.message.enums.MessageType;
import org.jeecg.modules.message.service.impl.DataReportServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TcpServerDataDealHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private DataReportServiceImpl dataReportService;
    @Autowired
    private DeviceInstanceServiceImpl deviceInstanceService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {

        dataReportService = ApplicationContextUtil.getContext().getBean(DataReportServiceImpl.class);
        deviceInstanceService = ApplicationContextUtil.getContext().getBean(DeviceInstanceServiceImpl.class);

        ReceiveData receiveData = new ReceiveData();
        try {
            receiveData = (ReceiveData) object;
        } catch (ClassCastException e) {
            log.error("接收的数据无法完成转换，请检查数据格式，或数据解析逻辑！{}", e.getMessage());
            return;
        }

        // 拿到传过来的msg数据，开始处理
        if (MessageType.HEART.getCode().equals(receiveData.getType())) {
            // 心跳数据直接返回
            // ctx.writeAndFlush(System.currentTimeMillis());
        } else if (MessageType.REPORT_DATA.getCode().equals(receiveData.getType())) {
            // 判断传感器有效
            int count = deviceInstanceService.lambdaQuery()
                    .eq(DeviceInstance::getId, receiveData.getDeviceInstanceId())
                    .eq(DeviceInstance::getStatus, "online")
                    .count();
            if (count == 1) {
                JSONObject data = dataReportService.verifyBuildData(receiveData.getDeviceModelId(), receiveData.getDataMap());

                DataReport dataReport = new DataReport();
                dataReport.setCreateTime(new Date(receiveData.getDatetime()));
                dataReport.setData(data.toJSONString());
                dataReport.setInputMode("report");
                dataReport.setInstanceDeviceBy(receiveData.getDeviceInstanceId());
                dataReportService.save(dataReport);
            }

        } else if (MessageType.EVENT.getCode().equals(receiveData.getType())) {

        }

    }
}
