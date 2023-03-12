package com.highload.feign.reactive_client;


import com.highload.feign.dto.UserDto;
import com.highload.feign.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@ReactiveFeignClient(name = "${user.service.app.name}", path = "${user.service.context.path")
@Component
public interface UserClient {

    @GetMapping("/{user_id}")
    public User getUserById(@PathVariable(name = "user_id") UUID user_id);

}
