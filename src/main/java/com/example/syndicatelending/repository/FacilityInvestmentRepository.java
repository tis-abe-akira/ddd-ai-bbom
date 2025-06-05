// FacilityInvestmentRepositoryのpackageを変更
package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.FacilityInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityInvestmentRepository extends JpaRepository<FacilityInvestment, Long> {
    void deleteByFacilityId(Long facilityId);
}
