package com.mirs.auction.dto;

import com.mirs.auction.entity.User;

/**
 * Data Transfer Object pour l'inscription et l'authentification.
 */
public class UserDTO {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userPhone;

    public UserDTO() {}

    // Constructeur à partir de l'entité
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        // Ne jamais renvoyer le mot de passe en clair dans un DTO de réponse !
        dto.setUserPhone(user.getUserPhone());
        return dto;
    }

    // Conversion DTO → Entité (pour l'enregistrement)
    public User toEntity() {
        User user = new User();
        user.setUserName(this.userName);
        user.setUserEmail(this.userEmail);
        user.setUserPassword(this.userPassword);
        user.setUserPhone(this.userPhone);
        return user;
    }

    // Getters & Setters

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
