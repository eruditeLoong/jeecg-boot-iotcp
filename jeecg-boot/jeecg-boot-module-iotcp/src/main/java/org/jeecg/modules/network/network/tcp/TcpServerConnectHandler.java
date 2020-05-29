package org.jeecg.modules.network.network.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.network.NetworkConnectStore;
import org.jeecg.modules.network.network.NetworkType;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Slf4j
@Component
public class TcpServerConnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        SocketAddress addr = ctx.channel().remoteAddress();
        NetworkConnectStore.addConnect(ctx, NetworkType.TCP_CLIENT);

        log.info(">>>>>>>>>>>>> 连接注册 <<<<<<<<<<<<<<<\n\t{}, {}", ctx.channel().localAddress(), addr.toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress adds = channel.remoteAddress();
        NetworkConnectStore.removeConnect(adds);
        log.info(">>>>>>>>>>>>> 连接注销 <<<<<<<<<<<<<<<\n\t{}", adds);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        log.warn("心跳超时, {}, {}", ctx, evt);
    }
}
