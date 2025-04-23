package com.mirs.auction.service;

import com.mirs.auction.entity.Auction;
import com.mirs.auction.entity.Bid;
import com.mirs.auction.entity.User;
import com.mirs.auction.repository.AuctionRepository;
import com.mirs.auction.repository.BidRepository;
import com.mirs.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuctionService {
    @Autowired private AuctionRepository auctionRepo;
    @Autowired private BidRepository bidRepo;
    @Autowired private UserRepository userRepo;

    @Transactional
    public Bid placeBid(Long aucId, Long userId, BigDecimal increment) {
        Auction auc = auctionRepo.findById(aucId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(auc.getAucStartTime()) || now.isAfter(auc.getAucEndTime())) {
            throw new RuntimeException("Auction not active");
        }
        if (auc.getAucLastBidder() != null && auc.getAucLastBidder().getUserId().equals(userId)) {
            throw new RuntimeException("You are already the highest bidder");
        }
        BigDecimal newPrice = BigDecimal.valueOf(Optional.ofNullable(auc.getAucCurrentBid()).orElse(0.0))
                .add(increment);
        User bidder = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Bid bid = new Bid();
        bid.setBidAmount(newPrice.doubleValue());
        bid.setBidTime(now);
        bid.setAuction(auc);
        bid.setBidder(bidder);
        Bid saved = bidRepo.save(bid);
        auc.setAucCurrentBid(newPrice.doubleValue());
        auc.setAucLastBidder(bidder);
        auctionRepo.save(auc);
        return saved;
    }

    @Transactional
    public void closeAuction(Long aucId) {
        Auction auc = auctionRepo.findById(aucId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        if (LocalDateTime.now().isBefore(auc.getAucEndTime())) {
            throw new RuntimeException("Auction not yet ended");
        }
        auc.setAucStatus("terminee");
        auc.setAucFinalPrice(auc.getAucCurrentBid());
        auctionRepo.save(auc);
    }
}

