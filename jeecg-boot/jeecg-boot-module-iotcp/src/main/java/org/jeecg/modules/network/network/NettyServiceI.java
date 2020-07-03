package org.jeecg.modules.network.network;

import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.network.tcp.NettyTcpServer;

/**
 * network 服务接口
 */
public interface NettyServiceI<T> {

    public NettyTcpServer config(NetworkService networkService);

    public Boolean start();

    public Boolean stop();

    public NetworkType getType();
}
