package com.zhouwr.protocol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhouwr.protocol.utils.CRCUtil;
import com.zhouwr.protocol.utils.HexConvertUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class WIRELESS120 implements DataProtocolProvider, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public JSONArray decoder(ByteBuf msg) throws Exception {

        byte[] bytes = ByteBufUtil.getBytes(msg);
        JSONArray array = new JSONArray();

        /* 心跳，抛给后台自己处理 */
        if ("heart".equals(new String(bytes))) {
            return array;
        } else {
            /* 数据 */
            int len = bytes.length;
            // CRC校验

            /* 报文后两位CRC校验值 */
            byte[] c = {bytes[len - 2], bytes[len - 1]};
            String srcCRC = HexConvertUtil.BinaryToHexString(c);
            /* 报文除CRC校验值之前的数据部分 */
            byte[] b = new byte[len - 2];
            System.arraycopy(bytes, 0, b, 0, len - 2);

            /* 报文内容计算CRC字符 */
            String destCRC = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(b)));
            if (!srcCRC.equals(destCRC)) {
                throw new RuntimeException("CRC校验错误！");
            }

            /* 数据字节数量 */
            int regNum = (bytes[2] >> 4 & 0x0f) * 16 + (bytes[2] & 0x0f);

            /* 传感器数据 */
            byte[] data = new byte[regNum];
            System.arraycopy(bytes, 3, data, 0, regNum);

            for (int i = 0; i < regNum - 4; i += 2) { // regNum-4 舍去最高温2位，舍去报警状态2位// FC19
                int t = Integer.parseInt(String.valueOf((data[i] & 0xff) * 256 + (data[i + 1] & 0xff)));
                if (t == 64537)
                    continue;
                t = t <= 32767 ? t : t - 65536;

                JSONObject json = new JSONObject();
                json.put("type", "data");
                json.put("deviceInstanceId", "temperature-" + ((i + 2) / 2));
                json.put("datetime", System.currentTimeMillis());
                JSONObject dataObj = new JSONObject();
                dataObj.put("temperature", t / 10.0);
                dataObj.put("inputMode", "report");
                json.put("dataMap", dataObj);

                array.add(json);
            }
            return array;
        }
    }

    @Override
    public void encoder(ChannelHandlerContext ctx, Object msg, List<Object> out) {

    }

}
