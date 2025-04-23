package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notId;

    @Column(name = "not_read")
    private boolean notRead = false;

    @Column(name = "not_message")
    private String notMessage;

    @Column(name = "not_type")
    private String notType;

    @Column(name = "not_sent_at")
    private LocalDateTime notSentAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "auc_id")
    private Auction auction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User recipient;

    public Long getNotId() {
        return notId;
    }
    public void setNotId(Long notId) {
        this.notId = notId;
    }
    public boolean isNotRead() {
        return notRead;
    }
    public void setNotRead(boolean notRead) {
        this.notRead = notRead;
    }
    public String getNotMessage() {
        return notMessage;
    }
    public void setNotMessage(String notMessage) {
        this.notMessage = notMessage;
    }
    public String getNotType() {
        return notType;
    }
    public void setNotType(String notType) {
        this.notType = notType;
    }
    public LocalDateTime getNotSentAt() {
        return notSentAt;
    }
    public void setNotSentAt(LocalDateTime notSentAt) {
        this.notSentAt = notSentAt;
    }
    public Auction getAuction() {
        return auction;
    }
    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
