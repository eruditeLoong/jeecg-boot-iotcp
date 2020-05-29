package org.jeecg.modules.network.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.network.entity.NetworkService;
import org.jeecg.modules.network.service.INetworkServiceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NetworkManager implements BeanPostProcessor, CommandLineRunner {

    @Autowired
    private INetworkServiceService networkServiceService;

    // 网络组件：NettyServiceI 注册的bean
    private Map<String, Network> networkServiceMap = new ConcurrentHashMap<>();
    private Map<String, NetworkProvider<Object>> networkProviderMap = new ConcurrentHashMap<>();

    public Map<String, Network> getServiceList(){
        return this.networkServiceMap;
    }

    /**
     *
     * @param type
     * @param id
     */
    public void reload(NetworkService networkService) {
        String addr = networkService.getHost() + ":" + networkService.getPort();

        Network nettyService = this.networkServiceMap.computeIfAbsent(addr, k -> null);
        if(nettyService == null){
            networkServiceMap.put(addr, createNetworkService(networkService));
        }
        System.out.println("网络服务Map："+networkServiceMap.toString());

    }

    public void shutdown(NetworkService networkService) {
        String addr = networkService.getHost() + ":" + networkService.getPort();
        Network nettyService = this.networkServiceMap.computeIfAbsent(addr, k -> null);
        if (nettyService != null) {
            nettyService.shutdown();
        }
        networkServiceMap.remove(addr);
        System.out.println(networkServiceMap.toString());
    }

    /**
     * 创建网络服务
     * @param networkService
     * @return
     */
    protected Network createNetworkService(NetworkService networkProperties) {
        NetworkProvider<Object> provider = networkProviderMap.get(networkProperties.getType());
        // 判断网络组件是否已经加载
        if(provider == null){
            log.error("网络组件类型：{}不支持", networkProperties.getType());
            throw new RuntimeException("网络组件类型："+ networkProperties.getType() + "不支持");
        }else {
            String addr = networkProperties.getHost() + ":" + networkProperties.getPort();
            Network network = networkServiceMap.computeIfAbsent(addr, id -> null);
            if (network == null) {
                network = provider.createNetwork(networkProperties);
            } else {
                //单例，已经存在则重新加载
                provider.reload(network, networkProperties);
            }
            return network;
        }
    }

    /**
     * 根据数据表中数据，启动相应服务
     */
    protected void checkNetwork() {
        // System.out.println("checkNetwork......");
        List<NetworkService> networkProperties = networkServiceService.lambdaQuery().list();
        networkProperties.forEach(networkPropertie -> {
            if(networkPropertie.getState()) {
                String addr = networkPropertie.getHost() + ":" + networkPropertie.getPort();
                networkServiceMap.put(addr, createNetworkService(networkPropertie));
            }
        });
    }

    protected void register(NetworkProvider<Object> provider) {
        log.info(">>>>>>>>> 注册网络通信组件："+provider.getType().getName() + " <<<<<<<<<<");
        this.networkProviderMap.put(provider.getType().getCode(), provider);
    }

    /**
     * springboot启动过程中，注册的bean
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NetworkProvider) {
            this.register(((NetworkProvider) bean));
        }
        return bean;
    }

    /**
     * springboot 启动完成时，执行操作
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>>> 网络服务初始化-开始 <<<<<<<<<<<<<<<<");
        this.checkNetwork();
        log.info(">>>>>>>>>>>>>>>> 网络服务初始化-结束 <<<<<<<<<<<<<<<<");
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Synchronization implements Serializable {
        private NetworkType type;
        private String id;
    }
}
