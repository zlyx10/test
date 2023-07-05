package com.bfxx.services;

import com.bfxx.pojo.EndNode;

import java.util.ArrayList;

public interface EndpointService {

    void saveEndpoint(String key,Object value);

    ArrayList<EndNode> getAllEndpoint();


    EndNode getEndpoint(String key);
}
