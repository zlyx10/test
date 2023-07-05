package com.bfxx.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
* 前端连接ws://本服务器ip:8080/notice/userx
*
* 节点下线：当redis中的数据过期  ->  redis监听器发现过期事件 ->  把过期的key的状态改为离线重新存入redis
* ->远程调用"http://127.0.0.1:8080/update/offline/接口 -> 此接口调用ws的send 向前端推节点下线消息
*
* 节点上线：
*
*
*
* */

@ServerEndpoint("/notice/{userId}")
@Component
@Slf4j
public class NoticeWebsocket {

    //记录连接的客户端
    public static Map<String, Session> clients = new ConcurrentHashMap<>();
    //userId关联sid（解决同一用户id，在多个web端连接的问题）
    public static Map<String, Set<String>> conns = new ConcurrentHashMap<>();

    private String sid = null;
    private String userId;



    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.sid = UUID.randomUUID().toString();
        this.userId = userId;
        clients.put(this.sid, session);

        Set<String> clientSet = conns.get(userId);
        if (clientSet==null){
            clientSet = new HashSet<>();
            conns.put(userId,clientSet);
        }
        clientSet.add(this.sid);
        log.info(this.sid + "连接开启！");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info(this.sid + "连接断开！");
        clients.remove(this.sid);
    }

    /**
     * 判断是否连接的方法
     * @return
     */
    public static boolean isServerClose() {
        if (NoticeWebsocket.clients.values().size() == 0) {
            log.info("已断开");
            return true;
        }else {
            log.info("已连接");
            return false;
        }
    }

//    /**
//     * 发送给所有用户
//     * @param noticeType
//     */
//    public static void sendMessage(String noticeType) throws JsonProcessingException {
//        NoticeWebsocketResp noticeWebsocketResp = new NoticeWebsocketResp();
//        noticeWebsocketResp.setNoticeType(noticeType);
//        sendMessage(noticeWebsocketResp);
//    }


    /**
     * 发送给所有用户
     * @param noticeWebsocketResp
     */
    public static void sendMessage(NoticeWebsocketResp noticeWebsocketResp) throws JsonProcessingException {
        String message = new ObjectMapper().writeValueAsString(noticeWebsocketResp);

        for (Session session1 : NoticeWebsocket.clients.values()) {
            try {
                session1.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据用户id发送给某一个用户
     * **/
    public static void sendMessageByUserId(String userId, NoticeWebsocketResp noticeWebsocketResp) throws JsonProcessingException {
        if (!StringUtils.isEmpty(userId)) {
            String message = new ObjectMapper().writeValueAsString(noticeWebsocketResp);
            Set<String> clientSet = conns.get(userId);
            if (clientSet != null) {
                Iterator<String> iterator = clientSet.iterator();
                while (iterator.hasNext()) {
                    String sid = iterator.next();
                    Session session = clients.get(sid);
                    if (session != null) {
                        try {
                            session.getBasicRemote().sendText(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+this.userId+"的信息:"+message);
    }

    /**
     * 发生错误时的回调函数
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        log.info("错误");
        error.printStackTrace();
    }



}
