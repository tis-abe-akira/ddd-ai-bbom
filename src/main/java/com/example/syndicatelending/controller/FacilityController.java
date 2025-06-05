// FacilityControllerのpackageを変更し、importを全てfacility配下から直下のステレオタイプパッケージに修正
package com.example.syndicatelending.controller;

// import文を修正
import com.example.syndicatelending.dto.CreateFacilityRequest;
import com.example.syndicatelending.dto.UpdateFacilityRequest;
import com.example.syndicatelending.entity.Facility;
import com.example.syndicatelending.entity.SharePie;
import com.example.syndicatelending.entity.FacilityInvestment;
import com.example.syndicatelending.repository.FacilityRepository;
import com.example.syndicatelending.repository.SharePieRepository;
import com.example.syndicatelending.repository.FacilityInvestmentRepository;
import com.example.syndicatelending.domain.FacilityValidator;
import com.example.syndicatelending.repository.SyndicateRepository;
import com.example.syndicatelending.entity.Syndicate;
import com.example.syndicatelending.common.application.exception.ResourceNotFoundException;
import com.example.syndicatelending.common.application.exception.BusinessRuleViolationException;
import com.example.syndicatelending.common.domain.model.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private SharePieRepository sharePieRepository;
    @Autowired
    private FacilityInvestmentRepository facilityInvestmentRepository;
    @Autowired
    private FacilityValidator facilityValidator;
    @Autowired
    private SyndicateRepository syndicateRepository;

    @PostMapping
    public ResponseEntity<?> createFacility(@RequestBody CreateFacilityRequest request) {
        try {
            Facility facility = new Facility(
                    request.getSyndicateId(),
                    request.getCommitment(),
                    request.getCurrency(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getInterestTerms());
            List<SharePie> sharePies = new ArrayList<>();
            for (CreateFacilityRequest.SharePieRequest pie : request.getSharePies()) {
                SharePie entity = new SharePie();
                entity.setInvestorId(pie.getInvestorId());
                entity.setShare(pie.getShare());
                entity.setFacility(facility);
                sharePies.add(entity);
            }
            facility.setSharePies(sharePies);
            facilityValidator.validateCreateFacilityRequest(request);
            Facility savedFacility = facilityRepository.save(facility);
            List<FacilityInvestment> investments = new ArrayList<>();
            Money commitment = savedFacility.getCommitment();
            Syndicate syndicate = syndicateRepository.findById(savedFacility.getSyndicateId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Syndicate not found with id: " + savedFacility.getSyndicateId()));
            Long borrowerId = syndicate.getBorrowerId();
            for (SharePie pie : savedFacility.getSharePies()) {
                FacilityInvestment investment = new FacilityInvestment();
                investment.setFacilityId(savedFacility.getId());
                investment.setInvestorId(pie.getInvestorId());
                investment.setBorrowerId(borrowerId);
                investment.setAmount(commitment.multiply(pie.getShare().getValue()));
                investment.setTransactionType("FACILITY_INVESTMENT");
                investment.setTransactionDate(LocalDate.now());
                investments.add(investment);
            }
            facilityInvestmentRepository.saveAll(investments);
            return ResponseEntity.ok(savedFacility);
        } catch (BusinessRuleViolationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFacilities(Pageable pageable) {
        try {
            Page<Facility> facilities = facilityRepository.findAll(pageable);
            return ResponseEntity.ok(facilities);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacilityById(@PathVariable Long id) {
        try {
            Facility facility = facilityRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));
            return ResponseEntity.ok(facility);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacility(@PathVariable Long id, @RequestBody UpdateFacilityRequest request) {
        try {
            Facility existingFacility = facilityRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));
            facilityValidator.validateUpdateFacilityRequest(request, id);
            Facility entityToSave = new Facility();
            entityToSave.setId(id);
            entityToSave.setVersion(request.getVersion());
            entityToSave.setSyndicateId(request.getSyndicateId());
            entityToSave.setCommitment(request.getCommitment());
            entityToSave.setCurrency(request.getCurrency());
            entityToSave.setStartDate(request.getStartDate());
            entityToSave.setEndDate(request.getEndDate());
            entityToSave.setInterestTerms(request.getInterestTerms());
            entityToSave.setCreatedAt(existingFacility.getCreatedAt());
            existingFacility.getSharePies().clear();
            sharePieRepository.deleteByFacility_Id(id);
            List<SharePie> newSharePies = new ArrayList<>();
            for (UpdateFacilityRequest.SharePieRequest pie : request.getSharePies()) {
                SharePie entity = new SharePie();
                entity.setInvestorId(pie.getInvestorId());
                entity.setShare(pie.getShare());
                entity.setFacility(entityToSave);
                newSharePies.add(entity);
            }
            entityToSave.setSharePies(newSharePies);
            Facility savedFacility = facilityRepository.save(entityToSave);
            facilityInvestmentRepository.deleteByFacilityId(id);
            List<FacilityInvestment> newInvestments = new ArrayList<>();
            Money newCommitment = savedFacility.getCommitment();
            Syndicate syndicate = syndicateRepository.findById(savedFacility.getSyndicateId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Syndicate not found with id: " + savedFacility.getSyndicateId()));
            Long borrowerId = syndicate.getBorrowerId();
            for (SharePie pie : savedFacility.getSharePies()) {
                FacilityInvestment investment = new FacilityInvestment();
                investment.setFacilityId(savedFacility.getId());
                investment.setInvestorId(pie.getInvestorId());
                investment.setBorrowerId(borrowerId);
                investment.setAmount(newCommitment.multiply(pie.getShare().getValue()));
                investment.setTransactionType("FACILITY_INVESTMENT");
                investment.setTransactionDate(LocalDate.now());
                newInvestments.add(investment);
            }
            facilityInvestmentRepository.saveAll(newInvestments);
            return ResponseEntity.ok(savedFacility);
        } catch (BusinessRuleViolationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacility(@PathVariable Long id) {
        try {
            if (!facilityRepository.existsById(id)) {
                throw new ResourceNotFoundException("Facility not found with id: " + id);
            }
            facilityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }
}
