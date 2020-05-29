package org.jeecg.modules.network.network;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JSONType(serializeEnumAsJavaBean = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NetworkType {

    TCP_CLIENT("tcp-client", "TCP客户端"),
    TCP_SERVER("tcp-server", "TCP服务"),

    MQTT_CLIENT("mqtt-client", "MQTT客户端"),
    MQTT_SERVER("mqtt-server", "MQTT服务"),

    HTTP_CLIENT("http-client", "HTTP客户端"),
    HTTP_SERVER("http-server", "HTTP服务"),

    WEB_SOCKET_CLIENT("websocket-client", "WebSocket客户端"),
    WEB_SOCKET_SERVER("websocket-server", "WebSocket服务"),

    UDP("udp", "UDP"),

    COAP_CLIENT("coap-client", "CoAP客户端"),
    COAP_SERVER("coap-server", "CoAP服务"),

    ;

    private final String code;
    private final String name;
}
