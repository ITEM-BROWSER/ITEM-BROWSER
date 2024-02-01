package com.psj.itembrowser.product.mapper;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
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

    Product findProductById(@Param("productId") Long productId);

    Product lockProductById(@Param("productId") Long productId);

    List<Product> findProductsByIds(@Param("productIds") List<Long> productIds);

    List<Product> lockAndFindProductsByIds(@Param("productIds") List<Long> productIds);

    List<Product> findProductsByOrderId(@Param("orderId") Long orderId);

    boolean updateProduct(ProductQuantityUpdateRequestDTO product);

    void insertProduct(Product product);

    boolean insertProductImages(List<ProductImage> productImages);
}