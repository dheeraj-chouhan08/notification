package com.assignment.notification.config;

import redis.clients.jedis.Jedis;

public class AppConfig {

    public Jedis jedis(){
        return new Jedis();
    }
}
