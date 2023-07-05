package com.bfxx.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class NoticeWebsocketResp<T> {

    private String noticeType;
    private T noticeInfo;


}
