package org.jeecg.modules.network.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.device.entity.DeviceInstance;
import org.jeecg.modules.device.service.IDeviceInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class NetworkConnectStore {

    @Autowired
    private IDeviceInstanceService instanceService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private NetworkManager networkManager;

    private static NetworkConnectStore networkConnectStore;

    private static Map<String, NetworkConnect> store = new HashMap<>();

    public static void addConnect(ChannelHandlerContext ctx, NetworkType networkType) {

        Network network = networkConnectStore.networkManager.getServiceList().get(ctx.channel().localAddress().toString().substring(1));

        NetworkConnect connect = new NetworkConnect();
        connect.setRegistTime(System.currentTimeMillis());  // 注册时间
        connect.setSocketAddress(ctx.channel().remoteAddress());  // 客户端地址
        connect.setNetworkType(networkType);                // 网络类型
        connect.setAction(1);                               // 行为：注册
        connect.setChannelHandlerContext(ctx);                        // 连接通道
        connect.setNetwork(network);                        // 网络服务

        networkConnectStore.resetStore();
        String address = connect.getSocketAddress().toString().substring(1);
        // 1、根据tcp地址查询设备实例，拿到设备实例
        DeviceInstance deviceInstance = networkConnectStore.instanceService.getInstanceDeviceByAddress(address.toString());
        connect.setDeviceInstance(deviceInstance);

        // 2、把连接地址(ip:port)作为map的key写入
        store.put(address, connect);
        log.info("新增连接：{}-{}，连接数量：{}",
                connect.getDeviceInstance() == null ?
                        "获取不到设备实例" :
                        connect.getDeviceInstance().getName(), connect.getSocketAddress(), store.size());

        if (deviceInstance != null) {
            deviceInstance.setStatus("online");
            // 3、发布事件：更新设备状态
            networkConnectStore.publisher.publishEvent(deviceInstance);
        }
    }

    public static void removeConnect(SocketAddress socketAddress) {
        String addr = socketAddress.toString().substring(1);
        NetworkConnect connect = store.get(addr);
        DeviceInstance deviceInstance = connect.getDeviceInstance();

        store.remove(addr);
        log.info("移除连接：{}, 连接数量：{}", addr, store.size());

        if (deviceInstance != null) {
            deviceInstance.setStatus("offline");
            networkConnectStore.publisher.publishEvent(deviceInstance);
        }
    }

    public static Map<String, NetworkConnect> getNetworkConnectMap() {
        return store;
    }

    private void resetStore(){
        Set<String> set = store.keySet();
        for (String adds : set) {
            NetworkConnect connect = store.get(adds);
            if (connect.getChannelHandlerContext() == null || !connect.getChannelHandlerContext().channel().isRegistered()) {
                store.remove(adds);
            }
        }
    }

    @PostConstruct
    public void init() {
        networkConnectStore = this;
        networkConnectStore.instanceService = this.instanceService;
        networkConnectStore.publisher = this.publisher;
        networkConnectStore.networkManager = this.networkManager;
    }
}
