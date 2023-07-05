package com.bfxx.redis.config;

import com.bfxx.dao.EndpointStatusDao;
import com.bfxx.pojo.EndNode;
import com.bfxx.services.impl.EndpointServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    EndpointServiceImpl endpointService;
    @Autowired
    EndpointStatusDao endpointStatusDao;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("修改过期key:" + message.toString()+"状态为”false“");
        EndNode endNode = endpointStatusDao.statusGet("Bak_"+message.toString());
        endNode.setOnlineStatus(false);
        endpointStatusDao.statusSetNoExpire(message.toString(), endNode);
        String url =  "http://127.0.0.1:8080/update/offline/"+message.toString();
        String s = restTemplate.getForObject(url, String.class);
        System.out.println("==========================="+s+"===========================");

    }
}
