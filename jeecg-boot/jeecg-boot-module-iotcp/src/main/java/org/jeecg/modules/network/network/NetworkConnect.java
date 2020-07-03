package org.jeecg.modules.network.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.device.entity.DeviceInstance;

import java.net.SocketAddress;
import java.util.Date;

@Data
public class NetworkConnect {

    public NetworkConnect() {
        super();
    }

    /**
     *
     * @param registTime
     * @param unregistTime
     * @param socketAddress
     * @param networkType
     * @param action
     */
    public NetworkConnect(long registTime, long unregistTime, SocketAddress socketAddress, NetworkType networkType, int action) {
        this.registTime = registTime;
        this.unregistTime = unregistTime;
        this.socketAddress = socketAddress;
        this.networkType = networkType;
        this.action = action;
    }

    public NetworkConnect(String deviceId, String deviceName, long registTime, long unregistTime, SocketAddress socketAddress, NetworkType networkType, int action) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.registTime = registTime;
        this.unregistTime = unregistTime;
        this.socketAddress = socketAddress;
        this.networkType = networkType;
        this.action = action;
    }

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 网络接入时间
     */
    private long registTime;

    /**
     * 网络注销时间
     */
    private long unregistTime;

    /**
     * 网络接入地址-客户端
     */
    private SocketAddress socketAddress;

    /**
     * 网络类型
     */
    private NetworkType networkType;

    /**
     * 网络行为：1-注册、0-注销
     */
    private int action;

    private ChannelHandlerContext channelHandlerContext;

    private DeviceInstance deviceInstance;

    private Network network;

    /**
     *
     */
    // private


    @Override
    public String toString() {
        return "\n----------------------------------------\n" +
                "| 类型: " + this.networkType.getName() + "                          |\n" +
                "| 时间: " + DateUtils.formatDate(new Date(this.registTime), "yyyy-MM-dd HH:mm:ss") + "               |\n" +
                "| 地址: " + this.socketAddress.toString() + "              |\n" +
                "------------------------------------------";
    }
}
