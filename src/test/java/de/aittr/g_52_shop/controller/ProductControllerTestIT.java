package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
class ProductControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${key.access}")
    private String accessPhrase;

    @Autowired
    private ProductRepository repository;

    private final String BEARER_PREFIX = "Bearer ";

    private ProductDto testProduct;
    private String adminAccessToken;
    private SecretKey accessKey;

    @BeforeEach
    public void setUp() {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testProduct = createTestProduct();
    }

    @Test
    @Order(1)
    public void checkRequestForAllProducts() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                "/products/all", HttpMethod.GET, request, ProductDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected Http-status");

        assertNotNull(response.getBody(), "Response body should not be empty");

        for (ProductDto product : response.getBody()) {
            assertNotNull(product.getId(), "Product id should not be null");
            assertNotNull(product.getTitle(), "Product title should not be null");
            assertNotNull(product.getPrice(), "Product price should not be null");
        }
    }

    @Test
    @Order(2)
    public void checkForbiddenStatusWhileSavingProductWithoutAuthorization() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "/products", HttpMethod.POST, request, ProductDto.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Unexpected Http-status");
        assertNull(response.getBody(), "Response body should be null");
    }

    @Test
    @Order(3)
    public void checkSuccessWhileSavingProductWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "/products", HttpMethod.POST, request, ProductDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected Http-status");

        ProductDto savedProduct = response.getBody();

        assertNotNull(savedProduct, "Saved product should not be null");

        assertNotNull(savedProduct.getId(), "Saved product-id should not be null");
        assertEquals(testProduct.getTitle(), savedProduct.getTitle(), "Saved product has incorrect title");
        assertEquals(testProduct.getPrice(), savedProduct.getPrice(), "Saved product has incorrect price");

        repository.deleteById(savedProduct.getId());
    }

    private ProductDto createTestProduct() {
        ProductDto product = new ProductDto();
        product.setTitle("Test product");
        product.setPrice(new BigDecimal(777));
        return product;
    }

    private String generateAdminAccessToken() {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");

        return BEARER_PREFIX + Jwts.builder()
                .subject("TestAdmin")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", Set.of(role))
                .signWith(accessKey)
                .compact();
    }
}