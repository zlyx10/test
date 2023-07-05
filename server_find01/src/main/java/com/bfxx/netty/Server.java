package com.bfxx.netty;


import com.bfxx.ioconfig.ReadConfig;
import com.bfxx.pojo.EndNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.io.IOException;

@Component
@Data
public class Server {


    private static Jedis jedis;




    public static class SingletionWServer{
        static Server instance;

        static {
            try {
                instance = new Server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Server getInstance(){
        return SingletionWServer.instance;
    }

    private EventLoopGroup mainGroup ;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public Server() throws IOException {

        String reidsIP = ReadConfig.readConfig(Server.class, "my.properties").getProperty("redis.ip");
        String redisPassword = ReadConfig.readConfig(Server.class, "my.properties").getProperty("redis.password");
        int redisPort =Integer.parseInt(ReadConfig.readConfig(Server.class, "my.properties").getProperty("redis.port")) ;


        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                EndNode endpoint = new ObjectMapper().readValue(msg.toString(), EndNode.class);
                                RestTemplate restTemplate = new RestTemplate();

//"@class":"com.bfxx.pojo.EndPoint",
                                jedis = new Jedis(reidsIP, redisPort);
                                jedis.auth(redisPassword);
                                jedis.select(0);
                                StringBuffer stringBuilder1=new StringBuffer(msg.toString());
                                StringBuffer insert = stringBuilder1.insert(1, "\"@class\":\"com.bfxx.pojo.EndNode\",");
                                String value = insert.toString();
                                if(!(jedis.exists(endpoint.getName()))){
                                    jedis.setex(endpoint.getName(),5, value);
                                    jedis.set("Bak_"+endpoint.getName(),value);

                                    String url =  "http://127.0.0.1:8080/update/online/"+endpoint.getName();
                                    String s = restTemplate.getForObject(url, String.class);
                                    System.out.println("==========================="+s+"===========================");
                                }else {

                                    String s = jedis.get(endpoint.getName());
                                    char onlineStatus1 = s.charAt(s.indexOf("onlineStatus")+14);
                                    if(onlineStatus1 =='t'){
                                        jedis.setex(endpoint.getName(), 5, value);
                                        jedis.set("Bak_" + endpoint.getName(), value);
                                    }else {
                                        jedis.setex(endpoint.getName(), 5, value);
                                        jedis.set("Bak_" + endpoint.getName(), value);
                                        String url1 =  "http://127.0.0.1:8080/update/oldnode_online/"+endpoint.getName();
                                        String s1 = restTemplate.getForObject(url1, String.class);
                                        System.out.println("==========================="+s1+"===========================");
                                    }




                                }
                            }
                        });
                    }
                });
    }
    public void start(){
        future = this.server.bind(9999);
        System.err.println("netty 服务端启动完毕 .....");
    }

}
