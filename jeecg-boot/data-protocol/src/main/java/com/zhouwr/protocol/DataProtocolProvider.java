package com.zhouwr.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;


public interface DataProtocolProvider {

    Object decoder(ByteBuf msg);

    void encoder(ChannelHandlerContext ctx, Object msg, List<Object> out);
}
