package com.psj.itembrowser.product.domain.dto.request;

import com.psj.itembrowser.product.domain.vo.Product;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link Product}
 */
@Data
@Builder
public class ProductQuantityUpdateRequestDTO implements Serializable {
    Long id;
    Integer quantity;
}