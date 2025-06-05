package com.example.syndicatelending.dto;

import java.time.LocalDate;
import java.util.List;
import com.example.syndicatelending.common.domain.model.Money;
import com.example.syndicatelending.common.domain.model.Percentage;

public class CreateFacilityRequest {
    private Long syndicateId;
    private Money commitment;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String interestTerms;
    private List<SharePieRequest> sharePies;

    public CreateFacilityRequest() {
    }

    public CreateFacilityRequest(Long syndicateId, Money commitment, String currency,
            LocalDate startDate, LocalDate endDate, String interestTerms,
            List<SharePieRequest> sharePies) {
        this.syndicateId = syndicateId;
        this.commitment = commitment;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interestTerms = interestTerms;
        this.sharePies = sharePies;
    }

    public Long getSyndicateId() {
        return syndicateId;
    }

    public void setSyndicateId(Long syndicateId) {
        this.syndicateId = syndicateId;
    }

    public Money getCommitment() {
        return commitment;
    }

    public void setCommitment(Money commitment) {
        this.commitment = commitment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getInterestTerms() {
        return interestTerms;
    }

    public void setInterestTerms(String interestTerms) {
        this.interestTerms = interestTerms;
    }

    public List<SharePieRequest> getSharePies() {
        return sharePies;
    }

    public void setSharePies(List<SharePieRequest> sharePies) {
        this.sharePies = sharePies;
    }

    public static class SharePieRequest {
        private Long investorId;
        private Percentage share;

        public Long getInvestorId() {
            return investorId;
        }

        public void setInvestorId(Long investorId) {
            this.investorId = investorId;
        }

        public Percentage getShare() {
            return share;
        }

        public void setShare(Percentage share) {
            this.share = share;
        }
    }
}
