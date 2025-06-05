// SharePieRepositoryのpackageを変更
package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.SharePie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SharePieRepository extends JpaRepository<SharePie, Long> {
    List<SharePie> findByFacility_Id(Long facilityId);

    void deleteByFacility_Id(Long facilityId);
}
