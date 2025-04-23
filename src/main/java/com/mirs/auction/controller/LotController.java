package com.mirs.auction.controller;

import com.mirs.auction.entity.Lot;
import com.mirs.auction.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
public class LotController {

    @Autowired
    private LotService lotService;

    // GET /api/lots
    @GetMapping
    public ResponseEntity<List<Lot>> findAll() {
        return ResponseEntity.ok(lotService.findAll());
    }

    // POST /api/lots
    @PostMapping
    public ResponseEntity<Lot> create(@RequestBody Lot lot) {
        Lot created = lotService.create(lot);
        return ResponseEntity.status(201).body(created);
    }

    // GET /api/lots/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Lot> getOne(@PathVariable Long id) {
        return lotService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/lots/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Lot> update(@PathVariable Long id,
                                      @RequestBody Lot lot) {
        return lotService.update(id, lot)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/lots/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (lotService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
