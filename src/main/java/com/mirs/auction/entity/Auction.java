package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aucId;

    @Column(name = "auc_start_time", nullable = false)
    private LocalDateTime aucStartTime;

    @Column(name = "auc_end_time", nullable = false)
    private LocalDateTime aucEndTime;

    @Column(name = "auc_current_bid")
    private Double aucCurrentBid = 0.0;

    @ManyToOne
    @JoinColumn(name = "auc_last_bidder_id")
    private User aucLastBidder;

    @Column(name = "auc_final_price")
    private Double aucFinalPrice;

    @Column(name = "auc_status")
    private String aucStatus = "en cours";

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    public Long getAucId() {
        return aucId;
    }
    public void setAucId(Long aucId) {
        this.aucId = aucId;
    }
    public LocalDateTime getAucStartTime() {
        return aucStartTime;
    }
    public void setAucStartTime(LocalDateTime aucStartTime) {
        this.aucStartTime = aucStartTime;
    }
    public LocalDateTime getAucEndTime() {
        return aucEndTime;
    }
    public void setAucEndTime(LocalDateTime aucEndTime) {
        this.aucEndTime = aucEndTime;
    }
    public Double getAucCurrentBid() {
        return aucCurrentBid;
    }
    public void setAucCurrentBid(Double aucCurrentBid) {
        this.aucCurrentBid = aucCurrentBid;
    }
    public User getAucLastBidder() {
        return aucLastBidder;
    }
    public void setAucLastBidder(User aucLastBidder) {
        this.aucLastBidder = aucLastBidder;
    }
    public Double getAucFinalPrice() {
        return aucFinalPrice;
    }
    public void setAucFinalPrice(Double aucFinalPrice) {
        this.aucFinalPrice = aucFinalPrice;
    }
    public String getAucStatus() {
        return aucStatus;
    }
    public void setAucStatus(String aucStatus) {
        this.aucStatus = aucStatus;
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public Lot getLot() {
        return lot;
    }
    public void setLot(Lot lot) {
        this.lot = lot;
    }
}
