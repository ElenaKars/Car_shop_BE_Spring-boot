package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.service.interfaces.EmailService;
import de.aittr.g_52_shop.service.interfaces.RoleService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService) {
        this.repository = repository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    //using the method the framework will receive users objects with the roles from the DB
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("User " + username + " not found")
        );
    }

    @Override
    public void register(User user) {
        user.setId(null);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(false);
        user.setRoles(Set.of(roleService.getRoleUser()));

        repository.save(user);
        emailService.sendConfirmationEmail(user);
    }
}
