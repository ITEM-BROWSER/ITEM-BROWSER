package com.psj.itembrowser.shippingInfos.mapper;

import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShippingInfosMapper {
    
    @Results(id = "shippingInfosResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "receiver", column = "receiver"),
            @Result(property = "mainAddress", column = "main_address"),
            @Result(property = "subAddress", column = "sub_address"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "alternativeNumber", column = "alternative_number"),
            @Result(property = "shippingRequestMsg", column = "shipping_request_msg"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    @Select("SELECT * " +
            "FROM SHIPPING_INFOS " +
            "WHERE ID = #{id} " +
            "AND DELETED_DATE  IS NULL " +
            "LIMIT 1")
    ShippingInfo findShippingInfoById(@Param("id") Long id);
}