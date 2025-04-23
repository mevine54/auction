package com.mirs.auction.service;

import com.mirs.auction.entity.Lot;
import com.mirs.auction.repository.LotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour gérer les Lots (CRUD).
 */
@Service
public class LotService {

    private final LotRepository lotRepo;

    @Autowired
    public LotService(LotRepository lotRepo) {
        this.lotRepo = lotRepo;
    }

    /**
     * Récupère tous les lots.
     */
    public List<Lot> findAll() {
        return lotRepo.findAll();
    }

    /**
     * Crée un nouveau lot.
     */
    public Lot create(Lot lot) {
        return lotRepo.save(lot);
    }

    /**
     * Cherche un lot par son ID.
     */
    public Optional<Lot> findById(Long id) {
        return lotRepo.findById(id);
    }

    /**
     * Met à jour un lot existant.
     * @return Optional vide si non trouvé, ou le lot mis à jour.
     */
    public Optional<Lot> update(Long id, Lot updated) {
        return lotRepo.findById(id).map(lot -> {
            lot.setLotName(updated.getLotName());
            lot.setLotDescription(updated.getLotDescription());
            lot.setLotStartingPrice(updated.getLotStartingPrice());
            return lotRepo.save(lot);
        });
    }

    /**
     * Supprime un lot.
     * @return true si supprimé, false si non existant.
     */
    public boolean delete(Long id) {
        if (lotRepo.existsById(id)) {
            lotRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

