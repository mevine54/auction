package com.mirs.auction.service;

import com.mirs.auction.entity.Auction;
import com.mirs.auction.entity.Bid;
import com.mirs.auction.entity.Notification;
import com.mirs.auction.entity.User;
import com.mirs.auction.repository.AuctionRepository;
import com.mirs.auction.repository.BidRepository;
import com.mirs.auction.repository.NotificationRepository;
import com.mirs.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepo;
    private final UserRepository userRepo;
    private final BidRepository bidRepo;
    private final NotificationRepository notifRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AuctionService(AuctionRepository auctionRepo,
                          UserRepository userRepo,
                          BidRepository bidRepo,
                          NotificationRepository notifRepo,
                          SimpMessagingTemplate messagingTemplate) {
        this.auctionRepo = auctionRepo;
        this.userRepo = userRepo;
        this.bidRepo = bidRepo;
        this.notifRepo = notifRepo;
        this.messagingTemplate = messagingTemplate;
    }

    public List<Auction> findAll() {
        return auctionRepo.findAll();
    }

    public Optional<Auction> findById(Long id) {
        return auctionRepo.findById(id);
    }

    /**
     * Place une mise : compare BigDecimal, gère l’ancien enchérisseur
     * et diffuse via WebSocket.
     */
    @Transactional
    public void placeBid(Long auctionId, String username, BigDecimal amount) {
        Auction auc = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Enchère introuvable : " + auctionId));
        User bidder = userRepo.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + username));

        LocalDateTime now = LocalDateTime.now();
        if (auc.getAucStartTime().isAfter(now) || auc.getAucEndTime().isBefore(now)) {
            throw new IllegalStateException("Enchère inactive (hors dates)");
        }
        if (!"en cours".equalsIgnoreCase(auc.getAucStatus())) {
            throw new IllegalStateException("Statut : " + auc.getAucStatus());
        }

        // ancien enchérisseur
        User lastBidder = auc.getAucLastBidder();
        if (lastBidder != null && lastBidder.getUserId().equals(bidder.getUserId())) {
            throw new IllegalArgumentException("Vous êtes déjà le dernier enchérisseur");
        }
        // montant minimum
        if (amount.compareTo(auc.getAucCurrentBid()) <= 0) {
            throw new IllegalArgumentException(
                    "La mise doit être > " + auc.getAucCurrentBid()
            );
        }

        // enregistrement de la mise
        Bid bid = new Bid();
        bid.setAuction(auc);
        bid.setUser(bidder);
        bid.setBidAmount(amount);
        bidRepo.save(bid);

        // mémoriser pour notification
        User previous = lastBidder;

        // mise à jour de l’enchère
        auc.setAucCurrentBid(amount);
        auc.setAucLastBidder(bidder);
        auctionRepo.save(auc);

        // notification en base – nouveau miseur
        Notification n1 = new Notification();
        n1.setAuction(auc);
        n1.setRecipient(bidder);
        n1.setNotType("BID_PLACED");
        n1.setNotMessage("Votre mise de " + amount + "€ a été enregistrée.");
        notifRepo.save(n1);

        // notification en base – ancien miseur
        if (previous != null) {
            Notification n2 = new Notification();
            n2.setAuction(auc);
            n2.setRecipient(previous);
            n2.setNotType("OVERBID");
            n2.setNotMessage("Vous avez été surenchéri sur l'enchère #"
                    + auctionId + " : " + amount + "€.");
            notifRepo.save(n2);
        }

        // diffusion temps réel
        messagingTemplate.convertAndSend(
                "/topic/auctions/" + auctionId,
                auc
        );
    }
}
