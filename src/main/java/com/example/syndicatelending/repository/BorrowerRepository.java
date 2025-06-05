package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.Borrower;
import com.example.syndicatelending.entity.CreditRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long>, JpaSpecificationExecutor<Borrower> {
    List<Borrower> findByNameContainingIgnoreCase(String name);

    List<Borrower> findByCreditRating(String creditRating);

    Page<Borrower> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Borrower> findByCreditRating(CreditRating creditRating, Pageable pageable);
}
