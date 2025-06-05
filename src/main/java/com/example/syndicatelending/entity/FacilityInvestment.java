// FacilityInvestmentのpackageを変更
package com.example.syndicatelending.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "facility_investment")
public class FacilityInvestment extends Transaction {

    @Column(nullable = false)
    private Long investorId;

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }
}
