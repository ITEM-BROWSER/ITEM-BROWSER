package com.psj.itembrowser.product.domain.vo;

import com.psj.itembrowser.common.BaseDateTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductImage extends BaseDateTimeEntity {

    private Long id;

    private Long productId;

    @URL(message = "Must be in url format.")
    private String imgUrl;
}
