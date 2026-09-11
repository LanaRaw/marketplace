package com.marketplace.controller;

import com.marketplace.model.Role;
import com.marketplace.model.User;
import com.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Обрабатывает запросы, связанные с авторизацией и регистрацией пользователей.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Отображает страницу авторизации.
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона страницы авторизации
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Вход");
        return "login";
    }

    /**
     * Отображает форму регистрации пользователя.
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона страницы регистрации
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("title", "Регистрация");
        return "register";
    }

    /**
     * Регистрирует нового пользователя.
     *
     * Проверяет уникальность email, шифрует пароль и сохраняет
     * пользователя с выбранной ролью (BUYER или SELLER).
     *
     * @param username имя пользователя
     * @param email адрес электронной почты
     * @param password пароль пользователя
     * @param role выбранная роль (BUYER или SELLER)
     * @param model модель для передачи сообщений в представление
     * @return имя шаблона регистрации или перенаправление на страницу авторизации
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "BUYER") String role,
            Model model) {

        model.addAttribute("title", "Регистрация");

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "register";
        }

        // Защита от регистрации с ролью ADMIN
        Role selectedRole;
        try {
            selectedRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            selectedRole = Role.BUYER;
        }

        if (selectedRole == Role.ADMIN) {
            selectedRole = Role.BUYER;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(selectedRole);
        user.setIsActive(true);

        userRepository.save(user);

        return "redirect:/login?registered";
    }
}