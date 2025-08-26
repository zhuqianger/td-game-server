package com.example.tdgameserver.requestEntity;

import lombok.Data;

@Data
public class ItemRequest {
    private Integer itemId;

    /**
     * 道具数量
     */
    private Integer quantity;
}
