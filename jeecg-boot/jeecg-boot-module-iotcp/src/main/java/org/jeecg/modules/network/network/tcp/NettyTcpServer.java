package org.jeecg.modules.network.network.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.network.Network;
import org.jeecg.modules.network.network.NetworkType;

@Slf4j
public class NettyTcpServer implements Network {

    private EventLoopGroup mainGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap server;
    private ChannelFuture future;
    private NetworkService properies;

    public NettyTcpServer() {
        super();
    }

    public NettyTcpServer(NetworkService properies) {
        super();
        this.properies = properies;
    }

    public void initTcpServer() {
        String serviceId = properies.getId();
        int servicePort = properies.getPort();

        // 定义一对线程组
        // 主线程组
        this.mainGroup = new NioEventLoopGroup();
        // 从线程组
        this.workerGroup = new NioEventLoopGroup();

        // netty服务器的创建
        this.server = new ServerBootstrap();

        this.server.group(mainGroup, workerGroup)               // 设置主从服务器
                .channel(NioServerSocketChannel.class)              // 设置nio双向通道
                .childHandler(new TcpServerChannelInitializer(properies.getThreadNum()));
        // 启动server，并设置8088端口号，同时启动方式为同步
        try {
            this.future = server.bind(servicePort).sync();
            this.future.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    //业务逻辑处理代码，此处省略...
                    log.info("tcp-server[{}]链路关闭", future.channel().toString());
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.future.isSuccess()) {
            log.info("tcp-server[{}]启动成功", servicePort);
        } else {
            log.info("tcp-server[{}]启动失败", servicePort);
        }
    }

    @Override
    public String getId() {
        return this.properies.getId();
    }

    @Override
    public NetworkType getType() {
        return NetworkType.TCP_SERVER;
    }

    @Override
    public void shutdown() {
        boolean isStoped = true;
        log.info("shutdown");
        try {
            if(this.future != null) this.future.channel().close().await();
            if(this.mainGroup != null) this.mainGroup.shutdownGracefully().await();
            if(this.workerGroup != null) this.workerGroup.shutdownGracefully().await();
            this.future = null;
            this.mainGroup = null;
            this.workerGroup = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.log.info("tcp-server[{}]服务已经停止...", properies.getPort());
    }

    @Override
    public boolean isAlive() {
        return this.server != null;
    }

    @Override
    public boolean isAutoReload() {
        return false;
    }

}
