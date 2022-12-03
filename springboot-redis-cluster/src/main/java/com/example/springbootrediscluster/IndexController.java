package com.example.springbootrediscluster;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/sentinel")
    public void testSentinel() throws InterruptedException{
        int i=1;
        while(true){
            try{
                stringRedisTemplate.opsForValue().set("zhangsan_"+i,i+"");
                System.out.println("设置key: "+ "zhangsan_"+i);
                i++;
                Thread.sleep(1000);
            }catch (Exception e){
                logger.error("错误: ",e);
            }
        }

    }
}
