package com.jpk.productservice.service;

import com.jpk.productservice.dto.ProductRequest;
import com.jpk.productservice.dto.ProductResponse;
import com.jpk.productservice.model.Product;
import com.jpk.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        productRepository.insert(product);
        log.info("Product {} is saved ", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productsList = productRepository.findAll();
        return productsList.stream().map(this::mapToProductResponse).toList();
    }

    public void deleteProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(productRepository::delete);
        log.info(("Product Deleted."));
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        return productResponse;
    }


}
