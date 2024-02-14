package com.psj.itembrowser.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy.DeliveryFee;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderCalculationServiceImplTest {
    
    @Mock
    private ProductService productService;
    @Mock
    private PercentageDiscountService percentageDiscountService;
    @Mock
    private ShippingPolicyService shippingPolicyService;
    @InjectMocks
    private OrderCalculationServiceImpl orderCalculationService;
    
    private OrderCreateRequestDTO mockOrderCreateRequestDTO;
    
    private Member mockMember;
    
    private Product mockProduct;
    
    @BeforeEach
    void setUp() {
        mockOrderCreateRequestDTO = mock(OrderCreateRequestDTO.class);
        mockMember = mock(Member.class);
        mockProduct = mock(Product.class);
    }
    
    @Test
    void calculateOrderDetails() {
        // given
        double totalPrice = 1000.0;
        double productPrice = 1000.0;
        double discount = 500.0;
        
        try (MockedStatic<Product> productMockedStatic = mockStatic(Product.class)) {
            ShippingPolicy mockShippingPolicy = mock(ShippingPolicy.class);
            
            given(mockProduct.getUnitPrice()).willReturn(1000);
            given(mockProduct.getQuantity()).willReturn(1);
            
            given(mockOrderCreateRequestDTO.getProducts()).willReturn(Collections.singletonList(mock(OrdersProductRelationResponseDTO.class)));
            
            productMockedStatic.when(() -> Product.from(mock(ProductResponseDTO.class))).thenReturn(mockProduct);
            
            given(mockProduct.calculateTotalPrice()).willReturn(productPrice);
            
            given(percentageDiscountService.calculateDiscount(mockProduct, mockMember)).willReturn(discount);
            
            given(shippingPolicyService.getCurrentShippingPolicy()).willReturn(mockShippingPolicy);
            
            given(shippingPolicyService.getCurrentShippingPolicy().calculateShippingFee(totalPrice)).willReturn(DeliveryFee.DEFAULT);
            
            // when
            OrderCalculationResult actualResult = orderCalculationService.calculateOrderDetails(mockOrderCreateRequestDTO, mockMember);
            
            // then
            assertThat(actualResult).isNotNull();
            assertThat(actualResult.getTotalPrice()).isEqualTo(totalPrice);
            assertThat(actualResult.getTotalDiscount()).isEqualTo(discount);
            assertThat(actualResult.getShippingFee()).isEqualTo(DeliveryFee.DEFAULT.getFee());
            assertThat(actualResult.getOrderTotal()).isEqualTo(totalPrice - discount + DeliveryFee.DEFAULT.getFee());
        }
        
        
    }
}