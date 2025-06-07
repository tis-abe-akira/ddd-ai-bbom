package com.example.syndicatelending.controller;

import com.example.syndicatelending.dto.CreateDrawdownRequest;
import com.example.syndicatelending.entity.Drawdown;
import com.example.syndicatelending.entity.Loan;
import com.example.syndicatelending.repository.DrawdownRepository;
import com.example.syndicatelending.repository.LoanRepository;
import com.example.syndicatelending.common.domain.model.Money;
import com.example.syndicatelending.common.domain.model.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans/drawdowns")
public class DrawdownController {
    @Autowired
    private DrawdownRepository drawdownRepository;
    @Autowired
    private LoanRepository loanRepository;

    @PostMapping
    public ResponseEntity<?> createDrawdown(@RequestBody CreateDrawdownRequest request) {
        try {
            Loan loan = new Loan();
            loan.setFacilityId(request.getFacilityId());
            loan.setBorrowerId(request.getBorrowerId());
            loan.setPrincipalAmount(Money.of(request.getAmount()));
            loan.setOutstandingBalance(Money.of(request.getAmount()));
            loan.setAnnualInterestRate(Percentage.of(request.getAnnualInterestRate()));
            loan.setDrawdownDate(request.getDrawdownDate());
            loan.setRepaymentPeriodMonths(request.getRepaymentPeriodMonths());
            loan.setRepaymentCycle(request.getRepaymentCycle());
            loan.setRepaymentMethod(request.getRepaymentMethod());
            loan.setCurrency(request.getCurrency());
            Loan savedLoan = loanRepository.save(loan);
            Drawdown drawdown = new Drawdown();
            drawdown.setLoanId(savedLoan.getId());
            drawdown.setCurrency(request.getCurrency());
            drawdown.setPurpose(request.getPurpose());
            drawdown.setFacilityId(request.getFacilityId());
            drawdown.setBorrowerId(request.getBorrowerId());
            drawdown.setTransactionDate(request.getDrawdownDate());
            drawdown.setAmount(Money.of(request.getAmount()));
            Drawdown savedDrawdown = drawdownRepository.save(drawdown);
            return ResponseEntity.status(201).body(savedDrawdown);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDrawdowns(Pageable pageable) {
        try {
            Page<Drawdown> drawdowns = drawdownRepository.findAll(pageable);
            return ResponseEntity.ok(drawdowns);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDrawdownById(@PathVariable Long id) {
        try {
            Drawdown drawdown = drawdownRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Drawdown not found with ID: " + id));
            return ResponseEntity.ok(drawdown);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<?> getDrawdownsByFacilityId(@PathVariable Long facilityId) {
        try {
            List<Drawdown> drawdowns = drawdownRepository.findByFacilityId(facilityId);
            return ResponseEntity.ok(drawdowns);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }
}
