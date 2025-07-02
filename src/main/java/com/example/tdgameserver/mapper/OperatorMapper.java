package com.example.tdgameserver.mapper;

import com.example.tdgameserver.entity.Operator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 干员基础信息Mapper接口
 */
@Mapper
public interface OperatorMapper {
    
    /**
     * 根据干员ID查询干员信息
     */
    Operator selectByOperatorId(@Param("operatorId") String operatorId);
    
    /**
     * 查询所有干员信息
     */
    List<Operator> selectAll();
    
    /**
     * 根据职业查询干员列表
     */
    List<Operator> selectByProfession(@Param("profession") String profession);
    
    /**
     * 根据稀有度查询干员列表
     */
    List<Operator> selectByRarity(@Param("rarity") Integer rarity);
    
    /**
     * 插入干员信息
     */
    int insert(Operator operator);
    
    /**
     * 更新干员信息
     */
    int update(Operator operator);
    
    /**
     * 删除干员信息
     */
    int deleteByOperatorId(@Param("operatorId") String operatorId);
} 