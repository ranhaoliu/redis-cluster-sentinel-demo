package org.example;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelTest {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(5);
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        String masterName = "mymaster";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(new HostAndPort("192.168.186.134",26379).toString());
        sentinels.add(new HostAndPort("192.168.186.134",26380).toString());
        sentinels.add(new HostAndPort("192.168.186.134",26381).toString());

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName,sentinels,config,3000,null);
        Jedis jedis = null;
        jedis = jedisSentinelPool.getResource();
        System.out.println(jedis.set("sentinel1666","zhuge"));
        System.out.println(jedis.get("sentinel1666"));
    }
}
