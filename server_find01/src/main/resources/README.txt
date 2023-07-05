netty.server 无法注入 RedisTemplete 故使用jedis。








websocket：
前端连接  ws://本服务器ip:8080/notice/userx

* 节点下线：当redis中的数据过期  ->  redis监听器发现过期事件 ->  把过期的key的状态改为离线重新存入redis
 ->远程调用"http://127.0.0.1:8080/update/offline/{id 接口 -> 此接口调用ws的send 向前端推节点下线消息

* 节点上线：末端节点每隔几秒上传自身节点信息 - 发向服务端的netty.server -
 server判断发送过来的key是否已经存在
 -若不存在
    - 调用"http://127.0.0.1:8080/update/online/{id} 接口
    - 此接口调用ws的send 向前端推节点新节点加入网络消息
 - 若已经存在
    -判断上线状态
       - 若是已下线
         - 调用"http://127.0.0.1:8080/update/oldnode_online/{id} 接口
         - 此接口调用ws的send 向前端推 旧节点上线消息
       - 若本来就是上线状态
         - 更新节点状态消息，无上线消息