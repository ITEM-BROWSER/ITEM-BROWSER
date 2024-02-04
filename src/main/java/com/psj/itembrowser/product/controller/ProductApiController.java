package com.psj.itembrowser.product.controller;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductService productService;

    @PostMapping(value = "/v1/api/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageDTO createProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO) {
        productService.createProduct(productRequestDTO);
        return new MessageDTO("Insert Success");
    }

    @PutMapping("/v1/api/products/{productId}")
    public MessageDTO modifyProduct(@Valid @ModelAttribute ProductUpdateDTO productUpdateDTO,
        @PathVariable Long productId) {
        productService.updateProduct(productUpdateDTO, productId);
        return new MessageDTO("Modify Success");
    }
}
