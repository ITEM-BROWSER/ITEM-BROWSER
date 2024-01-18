package com.psj.itembrowser.product.mapper;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.psj.itembrowser.product.mapper
 * fileName       : ProductMapper
 * author         : ipeac
 * date           : 2023-10-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-07        ipeac       최초 생성
 */
@Mapper
@Repository
public interface ProductMapper {
    @Results(id = "productResultMap", value = {
            @Result(id = true, property = "id", column = "ID"),
            @Result(property = "name", column = "NAME"),
            @Result(property = "category", column = "CATEGORY"),
            @Result(property = "detail", column = "DETAIL"),
            @Result(property = "status", column = "STATUS"),
            @Result(property = "sellerId", column = "SELLER_ID"),
            @Result(property = "sellStartDatetime", column = "SELL_START_DATETIME"),
            @Result(property = "sellEndDatetime", column = "SELL_END_DATETIME"),
            @Result(property = "displayName", column = "DISPLAY_NAME"),
            @Result(property = "unitPrice", column = "UNIT_PRICE"),
            @Result(property = "brand", column = "BRAND"),
            @Result(property = "deliveryFeeType", column = "DELIVERY_FEE_TYPE"),
            @Result(property = "deliveryMethod", column = "DELIVERY_METHOD"),
            @Result(property = "deliveryDefaultFee", column = "DELIVERY_DEFAULT_FEE"),
            @Result(property = "freeShipOverAmount", column = "FREE_SHIP_OVER_AMOUNT"),
            @Result(property = "returnCenterCode", column = "RETURN_CENTER_CODE"),
            @Result(property = "createdDate", column = "CREATED_DATE"),
            @Result(property = "updatedDate", column = "UPDATED_DATE")
    })
    @Select("SELECT ID, NAME, CATEGORY, DETAIL, STATUS, QUANTITY, SELLER_ID, SELL_START_DATETIME, SELL_END_DATETIME, " +
            "DISPLAY_NAME, UNIT_PRICE, BRAND, DELIVERY_FEE_TYPE, DELIVERY_METHOD, DELIVERY_DEFAULT_FEE, FREE_SHIP_OVER_AMOUNT, RETURN_CENTER_CODE, CREATED_DATE, UPDATED_DATE " +
            "FROM PRODUCT  " +
            "WHERE ID = #{productId} " +
            "LIMIT 1")
    Product findProductById(@Param("productId") Long productId);
    
    @ResultMap("productResultMap")
    @Select("SELECT ID, NAME, CATEGORY, DETAIL, STATUS, QUANTITY, SELLER_ID, SELL_START_DATETIME, SELL_END_DATETIME, " +
            "DISPLAY_NAME, UNIT_PRICE, BRAND, DELIVERY_FEE_TYPE, DELIVERY_METHOD, DELIVERY_DEFAULT_FEE, FREE_SHIP_OVER_AMOUNT, RETURN_CENTER_CODE, CREATED_DATE, UPDATED_DATE " +
            "FROM PRODUCT  " +
            "WHERE ID = #{productId} " +
            "LIMIT 1 " +
            "FOR UPDATE")
    Product lockProductById(@Param("productId") Long productId);
    
    List<Product> findProductsByIds(@Param("productIds") List<Long> productIds);
    
    List<Product> lockAndFindProductsByIds(@Param("productIds") List<Long> productIds);
    
    @ResultMap("productResultMap")
    @Select("SELECT P.ID, P.NAME, P.CATEGORY, P.DETAIL, P.STATUS, P.QUANTITY , P.SELLER_ID, P.SELL_START_DATETIME, P.SELL_END_DATETIME, " +
            " P.DISPLAY_NAME, P.UNIT_PRICE, P.BRAND, P.DELIVERY_FEE_TYPE, P.DELIVERY_METHOD, P.DELIVERY_DEFAULT_FEE, " +
            " P.FREE_SHIP_OVER_AMOUNT, P.RETURN_CENTER_CODE, P.CREATED_DATE, P.UPDATED_DATE " +
            "FROM PRODUCT P " +
            "LEFT JOIN ORDERS_PRODUCT_RELATION OPR ON OPR.PRODUCT_ID = P.ID " +
            "WHERE OPR.GROUP_ID = #{orderId}")
    List<Product> findProductsByOrderId(@Param("orderId") Long orderId);
    
    @Update("UPDATE PRODUCT " +
            "SET  QUANTITY = #{quantity}  " +
            "WHERE ID = #{id}")
    boolean updateProduct(ProductQuantityUpdateRequestDTO product);
}