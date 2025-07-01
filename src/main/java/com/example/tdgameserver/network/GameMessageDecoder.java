package com.example.tdgameserver.network;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class GameMessageDecoder extends ByteToMessageDecoder {
    private final int Min_Length = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //确保有足够的字节可读
//        if(in.readableBytes() < Min_Length){ //至少需要8字节(消息ID 4字节 + 消息长度 4字节)
//            return;
//        }

        //标记当前读取位置
        in.markReaderIndex();

        //读取消息Id
        int messageId = in.readInt();
        //读取消息体长度
        int length = in.readInt();

        //检查消息体是否完整
        if(in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        //读取消息体
        byte[] payload = new byte[length];
        in.readBytes(payload);

        //创建消息对象并添加到输出列表
        out.add(new GameMessage(messageId,payload));
    }
}
