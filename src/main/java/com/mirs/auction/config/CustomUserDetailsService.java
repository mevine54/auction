package com.mirs.auction.config;

import com.mirs.auction.entity.User;
import com.mirs.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Recherche par pseudo, puis email
        User user = userRepo.findByUserName(usernameOrEmail)
                .orElseGet(() -> userRepo.findByUserEmail(usernameOrEmail)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail)
                        ));

        // On peut ajouter des rôles selon user.getUserStatus() ou un champ roles
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getUserPassword(),
                user.isUserEmailVerified(),  // enabled si email vérifié
                true,                         // accountNonExpired
                true,                         // credentialsNonExpired
                !"suspendu".equals(user.getUserStatus()), // accountNonLocked si status pas suspendu
                Collections.singletonList(authority)
        );
    }
}
