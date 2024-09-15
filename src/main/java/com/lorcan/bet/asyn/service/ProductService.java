package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.dto.ProductDto;
import com.lorcan.bet.asyn.entity.Product;
import com.lorcan.bet.asyn.repository.ProductRepository;
import com.lorcan.bet.asyn.util.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private  ProductRepository productRepository;

    public ProductDto createProduct(ProductDto dto){

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        productRepository.save(product);

        dto.setId(product.getId());
        dto.setMessage("Added");
        return dto;
    }

    public ProductDto removeProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        ProductDto dto = null;
        if(product.isPresent()){
            dto = ProductUtil.convertToDto(product.get());
            dto.setMessage("Removed");
            productRepository.deleteById(id);
        }
        else{
            dto = new ProductDto(id, "Not Found");
        }
        return dto;
    }

    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductUtil::convertToDto)
                .orElse(new ProductDto(id,"Not found"));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductUtil::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        return productRepository.findById(productId)
                .map(existingProduct -> {
                    if(null != productDto.getName() && !productDto.getName().isEmpty()){
                        existingProduct.setName(productDto.getName());
                    }
                    if(null != productDto.getDescription() && !productDto.getDescription().isEmpty()){
                        existingProduct.setDescription(productDto.getDescription());
                    }
                    if(null != productDto.getPrice() && productDto.getPrice()>0.0){
                        existingProduct.setPrice(productDto.getPrice());
                    }

                    Product updatedProduct = productRepository.save(existingProduct);
                    ProductDto dto = ProductUtil.convertToDto(updatedProduct);
                    dto.setMessage("Updated");
                    return dto;
                })
                .orElse(new ProductDto(productId, "Not Found"));  // Return a blank ProductDTO if not found
    }

}
