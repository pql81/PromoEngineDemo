package com.pql.promo.service;

import com.pql.promo.domain.Product;
import com.pql.promo.dto.GetProductResponse;
import com.pql.promo.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Before
    public void setUp(){
        Product product1 = new Product();
        product1.setSku("abc01");
        product1.setName("test product 1");
        product1.setPrice(BigDecimal.TEN);

        Product product2 = new Product();
        product2.setSku("def02");
        product2.setName("test product 2");
        product2.setPrice(BigDecimal.TEN);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findBySku(product1.getSku())).thenReturn(Optional.of(product1));
        when(productRepository.findAll()).thenReturn(products);
    }

    @Test
    public void testGetProduct() throws Exception {
        GetProductResponse response = productService.getProduct("abc01");

        assertEquals("abc01", response.getSku());
        assertEquals("test product 1", response.getName());
        assertEquals(BigDecimal.TEN, response.getPrice());
    }

    @Test
    public void testEmptyGetProduct() throws Exception {
        GetProductResponse response = productService.getProduct("invalid");

        assertNull(response);
    }

    @Test
    public void testListProducts() throws Exception {
        List<GetProductResponse> response = productService.listProducts();

        assertFalse(response.isEmpty());
        for (GetProductResponse productResp: response) {
            assertNotNull(productResp.getSku());
            assertNotNull(productResp.getName());
            assertNotNull(productResp.getPrice());
        }
    }
}