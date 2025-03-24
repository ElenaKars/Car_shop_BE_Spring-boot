package de.aittr.g_52_shop.exception_handling;

import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // this annotation means that it is an advice object
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Response> handleException(ProductNotFoundException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<Response> handleException(ProductValidationException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Response> handleException(CustomerNotFoundException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
