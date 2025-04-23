package com.mirs.auction.controller;

import com.mirs.auction.dto.UserDTO;
import com.mirs.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        try {
            var created = userService.register(dto.toEntity());
            return ResponseEntity
                    .status(201)
                    .body(UserDTO.fromEntity(created));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserDTO dto) {
//        try {
//            String jwt = userService.authenticate(dto.getUserName(), dto.getUserPassword());
//            return ResponseEntity.ok().body(Map.of("token", jwt));
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(401)
//                    .body("Identifiants invalides");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO dto) {
        try {
            String jwt = userService.authenticate(dto.getUserName(), dto.getUserPassword());
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (RuntimeException e) {
            // Renvoie maintenant le vrai message pour debug
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
