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

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TestGameClientBag {
    private final String host;
    private final int port;
    private Long playerId;
    private String username;
    private String password;
    private Channel channel;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    public TestGameClientBag(String host, int port, Long playerId){
        this.host = host;
        this.port = port;
        this.playerId = playerId;
        // 根据playerId选择测试账号
        setTestCredentials(playerId);
    }

    public TestGameClientBag(String host, int port, String username, String password){
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
                                        protected void channelRead0(ChannelHandlerContext ctx, Object msg){
                                            log.info("收到服务器消息: {}", msg);
                                        }

                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx){
                                            channel = ctx.channel();
                                            isConnected.set(true);
                                            
                                            // 自动发送登录消息
                                            sendLoginMessage();
                                            
                                            // 启动控制台输入监听
                                            startConsoleInput();
                                            
                                            log.info("连接已建立，可以开始输入命令");
                                            printHelp();
                                        }
                                        
                                        @Override
                                        public void channelInactive(ChannelHandlerContext ctx) {
                                            isConnected.set(false);
                                            log.info("连接已断开");
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
    
    /**
     * 发送登录消息
     */
    private void sendLoginMessage() {
        JsonObject loginJson = new JsonObject();
        loginJson.addProperty("username", username);
        loginJson.addProperty("password", password);

        String loginData = loginJson.toString();
        GameMessage loginMessage = new GameMessage(MessageId.REQ_LOGIN.getId(), loginData.getBytes());
        
        channel.writeAndFlush(loginMessage);
        log.info("发送登录消息 - 用户名: {}, JSON数据: {}", username, loginData);
    }
    
    /**
     * 启动控制台输入监听
     */
    private void startConsoleInput() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (isConnected.get()) {
                try {
                    System.out.print("请输入命令: ");
                    String input = scanner.nextLine().trim();
                    
                    if (input.isEmpty()) {
                        continue;
                    }
                    
                    if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                        log.info("用户选择退出");
                        if (channel != null) {
                            channel.close();
                        }
                        break;
                    }
                    
                    if ("help".equalsIgnoreCase(input)) {
                        printHelp();
                        continue;
                    }
                    
                    // 解析命令
                    parseAndSendCommand(input);
                    
                } catch (Exception e) {
                    log.error("处理输入命令时出错", e);
                }
            }
            scanner.close();
        }).start();
    }
    
    /**
     * 解析并发送命令
     */
    private void parseAndSendCommand(String input) {
        String[] parts = input.split(",", 2);
        String command = parts[0].toLowerCase();
        String data = parts.length > 1 ? parts[1] : "";
        
        switch (command) {
            case "1":
                sendLoginMessage();
                break;
            case "2":
                if (data.isEmpty()) {
                    System.out.println("用法: get_backpack <backpack_type_id>");
                    return;
                }
                try {
                    int backpackTypeId = Integer.parseInt(data);
                    sendGetBackpackMessage(backpackTypeId);
                } catch (NumberFormatException e) {
                    System.out.println("背包类型ID必须是数字");
                }
                break;
                
            case "3":
                if (data.isEmpty()) {
                    System.out.println("用法: add_item <item_id> <count>");
                    return;
                }
                String[] addParts = data.split("/");
                if (addParts.length != 2) {
                    System.out.println("用法: add_item <item_id> <count>");
                    return;
                }
                try {
                    int itemId = Integer.parseInt(addParts[0]);
                    int count = Integer.parseInt(addParts[1]);
                    sendAddItemMessage(itemId, count);
                } catch (NumberFormatException e) {
                    System.out.println("道具ID和数量必须是数字");
                }
                break;
                
            case "4":
                if (data.isEmpty()) {
                    System.out.println("用法: use_item <item_id> <count>");
                    return;
                }
                String[] useParts = data.split("/");
                if (useParts.length != 2) {
                    System.out.println("用法: use_item <item_id> <count>");
                    return;
                }
                try {
                    int itemId = Integer.parseInt(useParts[0]);
                    int count = Integer.parseInt(useParts[1]);
                    sendUseItemMessage(itemId, count);
                } catch (NumberFormatException e) {
                    System.out.println("道具ID和数量必须是数字");
                }
                break;
            default:
                System.out.println("未知命令: " + command);
                printHelp();
                break;
        }
    }

    /**
     * 发送获取背包消息
     */
    private void sendGetBackpackMessage(int backpackTypeId) {
        JsonObject json = new JsonObject();
        json.addProperty("backpackTypeId", backpackTypeId);
        
        GameMessage message = new GameMessage(MessageId.REQ_GET_BACKPACK_BY_TYPE.getId(), json.toString().getBytes());
        channel.writeAndFlush(message);
        log.info("发送获取背包消息 - 背包类型: {}", backpackTypeId);
    }
    
    /**
     * 发送添加道具消息
     */
    private void sendAddItemMessage(int itemId, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("itemId", itemId);
        json.addProperty("count", count);
        
        GameMessage message = new GameMessage(MessageId.REQ_ADD_ITEM.getId(), json.toString().getBytes());
        channel.writeAndFlush(message);
        log.info("发送添加道具消息 - 道具ID: {}, 数量: {}", itemId, count);
    }
    
    /**
     * 发送使用道具消息
     */
    private void sendUseItemMessage(int itemId, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("itemId", itemId);
        json.addProperty("count", count);
        
        GameMessage message = new GameMessage(MessageId.REQ_USE_ITEM.getId(), json.toString().getBytes());
        channel.writeAndFlush(message);
        log.info("发送使用道具消息 - 道具ID: {}, 数量: {}", itemId, count);
    }
    
    /**
     * 打印帮助信息
     */
    private void printHelp() {
        System.out.println("\n=== 可用命令 ===");
        System.out.println("login                    - 重新发送登录消息");
        System.out.println("get_backpack <type_id>   - 获取指定类型背包");
        System.out.println("add_item <item_id> <count> - 添加道具");
        System.out.println("use_item <item_id> <count> - 使用道具");
        System.out.println("help                     - 显示此帮助");
        System.out.println("quit/exit                - 退出程序");
        System.out.println("========================\n");
    }

    public static void main(String[] args) throws Exception{
        // 测试用例1：使用预设账号（根据playerId）
        TestGameClientBag client1 = new TestGameClientBag("localhost", 8888, 2L);
        
        System.out.println("开始测试交互式客户端...");
        client1.start();
    }
}
