package com.zhouwr.protocol;


import com.alibaba.fastjson.JSONArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;


/**
 * 文件同步
 * jeecg-boot-module-iotcp/src/main/java/org/jeecg/modules/network/network/DataProtocolProvider.java
 */
public interface DataProtocolProvider {

    public JSONArray decoder(ByteBuf msg) throws Exception;

    public void encoder(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception;
}
