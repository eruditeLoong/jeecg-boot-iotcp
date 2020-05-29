package org.jeecg.modules.network.network.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
//@ChannelHandler.Sharable
public class TcpServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final EventExecutorGroup group;

    public TcpServerChannelInitializer(int nThreads) {
        group = new DefaultEventExecutorGroup(nThreads);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(10, 0, 0, TimeUnit.MINUTES));
        // netty基于分割符的自带解码器，根据提供的分隔符解析报文，这里是0x7e;1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
//        pipeline.addLast(
//                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[] { 0x7e }),
//                        Unpooled.copiedBuffer(new byte[] { 0x7e })));
        //自定义编解码器
        pipeline.addLast(new TcpDynamicEncoder());
        // pipeline.addLast(new TcpStringEncoder());
        pipeline.addLast(new TcpDynamicDecoder());
        // pipeline.addLast(new ReadTimeoutHandler(60));//设置超时时间
        // 自定义Hander,可用于处理耗时操作，不阻塞IO处理线程
        pipeline.addLast(group, "TcpServerConnectHandler", new TcpServerConnectHandler());
        pipeline.addLast(group, "TcpServerDataDealHandler", new TcpServerDataDealHandler());
    }
}
