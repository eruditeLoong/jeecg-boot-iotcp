package org.jeecg.modules.network.network.tcp;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.network.Network;
import org.jeecg.modules.network.network.NetworkProvider;
import org.jeecg.modules.network.network.NetworkType;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Slf4j
public class TcpServerProvider implements NetworkProvider {
    // private final EventLoopGroup mainGroup;
    // private final EventLoopGroup workerGroup;
    // private final ServerBootstrap server;
    // private final ChannelFuture future;
    //
    // public TcpServerProvider(EventLoopGroup mainGroup, EventLoopGroup workerGroup, ServerBootstrap server, ChannelFuture future){
    //     this.mainGroup = mainGroup;
    //     this.workerGroup = workerGroup;
    //     this.server = server;
    //     this.future = future;
    // }

    @Nonnull
    @Override
    public NetworkType getType() {
        return NetworkType.TCP_SERVER;
    }

    // private void initTcpServer(NettyTcpServer tcpServer, NetworkService properties) {
    //     int instance = Math.max(2, properties.getThreadNum());
    //     for (int i = 0; i < instance; i++) {
    //         instances.add(vertx.createNetServer(properties.getOptions()));
    //     }
    //     payloadParserBuilder.build(properties.getParserType(), properties);
    //     tcpServer.setParserSupplier(() -> payloadParserBuilder.build(properties.getParserType(), properties));
    //     tcpServer.setServer(instances);
    //     tcpServer.setKeepAliveTimeout(properties.getLong("keepAliveTimeout", Duration.ofMinutes(10).toMillis()));
    //     for (NetServer netServer : instances) {
    //         netServer.listen(properties.createSocketAddress(), result -> {
    //             if (result.succeeded()) {
    //                 log.info("tcp server startup on {}", result.result().actualPort());
    //             } else {
    //                 log.error("startup tcp server error", result.cause());
    //             }
    //         });
    //     }
    // }

    @Nonnull
    @Override
    public NettyTcpServer createNetwork(@Nonnull NetworkService properties) {
        NettyTcpServer tcpServer = new NettyTcpServer(properties);
        tcpServer.initTcpServer();
        return tcpServer;
    }

    @Override
    public void reload(@Nonnull Network network, @Nonnull NetworkService properties) {
        NettyTcpServer tcpServer = ((NettyTcpServer) network);
        tcpServer.shutdown();
        tcpServer.initTcpServer();
    }
}
