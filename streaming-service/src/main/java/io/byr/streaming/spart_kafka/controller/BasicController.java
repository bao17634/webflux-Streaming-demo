package io.byr.streaming.spart_kafka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @ClassName: BasicController
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/28 19:05
 */

@RestController
public class BasicController {
    @GetMapping("/hello_world")
    public Mono<String> sayHelloWorld() {
        return Mono.just("Hello World");
    }
}
