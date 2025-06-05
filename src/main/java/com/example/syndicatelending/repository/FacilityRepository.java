// FacilityRepositoryのpackageを変更
package com.example.syndicatelending.repository;

import com.example.syndicatelending.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    /**
     * 指定されたSyndicateに関連付けられたFacilityリストを取得
     */
    List<Facility> findBySyndicateId(Long syndicateId);
}
