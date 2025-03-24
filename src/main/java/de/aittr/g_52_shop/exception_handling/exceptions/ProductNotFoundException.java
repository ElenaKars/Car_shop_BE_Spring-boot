package de.aittr.g_52_shop.exception_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND) // the annotation ResponseStatus is the 2 option how to determinate exception
//the disadvantage of the 2d option is not informative
// the advantage is easy creating an exception
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %d not found", id));
    }

    public ProductNotFoundException(String title) {
        super(String.format("Product with title %s not found", title));
    }
}
