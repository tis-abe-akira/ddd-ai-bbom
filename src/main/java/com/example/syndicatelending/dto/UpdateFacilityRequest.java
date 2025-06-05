package com.example.syndicatelending.dto;

import java.time.LocalDate;
import java.util.List;
import com.example.syndicatelending.common.domain.model.Money;

/**
 * Facility更新用リクエストDTO。
 * 楽観的排他制御のためにversionフィールドを含む。
 */
public class UpdateFacilityRequest {
    private Long syndicateId;
    private Money commitment;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String interestTerms;
    private List<SharePieRequest> sharePies;
    private Long version; // 楽観的排他制御用

    // デフォルトコンストラクタ
    public UpdateFacilityRequest() {
    }

    public UpdateFacilityRequest(Long syndicateId, Money commitment, String currency,
            LocalDate startDate, LocalDate endDate, String interestTerms,
            List<SharePieRequest> sharePies, Long version) {
        this.syndicateId = syndicateId;
        this.commitment = commitment;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.interestTerms = interestTerms;
        this.sharePies = sharePies;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    // SharePieRequestを内部クラスとして定義
    public static class SharePieRequest {
        private Long investorId;
        private com.example.syndicatelending.common.domain.model.Percentage share;

        public Long getInvestorId() {
            return investorId;
        }

        public void setInvestorId(Long investorId) {
            this.investorId = investorId;
        }

        public com.example.syndicatelending.common.domain.model.Percentage getShare() {
            return share;
        }

        public void setShare(com.example.syndicatelending.common.domain.model.Percentage share) {
            this.share = share;
        }
    }
}
