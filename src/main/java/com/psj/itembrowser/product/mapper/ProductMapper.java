package com.psj.itembrowser.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;

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

	boolean updateProductQuantity(ProductQuantityUpdateRequestDTO product);

	void insertProduct(Product product);

	boolean insertProductImages(List<ProductImage> productImages);

	List<ProductImage> findProductImagesByImageIds(List<Long> imageIds);

	void updateProduct(Product product);

	void softDeleteProductImages(List<Long> deleteImageIds);

	void softDeleteProduct(@Param("productId") Long productId);

	List<ProductImage> findProductImagesByProductId(Long productId);
}