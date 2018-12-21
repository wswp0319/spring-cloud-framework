package net.menwei.consumer.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import net.menwei.consumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    /**
     * 服务调用 消费者
     *
     * @param name
     * @return
     */
    @GetMapping("/sayHello/{name}")
    @PostMapping("/sayHello/{name}")
//    @HystrixCommand(fallbackMethod = "error", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "4000")
//    }, threadPoolProperties = {
//            @HystrixProperty(name = "coreSize", value = "1"),
//            @HystrixProperty(name = "maxQueueSize", value = "10"),
//            @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
//            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "80"),
//            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "120"),
//            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
//    })
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")})
    public String sayHello(@PathVariable("name") String name) {
        return helloService.sayHello(name);
    }

    public String error(String name) {
        return "fuck";
    }
}
