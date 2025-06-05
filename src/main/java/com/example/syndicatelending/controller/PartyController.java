package com.example.syndicatelending.controller;

import com.example.syndicatelending.repository.CompanyRepository;
import com.example.syndicatelending.repository.BorrowerRepository;
import com.example.syndicatelending.repository.InvestorRepository;
import com.example.syndicatelending.dto.*;
import com.example.syndicatelending.entity.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parties")
@Tag(name = "Party Management")
public class PartyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private InvestorRepository investorRepository;

    // --- Company endpoints ---
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@RequestBody CreateCompanyRequest request) {
        try {
            Company company = new Company(
                    request.getCompanyName(),
                    request.getRegistrationNumber(),
                    request.getIndustry(),
                    request.getAddress(),
                    request.getCountry());
            Company saved = companyRepository.save(company);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompany(@PathVariable Long id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
            return ResponseEntity.ok(company);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies(Pageable pageable) {
        try {
            return ResponseEntity.ok(companyRepository.findAll(pageable));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody UpdateCompanyRequest request) {
        try {
            Company existing = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
            Company entityToSave = new Company();
            entityToSave.setId(id);
            entityToSave.setVersion(request.getVersion());
            entityToSave.setCompanyName(request.getCompanyName());
            entityToSave.setRegistrationNumber(request.getRegistrationNumber());
            entityToSave.setIndustry(request.getIndustry());
            entityToSave.setAddress(request.getAddress());
            entityToSave.setCountry(request.getCountry());
            entityToSave.setCreatedAt(existing.getCreatedAt());
            Company updated = companyRepository.save(entityToSave);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            if (!companyRepository.existsById(id)) {
                throw new RuntimeException("Company not found with ID: " + id);
            }
            companyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    // --- Borrower endpoints ---
    @PostMapping("/borrowers")
    public ResponseEntity<?> createBorrower(@RequestBody CreateBorrowerRequest request) {
        try {
            Borrower borrower = new Borrower();
            borrower.setName(request.getName());
            borrower.setEmail(request.getEmail());
            borrower.setPhoneNumber(request.getPhoneNumber());
            borrower.setCompanyId(request.getCompanyId());
            borrower.setCreditLimit(request.getCreditLimit());
            borrower.setCreditRating(request.getCreditRating());
            borrowerRepository.save(borrower);
            return ResponseEntity.status(201).body(borrower);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/borrowers/{id}")
    public ResponseEntity<?> getBorrower(@PathVariable Long id) {
        try {
            Borrower borrower = borrowerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + id));
            return ResponseEntity.ok(borrower);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/borrowers")
    public ResponseEntity<?> getAllBorrowers(Pageable pageable) {
        try {
            return ResponseEntity.ok(borrowerRepository.findAll(pageable));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @PutMapping("/borrowers/{id}")
    public ResponseEntity<?> updateBorrower(@PathVariable Long id, @RequestBody UpdateBorrowerRequest request) {
        try {
            Borrower existing = borrowerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + id));
            Borrower entityToSave = new Borrower();
            entityToSave.setId(id);
            entityToSave.setVersion(request.getVersion());
            entityToSave.setName(request.getName());
            entityToSave.setEmail(request.getEmail());
            entityToSave.setPhoneNumber(request.getPhoneNumber());
            entityToSave.setCompanyId(request.getCompanyId());
            entityToSave.setCreditLimit(request.getCreditLimit());
            entityToSave.setCreditRating(request.getCreditRating());
            entityToSave.setCreatedAt(existing.getCreatedAt());
            Borrower updated = borrowerRepository.save(entityToSave);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/borrowers/{id}")
    public ResponseEntity<?> deleteBorrower(@PathVariable Long id) {
        try {
            if (!borrowerRepository.existsById(id)) {
                throw new RuntimeException("Borrower not found with ID: " + id);
            }
            borrowerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    // --- Investor endpoints ---
    @PostMapping("/investors")
    public ResponseEntity<?> createInvestor(@RequestBody CreateInvestorRequest request) {
        try {
            Investor investor = new Investor();
            investor.setName(request.getName());
            investor.setEmail(request.getEmail());
            investor.setPhoneNumber(request.getPhoneNumber());
            investor.setCompanyId(request.getCompanyId());
            investor.setInvestmentCapacity(request.getInvestmentCapacity());
            investor.setInvestorType(request.getInvestorType());
            investorRepository.save(investor);
            return ResponseEntity.status(201).body(investor);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/investors/{id}")
    public ResponseEntity<?> getInvestor(@PathVariable Long id) {
        try {
            Investor investor = investorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Investor not found with ID: " + id));
            return ResponseEntity.ok(investor);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/investors")
    public ResponseEntity<?> getAllInvestors(Pageable pageable) {
        try {
            return ResponseEntity.ok(investorRepository.findAll(pageable));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @PutMapping("/investors/{id}")
    public ResponseEntity<?> updateInvestor(@PathVariable Long id, @RequestBody UpdateInvestorRequest request) {
        try {
            Investor existing = investorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Investor not found with ID: " + id));
            Investor entityToSave = new Investor();
            entityToSave.setId(id);
            entityToSave.setVersion(request.getVersion());
            entityToSave.setName(request.getName());
            entityToSave.setEmail(request.getEmail());
            entityToSave.setPhoneNumber(request.getPhoneNumber());
            entityToSave.setCompanyId(request.getCompanyId());
            entityToSave.setInvestmentCapacity(request.getInvestmentCapacity());
            entityToSave.setInvestorType(request.getInvestorType());
            entityToSave.setCreatedAt(existing.getCreatedAt());
            Investor updated = investorRepository.save(entityToSave);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/investors/{id}")
    public ResponseEntity<?> deleteInvestor(@PathVariable Long id) {
        try {
            if (!investorRepository.existsById(id)) {
                throw new RuntimeException("Investor not found with ID: " + id);
            }
            investorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }
}
