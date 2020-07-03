package com.zhouwr.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class WIRELESS120 implements DataProtocolProvider, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public Object decoder(ByteBuf msg) {
        final byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        msg.getBytes(msg.readerIndex(), array, 0, length);
        return new String(array);
    }

    @Override
    public void encoder(ChannelHandlerContext ctx, Object msg, List<Object> out) {
        out.add(msg);
    }

}
