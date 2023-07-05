package com.bfxx.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

//@Configuration
//@EnableRedisRepositories
//public class RedisConfig {
//
//
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(){
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        return redisMessageListenerContainer;
//    }
//
//    @Bean
//    public KeyExpiredListener keyExpiredListener() {
//        return new KeyExpiredListener(this.redisMessageListenerContainer());
//    }
//
//
//    @Bean
//    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
//    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate();
//        template.setConnectionFactory(lettuceConnectionFactory);
//        // 使用JSON格式序列化对象，对缓存数据key和value进行转换
//        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
//
//        // 解决查询缓存转换异常的问题
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSeial.setObjectMapper(om);
//        // 设置RedisTemplate模板API的序列化方式为JSON
//        template.setDefaultSerializer(jacksonSeial);
//        return template;
//    }
//
//}


/*redis监听过期时间设置*/
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }

    @Bean
    public KeyExpiredListener keyExpiredListener() {
        return new KeyExpiredListener(this.redisMessageListenerContainer());
    }
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return template;

    }


}





