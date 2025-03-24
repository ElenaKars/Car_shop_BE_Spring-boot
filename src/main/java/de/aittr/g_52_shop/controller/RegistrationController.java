package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.exception_handling.Response;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService service;
    private final ConfirmationService confirmationService;

    public RegistrationController(UserService service, ConfirmationService confirmationService) {
        this.service = service;
        this.confirmationService = confirmationService;
    }

    @PostMapping
    public Response register(@RequestBody User user) {
        service.register(user);
        return new Response("Регистрация успешна. Проверьте почту для подтверждения для подтверждения регистрации");
    }

    @GetMapping("/{code}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String code) {
        try {
            confirmationService.activateUser(code);
            return ResponseEntity.ok("Регистрация подтверждена успешно");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверный код подтверждения");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Срок действия кода подтверждения истек");
        }
    }
}
