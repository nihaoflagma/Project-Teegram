package com.example.otp.service;

import com.example.otp.model.Role;
import com.example.otp.model.User;
import com.example.otp.repository.RoleRepository;
import com.example.otp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Метод регистрации нового пользователя
    public String registerUser(String email, String password, String roleName) {
        // Проверка — есть ли уже пользователь с таким email
        if (userRepository.findByEmail(email).isPresent()) {
            return "Пользователь с таким email уже существует.";
        }

        // Поиск роли
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            return "Роль " + roleName + " не найдена.";
        }

        // Создание пользователя
        User user = new User();
        user.setUsername(email);   // 👈 исправил здесь
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);


        return "Пользователь успешно зарегистрирован!";
    }
}
