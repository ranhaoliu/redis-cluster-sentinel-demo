package com.example.redisbootsentinel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //测试哨兵模式下访问，当故意弄坏master结点，验证程序后面还能不能访问到新的master结点
    @RequestMapping("/sentinel")
    public void testSentinel() throws InterruptedException{
        int i=1;
        while(true){
            try{
                stringRedisTemplate.opsForValue().set("zhangsan_sentinel"+i,i+"");
                System.out.println("设置key: "+ "zhangsan_sentinel"+i);
                i++;
                Thread.sleep(1000);
            }catch (Exception e){
                logger.error("错误: ",e);
            }
        }

    }
}
