package com.bfxx.services.impl;

import com.bfxx.dao.EndpointStatusDao;
import com.bfxx.pojo.EndNode;
import com.bfxx.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class EndpointServiceImpl implements EndpointService {
    @Autowired
    EndpointStatusDao endpointStatusDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void saveEndpoint(String key,Object value) {

        endpointStatusDao.statusSet(key,value);
    }

    @Override
    public ArrayList<EndNode> getAllEndpoint() {
        Object pattern = "h".concat("*");
        Set<String> keys = redisTemplate.keys(pattern);


        ArrayList<EndNode> endNodes = new ArrayList<>();
        EndNode endNode = new EndNode();
        for (String key:keys) {
            endNode = endpointStatusDao.statusGet(key);
            endNodes.add(endNode);
        }
        return endNodes;

    }

    @Override
    public EndNode getEndpoint(String key) {


        return endpointStatusDao.statusGet(key);
    }

}
