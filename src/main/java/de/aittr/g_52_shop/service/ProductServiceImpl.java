package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductValidationException;
import de.aittr.g_52_shop.repository.ProductRepository;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.ProductMappingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMappingService mappingService;

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        try {
            Product entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mappingService.mapEntityToDto(entity);
        } catch (Exception e) {
            throw new ProductValidationException(e);
        }
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {

//        logger.info("Request for all products received");
//        logger.warn("Request for all products received");
//        logger.error("Request for all products received");


        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {

//        Product product = repository.findById(id).orElse(null);
//        if(product==null||!product.isActive()){
//            throw new ProductNotFoundException(id);
//        }
//        return mappingService.mapEntityToDto(product);
        return mappingService.mapEntityToDto(getActiveProductEntityById(id));
    }

    public Product getActiveProductEntityById(Long id){
        return repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public void update(ProductDto dto) {
        Product existingProduct = repository.findById(dto.getId())
                .orElseThrow(() -> new ProductNotFoundException(dto.getId()));

        existingProduct.setTitle(dto.getTitle());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setImage(dto.getImage());

        repository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setActive(false);
        repository.save(product);
    }

    @Override
    @Transactional
    public void deleteByTitle(String title) {
        Product product = repository.findByTitle(title)
                .orElseThrow(() -> new ProductNotFoundException(title));
        product.setActive(false);
        repository.save(product);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setActive(true);
        repository.save(product);
    }

    @Override
    public long getAllActiveProductsCount() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .count();
    }

    @Override
    public BigDecimal getAllActiveProductsTotalCost() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        List<Product> activeProducts = repository.findAll()
                .stream()
                .filter(Product::isActive)
                .toList();

        if (activeProducts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalCost = activeProducts
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalCost.divide(BigDecimal.valueOf(activeProducts.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void attachImage(String imageUrl, String productTitle) {
        repository.findByTitle(productTitle)
                .orElseThrow(() -> new ProductNotFoundException(productTitle))
                .setImage(imageUrl);
    }
    // https://shop-bucket.fra1.digitaloceanspaces.com
    // fKu8t3B3geeXdTJksnMAqR8tCtIghvXKwkMdHNCXrWc - secret key
    // DO801MMWX3RLJ7YYDHQJ - access key
}

