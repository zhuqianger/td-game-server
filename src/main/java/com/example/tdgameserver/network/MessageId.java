package com.example.tdgameserver.network;

import lombok.Getter;

@Getter
public enum MessageId {
    //登录相关
    REQ_LOGIN(1),
    RESP_LOGIN_FAIL(2),
    RESP_LOGIN_SUCCESS(3),
    
    //玩家相关
    REQ_CREATE_PLAYER(10),
    RESP_CREATE_PLAYER_SUCCESS(11),
    RESP_CREATE_PLAYER_FAIL(12),
    
    REQ_GET_PLAYER_INFO(13),
    RESP_GET_PLAYER_INFO(14),
    
    REQ_UPDATE_PLAYER(15),
    RESP_UPDATE_PLAYER_SUCCESS(16),
    RESP_UPDATE_PLAYER_FAIL(17),
    
    REQ_DELETE_PLAYER(18),
    RESP_DELETE_PLAYER_SUCCESS(19),
    RESP_DELETE_PLAYER_FAIL(20),
    
    REQ_GET_ALL_PLAYERS(21),
    RESP_GET_ALL_PLAYERS(22),
    
    //错误
    ERROR_MSG(-1);

    private final int id;
    private final String desc;

    MessageId(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    MessageId(int id) {
        this.id = id;
        this.desc = null;
    }

    public int getId(){
        return id;
    }
}
