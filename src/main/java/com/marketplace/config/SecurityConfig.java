package com.marketplace.config;

import com.marketplace.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Настраивает безопасность приложения и правила доступа к ресурсам.
 *
 * Определяет доступ для разных ролей, настройку формы входа,
 * выхода из системы и компоненты для аутентификации пользователей.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;

    /**
     * Настраивает цепочку фильтров безопасности Spring Security.
     *
     * @param http объект для настройки безопасности HTTP-запросов
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникает ошибка при настройке безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products/**", "/register", "/login", "/cart/**", "/css/**", "/js/**", "/images/**", "/uploads/**", "/favicon.ico", "/favicon.png", "/*.png", "/*.ico").permitAll()
                        .requestMatchers("/seller/**").hasAuthority("SELLER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(loginSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Создает кодировщик паролей на основе BCrypt.
     *
     * @return кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настраивает провайдер аутентификации пользователей.
     *
     * @return провайдер аутентификации
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Создает менеджер аутентификации Spring Security.
     *
     * @param authConfig конфигурация аутентификации Spring Security
     * @return менеджер аутентификации
     * @throws Exception если менеджер аутентификации не может быть создан
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}