package org.jeecg.modules.network.network.tcp;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.service.impl.DeviceInstanceServiceImpl;
import org.jeecg.modules.message.entity.DataReport;
import org.jeecg.modules.message.entity.ReceiveMessage;
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

    ReceiveMessage receiveMessage;

    /***************************************************
     * 接收数据格式：
     * {
     *     "type": "data"
     *     "deviceInstanceId": "device-1",
     *     "deviceModelId": "1252585944936529921",
     *     "datetime": 1591164493312,
     *     "dataMap": {
     *         "temperature": 33.9,
     *         "key": value
     *         ...
     *     },
     * }
     ***************************************************/



    /**************
     *
     * @param ctx
     * @param object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {

        dataReportService = ApplicationContextUtil.getContext().getBean(DataReportServiceImpl.class);
        deviceInstanceService = ApplicationContextUtil.getContext().getBean(DeviceInstanceServiceImpl.class);

        receiveMessage = new ReceiveMessage(object.toString());

        // 拿到传过来的msg数据，开始处理
        if (MessageType.HEART.getCode().equals(receiveMessage.getType())) {
            // 心跳数据直接返回
            // ctx.writeAndFlush(System.currentTimeMillis());
        } else if (MessageType.DATA.getCode().equals(receiveMessage.getType())) {
            // 判断传感器有效
            int count = deviceInstanceService.lambdaQuery()
                    .eq(DeviceInstance::getId, receiveMessage.getDeviceInstanceId())
                    .eq(DeviceInstance::getStatus, "online")
                    .count();
            if (count == 1) {
                JSONObject data = dataReportService.verifyBuildData(receiveMessage.getDeviceInstanceId(), receiveMessage.getDataMap());

                DataReport dataReport = new DataReport();
                dataReport.setCreateTime(new Date(receiveMessage.getDatetime()));
                dataReport.setData(data.toJSONString());
                dataReport.setInputMode("report");
                dataReport.setInstanceDeviceBy(receiveMessage.getDeviceInstanceId());
                log.info(dataReport.toString());
                dataReportService.save(dataReport);
            }

        } else if (MessageType.EVENT.getCode().equals(receiveMessage.getType())) {

        }

    }

    public ReceiveMessage getResponse(){
        return receiveMessage;
    }
}
