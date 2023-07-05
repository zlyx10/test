package com.bfxx.dao;

import com.bfxx.ioconfig.ReadConfig;
import com.bfxx.pojo.EndNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Repository
public class EndpointStatusDao {



    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
    private RedisTemplate<String,Object> redisTemplate;

    public void statusSet(String key,Object ep){


        try {
            int expireTime = Integer.parseInt(ReadConfig.readConfig(EndpointStatusDao.class, "my.properties").getProperty("redis.expireTime"));
            redisTemplate.opsForValue().set(key,ep,expireTime , TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void statusSetNoExpire(String key,Object ep){
        redisTemplate.opsForValue().set(key,ep);

    }

    public EndNode statusGet(String key){
        return (EndNode)redisTemplate.opsForValue().get(key);
    }
    public String statusGetString(String key){
        return redisTemplate.opsForValue().get(key).toString();
    }

    public Boolean isExist(String key){
        return redisTemplate.hasKey(key);
    }

    public Boolean isExpire(String key){
        return expire(key)>0?false:true;
    }

    public  long expire(String key){
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }


}
