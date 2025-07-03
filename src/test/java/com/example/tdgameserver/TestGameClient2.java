package com.example.tdgameserver;

import com.example.tdgameserver.network.GameMessage;
import com.example.tdgameserver.network.GameMessageDecoder;
import com.example.tdgameserver.network.GameMessageEncoder;
import com.example.tdgameserver.network.MessageId;
import com.google.gson.JsonObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestGameClient2 {
    private final String host;
    private final int port;
    private Long playerId;
    private String username;
    private String password;

    public TestGameClient2(String host, int port, Long playerId){
        this.host = host;
        this.port = port;
        this.playerId = playerId;
        // 根据playerId选择测试账号
        setTestCredentials(playerId);
    }

    public TestGameClient2(String host, int port, String username, String password){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    
    private void setTestCredentials(Long playerId) {
        switch (playerId.intValue()) {
            case 1:
                this.username = "1";
                this.password = "123456";
                break;
            case 2:
                this.username = "2";
                this.password = "123456";
                break;
            default:
                this.username = "1";
                this.password = "123456";
                break;
        }
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch){
                            ch.pipeline().addLast(
                                    new GameMessageDecoder(),
                                    new GameMessageEncoder(),
                                    new SimpleChannelInboundHandler<Object>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx,Object msg){
                                            log.info("收到服务器消息:{}",msg);
                                        }

                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx){
                                            // 创建JSON格式的登录数据
                                            JsonObject loginJson = new JsonObject();
                                            loginJson.addProperty("username", username);
                                            loginJson.addProperty("password", password);
                                            
                                            String loginData = loginJson.toString();
                                            GameMessage loginMessage = new GameMessage(MessageId.REQ_LOGIN.getId(), loginData.getBytes());
                                            
                                            ctx.writeAndFlush(loginMessage);
                                            log.info("发送JSON格式登录消息 - 用户名: {}, JSON数据: {}", username, loginData);

                                            ctx.writeAndFlush(new GameMessage(MessageId.REQ_GET_PLAYER_OPERATORS.getId()));
                                        }
                                    }
                            );
                        }

                    });

            //连接服务器
            ChannelFuture future = bootstrap.connect(host,port).sync();
            log.info("连接服务器成功");

            //等待连接关闭
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        // 测试用例1：使用预设账号（根据playerId）
        TestGameClient2 client1 = new TestGameClient2("localhost", 8888, 2L);  // 用户名: 1, 密码: 12345
        
        System.out.println("开始测试JSON格式身份验证...");
        client1.start();
    }
}
