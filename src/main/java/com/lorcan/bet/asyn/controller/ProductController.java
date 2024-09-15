package com.lorcan.bet.asyn.controller;

import com.lorcan.bet.asyn.dto.ProductDto;
import com.lorcan.bet.asyn.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping(value = "/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto){
       dto = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<ProductDto>> getProducts(){
        return ResponseEntity.ok(productService.getAllProducts());

    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        ProductDto dto = productService.getProductById(id);
        return ResponseEntity.ok(dto);

    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        ProductDto updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(value = "/remove/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        ProductDto deletedProduct = productService.removeProductById(id);
        return ResponseEntity.ok(deletedProduct);
    }
}
