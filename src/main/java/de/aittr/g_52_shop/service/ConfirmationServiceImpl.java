package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.ConfirmationCode;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.ConfirmationCodeRepository;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;
    private final UserRepository userRepository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(5);
        ConfirmationCode codeEntity = new ConfirmationCode(code, expired, user);
        repository.save(codeEntity);
        return code;
    }

    @Override
    public void activateUser(String code) {
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid confirmation code"));

        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Confirmation code has expired");
        }

        User user = confirmationCode.getUser();
        user.setActive(true);
        userRepository.save(user);
        repository.delete(confirmationCode);
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 5; i++) {
//            System.out.println(UUID.randomUUID());
//        }
//    }
}