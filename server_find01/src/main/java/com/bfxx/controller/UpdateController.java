package com.bfxx.controller;

import com.bfxx.common.CommonResult;
import com.bfxx.dao.EndpointStatusDao;
import com.bfxx.pojo.EndNode;
import com.bfxx.websocket.NoticeWebsocket;
import com.bfxx.websocket.NoticeWebsocketResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/update")
public class UpdateController {
    @Resource
    EndpointStatusDao endpointStatusdao;
    @GetMapping("/offline/{id}")
    public CommonResult<String> offLine(@PathVariable("id")String id) throws JsonProcessingException {
        EndNode endNode = endpointStatusdao.statusGet(id);
        String ws_info = new ObjectMapper().writeValueAsString(endNode);
        NoticeWebsocketResp<String> noticeWebsocketResp = new NoticeWebsocketResp<>(id+"末端节点下线", ws_info);
        NoticeWebsocket.sendMessage(noticeWebsocketResp);
        return CommonResult.success(id+"节点下线");
    }

    @GetMapping("/online/{id}")
    public CommonResult<String> onLine(@PathVariable("id")String id) throws JsonProcessingException {
        EndNode endNode = endpointStatusdao.statusGet(id);
        String ws_info = new ObjectMapper().writeValueAsString(endNode);
        NoticeWebsocketResp<String> noticeWebsocketResp = new NoticeWebsocketResp<>("新节点末端"+id+"加入网络", ws_info);
        NoticeWebsocket.sendMessage(noticeWebsocketResp);
        return CommonResult.success(id+"节点加入网络");
    }

    @GetMapping("/oldnode_online/{id}")
    public CommonResult<String> oldNodeOnLine(@PathVariable("id")String id) throws JsonProcessingException {
        EndNode endNode = endpointStatusdao.statusGet(id);
        String ws_info = new ObjectMapper().writeValueAsString(endNode);
        NoticeWebsocketResp<String> noticeWebsocketResp = new NoticeWebsocketResp<>(id+"末端节点上线", ws_info);
        NoticeWebsocket.sendMessage(noticeWebsocketResp);
        return CommonResult.success(id+"节点上线");
    }
}
