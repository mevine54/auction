package com.mirs.auction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "auc_current_bid", precision = 10, scale = 2, nullable = false)
    private BigDecimal aucCurrentBid = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "auc_last_bidder_id")
    private User aucLastBidder;

    @Column(name = "auc_final_price", precision = 10, scale = 2)
    private BigDecimal aucFinalPrice;

    @Column(name = "auc_status", nullable = false)
    private String aucStatus = "en cours";

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    // — Getters & Setters —

    public Long getAucId() { return aucId; }
    public void setAucId(Long id) { this.aucId = id; }

    public LocalDateTime getAucStartTime() { return aucStartTime; }
    public void setAucStartTime(LocalDateTime t) { this.aucStartTime = t; }

    public LocalDateTime getAucEndTime() { return aucEndTime; }
    public void setAucEndTime(LocalDateTime t) { this.aucEndTime = t; }

    public BigDecimal getAucCurrentBid() { return aucCurrentBid; }
    public void setAucCurrentBid(BigDecimal b) { this.aucCurrentBid = b; }

    public User getAucLastBidder() { return aucLastBidder; }
    public void setAucLastBidder(User u) { this.aucLastBidder = u; }

    public BigDecimal getAucFinalPrice() { return aucFinalPrice; }
    public void setAucFinalPrice(BigDecimal p) { this.aucFinalPrice = p; }

    public String getAucStatus() { return aucStatus; }
    public void setAucStatus(String s) { this.aucStatus = s; }

    public User getCreator() { return creator; }
    public void setCreator(User c) { this.creator = c; }

    public Lot getLot() { return lot; }
    public void setLot(Lot l) { this.lot = l; }

    /** Renvoie l’ID du dernier enchérisseur, ou null s’il n’y en a pas. */
    public Long getAucLastBidderId() {
        return (aucLastBidder != null ? aucLastBidder.getUserId() : null);
    }
}
