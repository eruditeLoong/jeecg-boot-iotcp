package org.jeecg.modules.network.network.tcp;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.network.NettyServiceI;
import org.jeecg.modules.network.network.NetworkType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyTcpClient implements NettyServiceI {

    @Override
    public NettyTcpServer config(NetworkService networkService) {
        return null;
    }

    @Override
    public Boolean start() {
        return null;
    }

    @Override
    public Boolean stop() {
        return null;
    }

    @Override
    public NetworkType getType() {
        return NetworkType.TCP_CLIENT;
    }
}
