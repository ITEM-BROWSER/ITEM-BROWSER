package com.psj.itembrowser.product.domain.vo;

import com.psj.itembrowser.common.BaseDateTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage extends BaseDateTimeEntity {

    private Long Id;

    private Long productId;

    @URL(message = "Must be in url format.")
    private String imgUrl;
}
