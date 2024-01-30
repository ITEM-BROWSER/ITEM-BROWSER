package com.psj.itembrowser.product.controller;

import com.psj.itembrowser.common.message.MessageDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductService productService;

    @PostMapping("/v1/api/products")
    public MessageDTO addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
        return new MessageDTO("Insert Success");
    }

    @PutMapping("/v1/api/products")
    public MessageDTO modifyProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return new MessageDTO("Modify Success");
    }
}
