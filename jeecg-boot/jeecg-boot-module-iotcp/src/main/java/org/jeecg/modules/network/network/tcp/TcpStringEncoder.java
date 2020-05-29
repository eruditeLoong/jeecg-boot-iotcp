package org.jeecg.modules.network.network.tcp;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @program: jeecg-boot-module-iot
 * @description: Tcp字符编码
 * @author: zhouwr
 * @create: 2020-05-27 13:59
 * @version：1.0
 **/
@Sharable
public class TcpStringEncoder extends MessageToMessageEncoder<CharSequence> {
    private final Charset charset;

    public TcpStringEncoder() {
        this(Charset.defaultCharset());
    }

    public TcpStringEncoder(Charset charset) {
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
    }

    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        System.out.println("======>>>>>>>>>" + msg);
        if (msg.length() != 0) {
            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), this.charset));
        }
    }
}

