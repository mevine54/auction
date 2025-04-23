package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @Column(name = "bid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal bidAmount;

    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "auc_id")
    private Auction auction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // — Getters & Setters —

    public Long getBidId() { return bidId; }
    public void setBidId(Long id) { this.bidId = id; }

    public BigDecimal getBidAmount() { return bidAmount; }
    public void setBidAmount(BigDecimal amt) { this.bidAmount = amt; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime t) { this.bidTime = t; }

    public Auction getAuction() { return auction; }
    public void setAuction(Auction a) { this.auction = a; }

    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
}
