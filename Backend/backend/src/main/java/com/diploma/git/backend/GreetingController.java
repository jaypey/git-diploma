package com.diploma.git.backend;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.diploma.git.backend.mapper.UserMapper;
import com.diploma.git.backend.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    UserMapper userMapper;
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        List<Users> userList = userMapper.findAll();
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}