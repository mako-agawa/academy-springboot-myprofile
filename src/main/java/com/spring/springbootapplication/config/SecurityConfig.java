package com.spring.springbootapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
        private final CustomLogoutSuccessHandler logoutSuccessHandler;

        public SecurityConfig(CustomLogoutSuccessHandler logoutSuccessHandler) {
                this.logoutSuccessHandler = logoutSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // --- 認可設定 ---
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/uploads/**",
                                                                "/error", "/error/**", "/debug/**", "/profile")
                                                .permitAll()
                                                .requestMatchers(
                                                                "/auth/login",
                                                                "/auth/register")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                // --- フォームログイン ---
                                .formLogin(form -> form
                                                .loginPage("/auth/login") // ログインページ(表示)
                                                .loginProcessingUrl("/auth/login") // ログイン処理(POST先)
                                                .usernameParameter("email") // ← これを追加
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true) // 成功後に必ずトップへ
                                                .permitAll())

                                // --- ログアウト ---
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout") // デフォルトは POST /logout
                                                .logoutSuccessHandler(logoutSuccessHandler) // ← 追加
                                                .logoutSuccessUrl("/auth/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers(
                                                                "/auth/login",
                                                                "/auth/register"));

                return http.build();

        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        
}