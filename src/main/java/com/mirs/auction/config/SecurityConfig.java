package com.mirs.auction.config;

import com.mirs.auction.security.JwtAuthenticationFilter;
import com.mirs.auction.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de la sécurité Spring Security pour l'API.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean pour encoder et vérifier les mots de passe avec BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure le DaoAuthenticationProvider pour déléguer la
     * récupération d'utilisateur et le check de mot de passe.
     */
    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Chaîne de filtres de sécurité :
     * - Désactive CSRF pour pouvoir tester avec Postman.
     * - Passe en mode stateless (pas de session HTTP).
     * - Ouvre /api/auth/register et /api/auth/login en POST, ainsi que /ws/**.
     * - Tout le reste nécessite un JWT valide.
     * - Insère le filtre JWT avant le filtre standard UsernamePasswordAuthentication.
     * - Active httpBasic() pour pouvoir dépanner, mais l'authe JWT prendra le relais.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authProvider, JwtUtil jwtUtil) throws Exception {
        http
                // 1) Désactivation CSRF (utile pour Postman / clients REST)
                .csrf(csrf -> csrf.disable())

                // 2) Pas de session (STATELESS), on travaille en JWT
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3) Règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                        // autoriser l'inscription et la connexion sans authentification
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // autoriser les WebSockets
                        .requestMatchers("/ws/**").permitAll()
                        // toutes les autres requêtes nécessitent un JWT valide
                        .anyRequest().authenticated()
                )

                // 4) Ajout du filtre JWT avant le filtre de login, on injecte jwtUtil dans le filtre
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)

                // 5) Notre provider d'authentification
                .authenticationProvider(authProvider)

                // 6) Activation de HTTP Basic (pour debug, facultatif)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
