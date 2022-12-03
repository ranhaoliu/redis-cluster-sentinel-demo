package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.Arrays;
import java.util.List;

/**
 * JedisTest
 *
 */
public class JedisTest
{
    public static void main( String[] args )
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMinIdle(5);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"192.168.186.134",6379,3000,null);
        Jedis jedis = null;
        jedis = jedisPool.getResource();

        //        System.out.println(jedis.set("zhangsan1", "123456"));//操作字符串

        //1. 不使用pipeline，耗时
        /*
        long start = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            jedis.incr("xiaoming1");
            jedis.set("wang1"+i,"wang");
        }
        long end = System.currentTimeMillis();
        System.out.println("time consuming "+ (end-start)+" ms");//time consuming 10232 ms
        */

        //2.使用pipeline 耗时, 800ms，可以看出和1中不使用pipeline相比要节省了很多时间(将多条命令合并发送给redis server，节省了连接的时间)
/*
        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            pipeline.incr("1pipelineKey");
            pipeline.set("1zhuge"+i,"zhuge");
        }
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("time consuming "+ (end-start)+" ms");//time consuming 855
        System.out.println(results);
*/

        // 3.使用pieline当发生异常或错误时，不支持事务回滚，
       /*
        Pipeline pipeline = jedis.pipelined();
        for(int i=0;i<10;i++){
            pipeline.incr("1pipelineKey");
            pipeline.set("1zhuge"+i,"zhuge");

           pipeline.setbit("zhuge",-1,true);//模拟语法错误
        }
        List<Object> results = pipeline.syncAndReturnAll();
        System.out.println(results);
        */

        //使用lua脚本实现事务回滚

        //在redis-cli 命令窗口 测试敲下命令(2表示的是key的个数):  eval "return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}" 2 key1 key2 first second
       //lua脚本命令执行方式 redis-cli --eval /temp/test.lua ,10
        jedis.set("product_stock_10016","15");
        String script = " local count = redis.call('get',KEYS[1]) "+
                        "local a = tonumber(count) "+
                        "local b = tonumber(ARGV[1]) "+
                        "if a >= b then "+
                        " redis.call('set',KEYS[1],a-b) "+
                        //模拟语法错误回滚操作
                        "bb == 0"+
//                        " return 1 "+
                        " end "+
                        " return 0";
         Object obj = jedis.eval(script, Arrays.asList("product_stock_10016"),Arrays.asList("10") );//script,key,argv
        System.out.println(obj);


        System.out.println( "Hello World! end" );
    }
}
