package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @Column(name = "bid_amount", nullable = false)
    private Double bidAmount;

    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "auc_id")
    private Auction auction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User bidder;

    public Long getBidId() {
        return bidId;
    }
    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }
    public Double getBidAmount() {
        return bidAmount;
    }
    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }
    public LocalDateTime getBidTime() {
        return bidTime;
    }
    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
    public Auction getAuction() {
        return auction;
    }
    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    public User getBidder() {
        return bidder;
    }
    public void setBidder(User bidder) {
        this.bidder = bidder;
    }
}
