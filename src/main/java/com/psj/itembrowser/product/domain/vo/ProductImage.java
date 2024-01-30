package com.psj.itembrowser.product.domain.vo;

import com.psj.itembrowser.common.BaseDateTimeEntity;
import org.hibernate.validator.constraints.URL;

public class ProductImage extends BaseDateTimeEntity {

    private Long Id;

    private Long productId;

    @URL(message = "Must be in url format.")
    private String imgUrl;
}
