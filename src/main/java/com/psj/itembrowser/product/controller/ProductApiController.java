package com.psj.itembrowser.product.controller;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductService productService;

    @PostMapping(value = "/v1/api/products", consumes =  {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageDTO addProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO) {
        productService.createProduct(productRequestDTO);
        return new MessageDTO("Insert Success");
    }

    @PutMapping("/v1/api/products")
    public MessageDTO modifyProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return new MessageDTO("Modify Success");
    }
}
