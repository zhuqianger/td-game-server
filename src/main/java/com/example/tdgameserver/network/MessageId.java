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
    
    //干员相关
    REQ_GET_OPERATOR_INFO(30),
    RESP_GET_OPERATOR_INFO(31),
    
    REQ_GET_ALL_OPERATORS(32),
    RESP_GET_ALL_OPERATORS(33),
    
    REQ_GET_PLAYER_OPERATORS(34),
    RESP_GET_PLAYER_OPERATORS(35),
    
    REQ_ADD_PLAYER_OPERATOR(36),
    RESP_ADD_PLAYER_OPERATOR_SUCCESS(37),
    RESP_ADD_PLAYER_OPERATOR_FAIL(38),
    
    REQ_LEVEL_UP_OPERATOR(39),
    RESP_LEVEL_UP_OPERATOR_SUCCESS(40),
    RESP_LEVEL_UP_OPERATOR_FAIL(41),
    
    REQ_ELITE_OPERATOR(42),
    RESP_ELITE_OPERATOR_SUCCESS(43),
    RESP_ELITE_OPERATOR_FAIL(44),
    
    REQ_UPGRADE_SKILL(45),
    RESP_UPGRADE_SKILL_SUCCESS(46),
    RESP_UPGRADE_SKILL_FAIL(47),
    
    REQ_MASTER_SKILL(48),
    RESP_MASTER_SKILL_SUCCESS(49),
    RESP_MASTER_SKILL_FAIL(50),
    
    REQ_UPDATE_OPERATOR_HP(51),
    RESP_UPDATE_OPERATOR_HP_SUCCESS(52),
    RESP_UPDATE_OPERATOR_HP_FAIL(53),
    
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
