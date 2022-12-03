package org.example;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterTest {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(5);
        config.setMaxTotal(20);
        config.setMaxIdle(10);

        Set<HostAndPort> jedisClusterNode  = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.186.134",8002));
        jedisClusterNode.add(new HostAndPort("192.168.186.134",8005));
        jedisClusterNode.add(new HostAndPort("192.168.186.135",8001));
        jedisClusterNode.add(new HostAndPort("192.168.186.135",8004));
        jedisClusterNode.add(new HostAndPort("192.168.186.136",8006));
        jedisClusterNode.add(new HostAndPort("192.168.186.136",8003));
        try {
            JedisCluster jedisCluster = null;
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, "123456", config);
            System.out.println(jedisCluster.set("cluster", "zhuge"));
            System.out.println(jedisCluster.get("cluster"));
        }catch (Exception e){
            System.out.println("错误"+e);
        }
    }
}
