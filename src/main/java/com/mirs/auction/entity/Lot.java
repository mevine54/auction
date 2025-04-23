package com.mirs.auction.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lots")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    @Column(name = "lot_name", nullable = false)
    private String lotName;

    @Column(name = "lot_description")
    private String lotDescription;

    @Column(name = "lot_starting_price", nullable = false)
    private Double lotStartingPrice;

    public Long getLotId() {
        return lotId;
    }
    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }
    public String getLotName() {
        return lotName;
    }
    public void setLotName(String lotName) {
        this.lotName = lotName;
    }
    public String getLotDescription() {
        return lotDescription;
    }
    public void setLotDescription(String lotDescription) {
        this.lotDescription = lotDescription;
    }
    public Double getLotStartingPrice() {
        return lotStartingPrice;
    }
    public void setLotStartingPrice(Double lotStartingPrice) {
        this.lotStartingPrice = lotStartingPrice;
    }
}
