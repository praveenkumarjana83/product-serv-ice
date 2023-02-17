package com.jpk.productservice;

import com.jpk.productservice.dto.ProductRequest;
import com.jpk.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class ProductServiceApplicationTests {

    //@Container
    static MongoDBContainer container = new MongoDBContainer("mongo:latest");

    static {
        container.start();
    }
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        log.info(" ---> container::getReplicaSetUrl : {}", container.getReplicaSetUrl());
        dynamicPropertyRegistry.add("DOCKER_MACHINE_NAME", () -> "Sachin");
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }
    @Test
    void shouldCreateProduct() throws Exception {
        log.info("-----> test execution is staring <-----");
        ProductRequest productRequest = getProductRequest();
        log.info(" ---> product request : {} ", productRequest);
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        log.info("--> productRequest String -----> : " + productRequestString);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString));
        //.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
        //container.stop();
        log.info("-----> test case is completed.. It's a Pass");
    }

    private ProductRequest getProductRequest() {
        ProductRequest pr = new ProductRequest();
        pr.setName("mobile");
        pr.setDescription("IPhone 14 Prod Max");
        pr.setPrice(BigDecimal.valueOf(2500));
        return pr;
    }

    @AfterAll
    static void terminate() {
        container.stop();
    }
}
