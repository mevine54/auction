package com.mirs.auction.controller;

import com.mirs.auction.dto.BidDTO;
import com.mirs.auction.entity.Auction;
import com.mirs.auction.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    /**
     * Renvoie la liste de toutes les enchères.
     * GET /api/auctions
     */
    @GetMapping
    public ResponseEntity<List<Auction>> findAll() {
        return ResponseEntity.ok(auctionService.findAll());
    }

    /**
     * Renvoie les détails d'une enchère.
     * GET /api/auctions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Auction> getOne(@PathVariable Long id) {
        return auctionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Place une mise via HTTP POST.
     * POST /api/auctions/{id}/bid
     */
    @PostMapping("/{id}/bid")
    public ResponseEntity<Void> placeBid(
            @PathVariable Long id,
            @Valid @RequestBody BidDTO bidDto,
            Principal principal
    ) {
        auctionService.placeBid(
                id,
                principal.getName(),
                bidDto.getAmount()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Place une mise en temps réel via STOMP.
     * Destiné aux requêtes WebSocket sur /app/auction/bid
     */
    @MessageMapping("/auction/bid")
    public void placeBidRealtime(@Valid BidDTO bidDto, Principal principal) {
        auctionService.placeBid(
                bidDto.getAuctionId(),
                principal.getName(),
                bidDto.getAmount()
        );
        // les notifications WS sont émises par le service
    }

    // ————— Gestion centralisée des exceptions métiers —————

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<String> handleError(RuntimeException ex) {
        // Renvoie 400 + message d'erreur
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
