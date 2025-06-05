package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.Drawdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawdownRepository extends JpaRepository<Drawdown, Long> {
    List<Drawdown> findByFacilityId(Long facilityId);
    List<Drawdown> findByLoanId(Long loanId);
    List<Drawdown> findByBorrowerId(Long borrowerId);
}
