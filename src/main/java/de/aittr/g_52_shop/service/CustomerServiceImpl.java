package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.repository.CustomerRepository;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.CustomerMappingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMappingService mappingService;
    private final ProductService productService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService, ProductService productService) {
        this.repository = repository;
        this.mappingService = mappingService;
        this.productService = productService;
    }


    @Override
    @Transactional
    public CustomerDto save(CustomerDto dto) {
        Customer entity = mappingService.mapDtoToEntity(dto);
        entity = repository.save(entity);

        Cart cart = new Cart();
        cart.setCustomer(entity);
        entity.setCart(cart);

        entity = repository.save(entity);
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getById(Long id) {
        Customer customer = repository.findById(id).orElse(null);
        if (customer == null || !customer.isActive()) {
            throw new CustomerNotFoundException(id);
        }
        return mappingService.mapEntityToDto(customer);
    }

    @Override
    public CustomerDto update(CustomerDto customer) {
        return repository.findById(customer.getId())
                .map(existingCustomer -> {
                    existingCustomer.setName(customer.getName());
                    return mappingService.mapEntityToDto(repository.save(existingCustomer));
                }).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresent(customer -> {
            customer.setActive(false);
            repository.save(customer);
        });
    }

    @Override
    public void deleteByName(String name) {
        repository.findAll().stream()
                .filter(customer -> customer.getName().equals(name) && customer.isActive())
                .forEach(customer -> {
                    customer.setActive(false);
                    repository.save(customer);
                });
    }

    @Override
    public void restoreById(Long id) {
        repository.findById(id).ifPresent(customer -> {
            customer.setActive(true);
            repository.save(customer);
        });
    }

    @Override
    public long getAllActiveCustomersCount() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .count();
    }

    @Override
    public BigDecimal getShoppingValueByCustomerId(Long customerId) {
        return null;
    }

    @Override
    public BigDecimal getAverageValueCartByCustomerId(Long customerId) {
        return null;
    }

    @Override
    @Transactional
    public void addProductToCustomerCartById(Long customerId, Long productId) {
        Customer customer = getActiveCustomerEntityById(customerId);
        Product product = productService.getActiveProductEntityById(productId);
        customer.getCart().addProduct(product);
    }

    private Customer getActiveCustomerEntityById(Long id) {
        return repository.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public Double getCartTotal(Long id) {
        return 0.0;
    }

    @Override
    public Double getCartAverage(Long id) {
        return 0.0;
    }

    @Transactional
    @Override
    public void addProductToCart(Long customerId, Long productId) {
        Customer customer = getActiveCustomerEntityById(customerId);
        Product  product = productService.getActiveProductEntityById(productId);
        customer.getCart().addProduct(product);
    }

    @Override
    public void deleteProductFromTheCartById(Long customerId, Long productId) {
        System.out.println("Продукт с ID " + productId + " удален из корзины клиента с ID " + customerId);
    }

    @Override
    public void deleteAllProductByCustomerId(Long customerId) {
        System.out.println("Все Продукты удалены из корзины клиента с ID " + customerId);
    }
}
