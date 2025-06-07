// FacilityValidatorのpackageを変更し、importを全てfacility配下から直下のステレオタイプパッケージに修正
package com.example.syndicatelending.domain;

import com.example.syndicatelending.dto.CreateFacilityRequest;
import com.example.syndicatelending.dto.UpdateFacilityRequest;
import com.example.syndicatelending.common.application.exception.BusinessRuleViolationException;
import com.example.syndicatelending.repository.InvestorRepository;
import com.example.syndicatelending.repository.BorrowerRepository;
import com.example.syndicatelending.repository.SyndicateRepository;
import com.example.syndicatelending.entity.Borrower;
import com.example.syndicatelending.entity.Syndicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacilityValidator {
    
    @Autowired
    private InvestorRepository investorRepository;
    
    @Autowired
    private BorrowerRepository borrowerRepository;
    
    @Autowired
    private SyndicateRepository syndicateRepository;

    public void validateCreateFacilityRequest(CreateFacilityRequest request) {
        if (request == null)
            throw new BusinessRuleViolationException("リクエストがnullです");
        if (request.getSyndicateId() == null)
            throw new BusinessRuleViolationException("syndicateIdは必須です");
        if (request.getCommitment() == null || request.getCommitment().isZero())
            throw new BusinessRuleViolationException("commitmentは必須かつ0より大きい必要があります");
        if (request.getCurrency() == null || request.getCurrency().isBlank())
            throw new BusinessRuleViolationException("currencyは必須です");
        if (request.getStartDate() == null)
            throw new BusinessRuleViolationException("startDateは必須です");
        if (request.getEndDate() == null)
            throw new BusinessRuleViolationException("endDateは必須です");
        if (request.getEndDate().isBefore(request.getStartDate()))
            throw new BusinessRuleViolationException("endDateはstartDate以降でなければなりません");
        if (request.getSharePies() == null || request.getSharePies().isEmpty())
            throw new BusinessRuleViolationException("sharePiesは1件以上必要です");
        
        // シェア合計100%チェック
        double totalShare = request.getSharePies().stream()
                .map(sp -> sp.getShare() != null ? sp.getShare().doubleValue() : 0.0)
                .reduce(0.0, Double::sum);
        if (Math.abs(totalShare - 1.0) > 0.0001)
            throw new BusinessRuleViolationException("sharePiesの合計は100%（1.0）でなければなりません");
        
        // 投資家の存在チェック
        for (CreateFacilityRequest.SharePieRequest sharePie : request.getSharePies()) {
            if (sharePie.getInvestorId() == null) {
                throw new BusinessRuleViolationException("投資家IDは必須です");
            }
            if (!investorRepository.existsById(sharePie.getInvestorId())) {
                throw new BusinessRuleViolationException("投資家ID " + sharePie.getInvestorId() + " が存在しません");
            }
        }
        
        // 借り手の信用限度額チェック
        Syndicate syndicate = syndicateRepository.findById(request.getSyndicateId())
                .orElseThrow(() -> new BusinessRuleViolationException("シンジケートID " + request.getSyndicateId() + " が存在しません"));
        
        Borrower borrower = borrowerRepository.findById(syndicate.getBorrowerId())
                .orElseThrow(() -> new BusinessRuleViolationException("借り手ID " + syndicate.getBorrowerId() + " が存在しません"));
        
        if (request.getCommitment().isGreaterThan(borrower.getCreditLimit())) {
            throw new BusinessRuleViolationException("コミットメント額 " + request.getCommitment().getAmount() + 
                    " が借り手の信用限度額 " + borrower.getCreditLimit().getAmount() + " を超えています");
        }
    }

    public void validateUpdateFacilityRequest(UpdateFacilityRequest request, Long excludeFacilityId) {
        if (request == null)
            throw new BusinessRuleViolationException("リクエストがnullです");
        if (request.getSyndicateId() == null)
            throw new BusinessRuleViolationException("syndicateIdは必須です");
        if (request.getCommitment() == null || request.getCommitment().isZero())
            throw new BusinessRuleViolationException("commitmentは必須かつ0より大きい必要があります");
        if (request.getCurrency() == null || request.getCurrency().isBlank())
            throw new BusinessRuleViolationException("currencyは必須です");
        if (request.getStartDate() == null)
            throw new BusinessRuleViolationException("startDateは必須です");
        if (request.getEndDate() == null)
            throw new BusinessRuleViolationException("endDateは必須です");
        if (request.getEndDate().isBefore(request.getStartDate()))
            throw new BusinessRuleViolationException("endDateはstartDate以降でなければなりません");
        if (request.getSharePies() == null || request.getSharePies().isEmpty())
            throw new BusinessRuleViolationException("sharePiesは1件以上必要です");
        
        // シェア合計100%チェック
        double totalShare = request.getSharePies().stream()
                .map(sp -> sp.getShare() != null ? sp.getShare().doubleValue() : 0.0)
                .reduce(0.0, Double::sum);
        if (Math.abs(totalShare - 1.0) > 0.0001)
            throw new BusinessRuleViolationException("sharePiesの合計は100%（1.0）でなければなりません");
        
        // 投資家の存在チェック
        for (UpdateFacilityRequest.SharePieRequest sharePie : request.getSharePies()) {
            if (sharePie.getInvestorId() == null) {
                throw new BusinessRuleViolationException("投資家IDは必須です");
            }
            if (!investorRepository.existsById(sharePie.getInvestorId())) {
                throw new BusinessRuleViolationException("投資家ID " + sharePie.getInvestorId() + " が存在しません");
            }
        }
        
        // 借り手の信用限度額チェック
        Syndicate syndicate = syndicateRepository.findById(request.getSyndicateId())
                .orElseThrow(() -> new BusinessRuleViolationException("シンジケートID " + request.getSyndicateId() + " が存在しません"));
        
        Borrower borrower = borrowerRepository.findById(syndicate.getBorrowerId())
                .orElseThrow(() -> new BusinessRuleViolationException("借り手ID " + syndicate.getBorrowerId() + " が存在しません"));
        
        if (request.getCommitment().isGreaterThan(borrower.getCreditLimit())) {
            throw new BusinessRuleViolationException("コミットメント額 " + request.getCommitment().getAmount() + 
                    " が借り手の信用限度額 " + borrower.getCreditLimit().getAmount() + " を超えています");
        }
        
        if (request.getVersion() == null)
            throw new BusinessRuleViolationException("versionは必須です");
    }
}
