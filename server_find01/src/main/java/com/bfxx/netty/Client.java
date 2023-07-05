package com.bfxx.netty;

import com.bfxx.common.CommonResult;
import com.bfxx.ioconfig.ReadConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class Client {

    private static Jedis jedis;

    public static class SingletionWClient{
        static Client instance;

        static {
            try {
                instance = new Client();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Client getInstance(){
        return Client.SingletionWClient.instance;
    }

    private EventLoopGroup mainGroup ;

    private Bootstrap client;
    private ChannelFuture future;

    public Client() throws InterruptedException, IOException {
//        String serverIP = ReadConfig.readConfig(Client.class,"my.properties").getProperty("otherpointIP");
        String fserverIP = ReadConfig.readConfig(Client.class, "my.properties").getProperty("otherpointIP");
        int fNettyServerPort = Integer.parseInt(ReadConfig.readConfig(Client.class, "my.properties").getProperty("otherpointNettyPort"));
        int sendingIntervalTime = Integer.parseInt(ReadConfig.readConfig(Client.class, "my.properties").getProperty("sendingIntervalTime"));



        RestTemplate restTemplate = new RestTemplate();
        mainGroup = new NioEventLoopGroup();

        client = new Bootstrap();
        Channel localhost = client.group(mainGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());

                    }

                    //TODO:这里调用外部接口获取父节点地址
                }).connect(new InetSocketAddress(fserverIP, fNettyServerPort)).sync().channel();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask(){

            @Override
            public void run() {

                String url1 =  "http://127.0.0.1/endnode/all";
                CommonResult s1 = restTemplate.getForObject(url1, CommonResult.class);
                localhost.writeAndFlush(s1);
            }
        };

        timer.schedule(timerTask, 1000,sendingIntervalTime);
    }
    public void start(){

        System.err.println("netty 客户端启动完毕 .....，正在向父节点发所有末端信息");
    }
}
