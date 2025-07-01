package com.example.tdgameserver.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameServer {
    private final int port = 8888; // 游戏服务器端口

    @PostConstruct
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new GameMessageDecoder(),    // 消息解码器
                                    new GameMessageEncoder(),    // 消息编码器
                                    new GameServerHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口并启动服务器
            bootstrap.bind(port).sync();
            log.info("游戏服务器启动成功，监听端口: {}，已启用身份验证", port);
        } catch (Exception e) {
            log.error("游戏服务器启动失败", e);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}