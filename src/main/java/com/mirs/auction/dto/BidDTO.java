package com.mirs.auction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BidDTO {
    @NotNull
    private Long auctionId;

    @NotNull
    @DecimalMin(value="0.01", message="Mise minimale 0.01€")
    private BigDecimal amount;

    public BidDTO() {}
    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

