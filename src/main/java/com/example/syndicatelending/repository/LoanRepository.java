package com.example.syndicatelending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.syndicatelending.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Custom query methods can be defined here
}
// 空ファイル（loan/repository/LoanRepositoryを参照）
