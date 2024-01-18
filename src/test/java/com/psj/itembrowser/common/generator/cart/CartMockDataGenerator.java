package com.psj.itembrowser.common.generator.cart;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartProductRelationResponseDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.psj.itembrowser.cart.data
 * fileName       : CartMockDataGenerator
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
public class CartMockDataGenerator {
    
    public static Cart createCart(Long id, String userId, List<CartProductRelation> products) {
        return new Cart(id, userId, products);
    }
    
    public static Product createProduct(Long id, String name, Integer category, String detail, ProductStatus status, Integer quantity,
                                        Integer unitPrice, String sellerId, LocalDateTime sellStartDatetime,
                                        LocalDateTime sellEndDatetime, String displayName, String brand,
                                        String deliveryFeeType, String deliveryMethod, Integer deliveryDefaultFee,
                                        Integer freeShipOverAmount, String returnCenterCode, LocalDateTime createdDate,
                                        LocalDateTime updatedDate, List<CartProductRelation> cartProductRelations) {
        
        return new Product(id, name, category, detail, status, quantity, unitPrice, sellerId, sellStartDatetime,
            sellEndDatetime, displayName, brand, deliveryFeeType, deliveryMethod, deliveryDefaultFee,
            freeShipOverAmount, returnCenterCode, createdDate, updatedDate, cartProductRelations);
    }
    
    public static CartProductRequestDTO createCartProductRequestDTO(Long cartId, Long productId, Integer quantity) {
        return createCartProductRequestDTO(cartId, productId, "sampleUser", quantity);
    }
    
    public static CartProductUpdateRequestDTO createCartProductUpdateRequestDTO(Long cartId, Long productId, Integer quantity) {
        return new CartProductUpdateRequestDTO(cartId, productId, quantity);
    }
    
    public static CartProductRequestDTO createCartProductRequestDTO(Long cartId, Long productId, String userId, Integer quantity) {
        return new CartProductRequestDTO(cartId, productId, userId, quantity);
    }
    
    public static CartProductDeleteRequestDTO createCartProductDeleteRequestDTO(Long cartId, Long productId) {
        return new CartProductDeleteRequestDTO(cartId, productId);
    }
    
    public static Product createSimpleProduct(Long id, String name, Integer category, Integer quantity, Integer unitPrice) {
        return createProduct(id, name, category, "detail", ProductStatus.APPROVED, quantity, unitPrice, "seller1",
            LocalDateTime.now(), LocalDateTime.now(), "displayName", "brand", "deliveryFeeType", "deliveryMethod",
            1000, 10000, "returnCenterCode", LocalDateTime.now(), LocalDateTime.now(), List.of());
    }
    
    public static CartProductRelation createCartProductRelation(Long cartId,
                                                                Long productId,
                                                                Long productQuantity,
                                                                LocalDateTime createdDate,
                                                                LocalDateTime updatedDate,
                                                                LocalDateTime deletedDate,
                                                                Cart cart,
                                                                Product product) {
        return new CartProductRelation(cartId, productId, productQuantity, cart, product, createdDate, updatedDate, deletedDate);
    }
    
    public static CartResponseDTO createCartResponseDTO(String userId, List<CartProductRelationResponseDTO> products) {
        // 빌더로 변경
        return CartResponseDTO
            .builder()
            .userId(userId)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .products(products)
            .build();
    }
    
    public static CartProductRelationResponseDTO createCartProductRelationResponseDTO(Long cartId,
                                                                                      Long productId,
                                                                                      Long productQuantity,
                                                                                      CartResponseDTO cart,
                                                                                      ProductResponseDTO product) {
        return new CartProductRelationResponseDTO(cartId, productId, productQuantity, cart, product);
    }
    
    public static CartRequestDTO createCartRequestDTO(String testUserId) {
        return new CartRequestDTO(testUserId);
    }
}