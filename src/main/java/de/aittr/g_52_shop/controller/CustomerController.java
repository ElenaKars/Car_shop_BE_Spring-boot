package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    //Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным)
    //POST -> http://12.34.56.78:8080/customers
    @PostMapping
    public CustomerDto save(@RequestBody CustomerDto customer) {
        return service.save(customer);
    }

    //Вернуть всех покупателей из базы данных (активных). GET -> /customers/all
    @GetMapping("/all")
    public List<CustomerDto> getAllCustomers() {
        return service.getAllActiveCustomers();
    }

    //Вернуть одного покупателя из базы данных по его идентификатору (если он активен) GET -> /customers/5
    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID", description = "Returns a customer by their unique identifier if they are active")
    public CustomerDto getCustomerById(
            @PathVariable
            @Parameter(description = "Customer unique identifier")
            Long id) {
        return service.getById(id);
    }

    //Изменить одного покупателя в базе данных по его идентификатору. PUT -> /customers/5
    @PutMapping
    @Operation(summary = "Update customer", description = "Updates an existing customer")
    public void updateCustomer(@RequestBody CustomerDto dto) {
        service.update(dto);
    }

    //Удалить покупателя из базы данных по его идентификатору DELETE -> /customers/5
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID", description = "Deletes a customer by their ID")
    public void deleteCustomerById(@PathVariable Long id) {
        service.deleteById(id);
    }


    //Удалить покупателя из базы данных по его имени DELETE -> /customers/name
    @DeleteMapping("/by-name/{name}")
    @Operation(summary = "Delete customer by name", description = "Deletes a customer by their name")
    public void deleteByName(@PathVariable String name) {
        service.deleteByName(name);
    }

    //Восстановить удалённого покупателя в базе данных по его идентификатору PUT -> /customers/restore/5
    @PutMapping("/restore/{id}")
    @Operation(summary = "Restore deleted customer", description = "Restores a deleted customer by their ID")
    public void restoreCustomer(@PathVariable Long id) {
        service.restoreById(id);
    }

    //Вернуть общее количество покупателей в базе данных (активных) GET-> /customers/count
    @GetMapping("/count")
    @Operation(summary = "Get active customer count", description = "Returns the number of active customers in the database")
    public long getActiveCustomerCount() {
        return service.getAllActiveCustomersCount();
    }

    //Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
    @GetMapping("/cart-total/{id}")
    @Operation(summary = "Get cart total", description = "Returns the total cost of a customer's cart")
    public Double getCartTotal(@PathVariable Long id) {
        return service.getCartTotal(id);
    }
//Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)

    @GetMapping("/cart-average/{id}")
    @Operation(summary = "Get cart average price", description = "Returns the average price of items in a customer's cart")
    public double getCartAverage(@PathVariable Long id) {
        return service.getCartAverage(id);
    }

    //Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    @PutMapping("/{customerId}/add-product/{productId}")
    @Operation(summary = "Add product to cart", description = "Adds a product to a customer's cart")
    public void addProductToCart(
            @PathVariable Long customerId,
            @PathVariable Long productId) {
        service.addProductToCart(customerId, productId);
    }
//Удалить товар из корзины покупателя по их идентификаторам

    @DeleteMapping("/cart/remove")
//    @Operation(summary = "Remove product from cart", description = "Removes a product from a customer's cart")
    public void removeProductFromCart(@RequestParam Long customerId, @RequestParam Long productId) {
        service.deleteProductFromTheCartById(customerId, productId);
    }
//Полностью очистить корзину покупателя по его идентификатору (если он активен)

    @DeleteMapping("/cart/clear/{id}")
    @Operation(summary = "Clear customer's cart", description = "Removes all products from a customer's cart")
    public void clearCart(@PathVariable Long id) {
        service.deleteAllProductByCustomerId(id);
    }

}
