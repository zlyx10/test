package com.bfxx;


import com.bfxx.dao.EndpointStatusDao;
import com.bfxx.ioconfig.ReadConfig;
import com.bfxx.pojo.EndNode;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Component
@Data
@PropertySource(value = "classpath:application.yaml")
public class redisTest {
    @Autowired
    EndpointStatusDao endpointStatusDao;
    @Autowired
    RedisTemplate redisTemplate;

//    @Test
//    void test1(){
//
//        Object pattern = "h".concat("*");
//        Set<String> keys = redisTemplate.keys(pattern);
//
//
//        ArrayList<EndNode> endNodes = new ArrayList<>();
//        EndNode endNode = new EndNode();
//        for (String key:keys) {
//            endNode = endpointStatusDao.statusGet(key);
//            endNodes.add(endNode);
//        }
//        System.out.println(endNodes);
//    }
//
//    @Test
//    void test2(){
//        System.out.println(endpointStatusDao.statusGet("h1"));
//    }
//
//    @Test
//    void test3() throws IOException {
//
//        Properties properties = ReadConfig.readConfig(this.getClass(), "src/main/resources/my.properties");
//        String property = properties.getProperty("redis.ip");
//        System.out.println(property);
//    }


}
