package org.jeecg.modules.network.network.tcp;

import com.alibaba.fastjson.JSONObject;
import com.zhouwr.protocol.DataProtocolProvider;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.jeecg.modules.protocol.entity.ProtocolData;
import org.jeecg.modules.protocol.service.IProtocolDataService;
import org.jeecg.utils.HexConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;

/**
 * @program: 物联网平台系统
 * @description: TCP动态解码器
 * @author: zhouwr
 * @create: 2020-05-23 11:29
 * @version：1.0
 **/
@Slf4j
@Component
public class TcpDynamicEncoder extends MessageToMessageEncoder<CharSequence> {

    private static TcpDynamicEncoder tcpDynamicEncoder;
    @Autowired
    private NetworkConnectStore connectStore;
    @Autowired
    private IProtocolDataService protocolDataService;

    /***************
     * 内置功能请求数据结构
     * {
     *     "deviceModelId": "1252595618989654018"
     *     "deviceInstanceId": "1254039235352555521",
     *     "function": [
     *         {
     *             "id": "1253971247969107970"
     *             "name": "请求数据",
     *             "code": "requestData",
     *             "inputs": [
     *                 {
     *                     "code": "requestCmd",
     *                     "dataId": "1253970109857300481",
     *                     "inputMode": "1",
     *                     "valueType": {
     *                         "arrayType": "byte",
     *                         "arraySize": 8,
     *                         "type": "array"
     *                     },
     *                     "name": "请求命令",
     *                     "id": "1253970109857300481",
     *                     "deviceModelBy": "1252595618989654018",
     *                     "rwAuthor": "w",
     *                     "value": "01 03 00 00 89 67"
     *                 }
     *             ],
     *             "output": "",
     *         }
     *     ],
     * }
     */

    /**
     * 编码
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {

        // 获取channel
        Channel channel = ctx.channel();
        // 客户端地址
        String addr = channel.remoteAddress().toString().substring(1);

        // 根据tcp地址(host:port)拿到连接信息
        Map<String, NetworkConnect> connectMap = NetworkConnectStore.getNetworkConnectMap();
        NetworkConnect connect = connectMap.get(addr);
        // 连接设备模型
        String deviceModelId = connect.getDeviceInstance().getModelBy();


        // 验证数据协议
        ProtocolData protocolData = tcpDynamicEncoder.protocolDataService.getDeviceDataProtocol(deviceModelId);

        if (!"private".equals(protocolData.getCode())) {
            // 自定义协议
            DataProtocolProvider protocolProvider = tcpDynamicEncoder.protocolDataService.loadDataProtocolProvider(protocolData);
            ///////

            JSONObject msgObj = JSONObject.parseObject(msg.toString());
            JSONObject functionObj = msgObj.getJSONObject("function");
            String funcCode = functionObj.getString("code");
            // 请求数据
            if ("requestData".equals(funcCode)) {
                String value = functionObj.getJSONArray("inputs").getJSONObject(0).getString("value");
                log.info("requestData: {}", value);
                out.add(Unpooled.wrappedBuffer(HexConvertUtil.hexStringToBytes(value)));
            }
            // 调用自定义编码
//            protocolProvider.encoder(ctx, msgObj, out);
        } else {
            // 私有协议，直接发送
            out.add(JSONObject.parseObject(String.valueOf(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), CharsetUtil.UTF_8))));
        }

    }

    @PostConstruct
    public void init() {
        tcpDynamicEncoder = this;
        // this.publisher = this.publisher;
        tcpDynamicEncoder.protocolDataService = this.protocolDataService;
    }

}
