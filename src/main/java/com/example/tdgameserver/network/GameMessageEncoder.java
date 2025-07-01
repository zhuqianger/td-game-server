package com.example.tdgameserver.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GameMessageEncoder extends MessageToByteEncoder<GameMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GameMessage msg, ByteBuf out) throws Exception {
        //写入消息ID(4字节)
        out.writeInt(msg.getMessageId());
        //写入消息体长度(4字节)
        out.writeInt(msg.getPayload().length);
        //写入消息体
        out.writeBytes(msg.getPayload());
    }
}
