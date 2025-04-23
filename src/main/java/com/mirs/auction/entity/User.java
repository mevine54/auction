package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt = LocalDateTime.now();

    @Column(name = "user_email_verified")
    private boolean userEmailVerified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "user_status")
    private String userStatus = "actif";

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
    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }
    public void setUserCreatedAt(LocalDateTime userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }
    public boolean isUserEmailVerified() {
        return userEmailVerified;
    }
    public void setUserEmailVerified(boolean userEmailVerified) {
        this.userEmailVerified = userEmailVerified;
    }
    public String getVerificationToken() {
        return verificationToken;
    }
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}

