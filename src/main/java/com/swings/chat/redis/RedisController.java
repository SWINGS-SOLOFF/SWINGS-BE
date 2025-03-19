package com.swings.chat.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/set")
    public String setRedisData(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value);
        return "데이터 저장 완료!";
    }

    @GetMapping("/get")
    public String getRedisData(@RequestParam String key) {
        return redisService.getData(key);
    }
}

