package com.mirs.auction.service;

import com.mirs.auction.entity.User;
import com.mirs.auction.repository.UserRepository;
import com.mirs.auction.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service gérant l'inscription, l'authentification et la génération de JWT.
 */
@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Enregistre un nouvel utilisateur en hashant son mot de passe.
     */
    public User register(User user) {
        user.setUserPassword(
                passwordEncoder.encode(user.getUserPassword())
        );
        user.setUserStatus("actif");
        return userRepo.save(user);
    }

    /**
     * Authentifie un utilisateur (pseudo ou email + mot de passe)
     * et génère un JWT via JwtUtil.
     *
     * @param usernameOrEmail le pseudo ou l'adresse e-mail
     * @param rawPassword     le mot de passe en clair
     * @return le token JWT
     * @throws RuntimeException si utilisateur introuvable ou mot de passe incorrect
     */
    public String authenticate(String usernameOrEmail, String rawPassword) {
        // 1) Recherche de l'utilisateur par pseudo ou e-mail
        User user = userRepo.findByUserName(usernameOrEmail)
                .orElseGet(() ->
                        userRepo.findByUserEmail(usernameOrEmail)
                                .orElseThrow(() ->
                                        new RuntimeException("Utilisateur non trouvé")
                                )
                );

        // 2) Vérification du mot de passe
        if (!passwordEncoder.matches(rawPassword, user.getUserPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        // 3) Génération du JWT
        return jwtUtil.generateToken(user.getUserName());
    }

    /**
     * Recherche un utilisateur par e-mail.
     */
    public Optional<User> findByEmail(String email) {
        return userRepo.findByUserEmail(email);
    }

    /**
     * Recherche un utilisateur par pseudo.
     */
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUserName(username);
    }
}
