package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.Company;
import com.example.syndicatelending.entity.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);

    Optional<Company> findByRegistrationNumber(String registrationNumber);

    List<Company> findByIndustry(String industry);

    List<Company> findByCountry(String country);

    Page<Company> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<Company> findByIndustry(Industry industry, Pageable pageable);
}
