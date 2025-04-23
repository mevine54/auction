package com.mirs.auction.security;

import com.mirs.auction.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtre qui intercepte chaque requête, extrait le JWT du header Authorization,
 * le valide via JwtUtil, et positionne l'Authentication dans le contexte Spring.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * On injecte JwtUtil (contenant le SecretKey et la logique de parseToken)
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // Parse et valide le JWT
                Jws<Claims> claimsJws = jwtUtil.parseToken(token);
                String username = claimsJws.getBody().getSubject();

                // Crée un Authentication avec un rôle USER
                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ex) {
                // Token invalide ou expiré → on nettoie le contexte
                SecurityContextHolder.clearContext();
            }
        }
        // On poursuit la chaîne de filtres
        chain.doFilter(req, res);
    }
}
