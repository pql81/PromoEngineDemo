package com.pql.promo.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.repository.ProductRepository;
import com.pql.promo.repository.PromoRepository;
import com.pql.promo.strategies.PromoType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PromoEngineDemoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoRepository promoRepository;


    @Before
    public void setUp() {
        Promo promoA = createPromoForTest("3 A's for 130", "PROMO1", 3, PromoType.SINGLE_ITEM_FIXED, 130);
        Promo promoB = createPromoForTest("2 B's for 45", "PROMO2", 2, PromoType.SINGLE_ITEM_FIXED, 45);
        Promo promoC = createPromoForTest("C + D = 30", "PROMO3", 0, PromoType.MULTIPLE_ITEMS_FIXED, 30);

        createProductForTest("A", "test product A", 50, promoA);
        createProductForTest("B", "test product B", 30, promoB);
        createProductForTest("C", "test product C", 20, promoC);
        createProductForTest("D", "test product D", 15, promoC);
    }

    @Test
    public void testPromoScenario1() throws Exception {
        String reference = getCartForTest();

        addItemForTest(reference, "A", 1);
        addItemForTest(reference, "B", 1);
        addItemForTest(reference, "C", 1);

        mockMvc.perform(post("/carts/{reference}/checkout", reference)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(100.00))
                .andExpect(jsonPath("$.totalDiscount").value(0.00))
                .andExpect(jsonPath("$.amountDue").value(100.00));
    }

    @Test
    public void testPromoScenario2() throws Exception {
        String reference = getCartForTest();

        addItemForTest(reference, "A", 5);
        addItemForTest(reference, "B", 5);
        addItemForTest(reference, "C", 1);

        mockMvc.perform(post("/carts/{reference}/checkout", reference)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(420.00))
                .andExpect(jsonPath("$.totalDiscount").value(50.00))
                .andExpect(jsonPath("$.amountDue").value(370.00));
    }

    @Test
    public void testPromoScenario3() throws Exception {
        String reference = getCartForTest();

        addItemForTest(reference, "A", 3);
        addItemForTest(reference, "B", 5);
        addItemForTest(reference, "C", 1);
        addItemForTest(reference, "D", 1);

        mockMvc.perform(post("/carts/{reference}/checkout", reference)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(335.00))
                .andExpect(jsonPath("$.totalDiscount").value(55.00))
                .andExpect(jsonPath("$.amountDue").value(280.00));
    }

    private String getCartForTest() throws Exception {
        MvcResult resp = mockMvc.perform(post("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test1\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResp = resp.getResponse().getContentAsString();
        Map<String, String> map = mapper.readValue(jsonResp, Map.class);

        return map.get("reference");
    }

    private void addItemForTest(String reference, String sku, int qty) throws Exception {

        mockMvc.perform(post("/carts/{reference}/items", reference)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"sku\": \"%s\", \"quantity\": %d}", sku, qty))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private Promo createPromoForTest(String name, String code, int numItems, PromoType type, int value) {
        Promo promo = new Promo();
        promo.setName(name);
        promo.setPromoCode(code);
        promo.setNumItems(numItems);
        promo.setPromoType(type);
        promo.setDiscountValue(value);

        return promoRepository.save(promo);
    }

    private Product createProductForTest(String sku, String name, int price, Promo promo) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setPromo(promo);

        return productRepository.save(product);
    }
}
