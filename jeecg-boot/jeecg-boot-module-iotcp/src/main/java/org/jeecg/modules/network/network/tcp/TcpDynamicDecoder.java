package org.jeecg.modules.network.network.tcp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhouwr.protocol.DataProtocolProvider;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.message.entity.ReceiveData;
import org.jeecg.modules.message.enums.MessageType;
import org.jeecg.modules.network.network.NetworkConnect;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.jeecg.modules.protocol.entity.ProtocolData;
import org.jeecg.modules.protocol.service.IProtocolDataService;
import org.jeecg.utils.CRCUtil;
import org.jeecg.utils.HexConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.HashMap;
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
public class TcpDynamicDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static TcpDynamicDecoder tcpDynamicDecoder;
    @Autowired
    private NetworkConnectStore connectStore;
    @Autowired
    private IProtocolDataService protocolDataService;

    //將10進制轉換為16進制
    public static String encodeHEX(Integer numb) {

        String hex = Integer.toHexString(numb);
        return hex;

    }

    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    ////////

    public static void main(String[] args) {
        int numb = 580;
        String hex = encodeHEX(numb);
        System.out.println(numb + "：16进制为" + hex);
        System.out.println("16進制字符 " + hex + " 的10進制數字為   " + decodeHEX(hex));

        System.out.println(String.valueOf(0xF4 & 0xff));
        System.out.println(decodeHEX("F4"));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        log.info("decode 接收数据：\r\n{}", HexConvertUtil.BinaryToHexString(ByteBufUtil.getBytes(msg)));
        // 获取channel
        Channel channel = ctx.channel();
        // 客户端地址
        String addr = channel.remoteAddress().toString().substring(1);

        // 根据tcp地址(host:port)拿到连接信息
        Map<String, NetworkConnect> connectMap = NetworkConnectStore.getNetworkConnectMap();
        NetworkConnect connect = connectMap.get(addr);
        // 连接设备模型
        String deviceModelId = connect.getDeviceInstance().getModelBy();
        String deviceInstanceId = connect.getDeviceInstance().getId();

        ProtocolData protocolData = tcpDynamicDecoder.protocolDataService.getDeviceDataProtocol(deviceModelId);

        // 验证数据协议
        if (!"private".equals(protocolData.getCode())) {
            // 自定义协议
            DataProtocolProvider protocolProvider = tcpDynamicDecoder.protocolDataService.loadDataProtocolProvider(protocolData);
            // Object object = protocolProvider.decoder(msg);

            /////
            byte[] bytes = ByteBufUtil.getBytes(msg);
            System.out.println(HexConvertUtil.BinaryToHexString(bytes));
            if (MessageType.HEART.getCode().equals(new String(bytes))) {
                ReceiveData receiveData = new ReceiveData(
                        MessageType.HEART.getCode(),
                        deviceModelId,
                        deviceInstanceId,
                        System.currentTimeMillis(),
                        null);
                out.add(receiveData);

            } else {
                int len = bytes.length;
                byte address = bytes[0]; // 设备地址
                int regNum = (bytes[2] >> 4 & 0x0f) * 16 + (bytes[2] & 0x0f); // 寄存器数量
                // 数据
                byte[] data = new byte[regNum];
                System.arraycopy(bytes, 3, data, 0, regNum);

                // 原报文CRC字符
                byte[] c = {bytes[len - 2], bytes[len - 1]};
                String srcCRC = HexConvertUtil.BinaryToHexString(c);
                // 报文内容计算CRC字符
                byte[] b = new byte[len - 2]; // 数据
                System.arraycopy(bytes, 0, b, 0, len - 2);
                String destCRC = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(b)));
                if (srcCRC.equals(destCRC) || true) {
                    JSONObject dJson = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < regNum - 4; i += 2) { // regNum-4 舍去最高温2位，舍去报警状态2位// FC19
                        int t = Integer.parseInt(String.valueOf((data[i] & 0xff) * 256 + (data[i + 1] & 0xff)));
                        if (t == 64537)
                            continue;
                        t = t <= 32767 ? t : t - 65536;
                        Map<String, Object> dataMap = new HashMap<>();
                        dataMap.put("temperature", t / 10.0);
                        ReceiveData receiveData = new ReceiveData(
                                MessageType.REPORT_DATA.getCode(),
                                deviceModelId,
                                "device-" + ((i + 2) / 2),
                                System.currentTimeMillis(),
                                dataMap);
                        out.add(receiveData);
                    }
                } else {
                    log.error("CRC校验错误！");
                }
            }
            /////


        } else {
            out.add(ByteBufUtil.getBytes(msg));
        }
    }

    @PostConstruct
    public void init() {
        tcpDynamicDecoder = this;
        // this.publisher = this.publisher;
        tcpDynamicDecoder.protocolDataService = this.protocolDataService;
    }

}
