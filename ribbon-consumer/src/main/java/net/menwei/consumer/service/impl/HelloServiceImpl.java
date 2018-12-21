package net.menwei.consumer.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import net.menwei.consumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class HelloServiceImpl implements HelloService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
//    @Value("${eurekaclientURL}")
//    private String eurekaclientURL;

    @Override
    @HystrixCommand(fallbackMethod = "failHello", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")})
    public String sayHello(String name) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("PROVIDER");
        String server = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        String url = "http://" + server + ":" + port + "/hi/{1}";
        //return restTemplate.getForObject("http://PROVIDER/hi/{1}", String.class, name);
        return new RestTemplate().getForObject(url, String.class, name);
    }

    @Override
    public String failHello(String name) {
        return "服务器内部错误";
    }
}
