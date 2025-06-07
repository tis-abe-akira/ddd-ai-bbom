package com.example.syndicatelending.controller;

import com.example.syndicatelending.dto.CreateSyndicateRequest;
import com.example.syndicatelending.dto.UpdateSyndicateRequest;
import com.example.syndicatelending.entity.Syndicate;
import com.example.syndicatelending.repository.SyndicateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/syndicates")
public class SyndicateController {
    @Autowired
    private SyndicateRepository syndicateRepository;

    @PostMapping
    public ResponseEntity<?> createSyndicate(@RequestBody CreateSyndicateRequest request) {
        try {
            Syndicate syndicate = new Syndicate(request.getName(), request.getLeadBankId(), request.getBorrowerId(),
                    request.getMemberInvestorIds());
            Syndicate saved = syndicateRepository.save(syndicate);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSyndicate(@PathVariable Long id) {
        Optional<Syndicate> syndicate = syndicateRepository.findById(id);
        if (syndicate.isPresent()) {
            return ResponseEntity.ok(syndicate.get());
        } else {
            return ResponseEntity.status(404).body("Syndicate not found with ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSyndicates(Pageable pageable) {
        return ResponseEntity.ok(syndicateRepository.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSyndicate(@PathVariable Long id, @RequestBody UpdateSyndicateRequest request) {
        Optional<Syndicate> existingOpt = syndicateRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Syndicate not found with ID: " + id);
        }
        Syndicate existing = existingOpt.get();
        Syndicate entityToSave = new Syndicate();
        entityToSave.setId(id);
        entityToSave.setVersion(request.getVersion());
        entityToSave.setName(request.getName());
        entityToSave.setLeadBankId(request.getLeadBankId());
        entityToSave.setBorrowerId(request.getBorrowerId());
        entityToSave.setMemberInvestorIds(request.getMemberInvestorIds());
        entityToSave.setCreatedAt(existing.getCreatedAt());
        Syndicate updated = syndicateRepository.save(entityToSave);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSyndicate(@PathVariable Long id) {
        if (!syndicateRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Syndicate not found with ID: " + id);
        }
        syndicateRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
