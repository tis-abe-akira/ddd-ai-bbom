// FacilityValidatorのpackageを変更し、importを全てfacility配下から直下のステレオタイプパッケージに修正
package com.example.syndicatelending.domain;

import com.example.syndicatelending.dto.CreateFacilityRequest;
import com.example.syndicatelending.dto.UpdateFacilityRequest;
import org.springframework.stereotype.Component;

@Component
public class FacilityValidator {
    public void validateCreateFacilityRequest(CreateFacilityRequest request) {
        if (request == null)
            throw new IllegalArgumentException("リクエストがnullです");
        if (request.getSyndicateId() == null)
            throw new IllegalArgumentException("syndicateIdは必須です");
        if (request.getCommitment() == null || request.getCommitment().isZero())
            throw new IllegalArgumentException("commitmentは必須かつ0より大きい必要があります");
        if (request.getCurrency() == null || request.getCurrency().isBlank())
            throw new IllegalArgumentException("currencyは必須です");
        if (request.getStartDate() == null)
            throw new IllegalArgumentException("startDateは必須です");
        if (request.getEndDate() == null)
            throw new IllegalArgumentException("endDateは必須です");
        if (request.getEndDate().isBefore(request.getStartDate()))
            throw new IllegalArgumentException("endDateはstartDate以降でなければなりません");
        if (request.getSharePies() == null || request.getSharePies().isEmpty())
            throw new IllegalArgumentException("sharePiesは1件以上必要です");
        // シェア合計100%チェック
        double totalShare = request.getSharePies().stream()
                .map(sp -> sp.getShare() != null ? sp.getShare().doubleValue() : 0.0)
                .reduce(0.0, Double::sum);
        if (Math.abs(totalShare - 1.0) > 0.0001)
            throw new IllegalArgumentException("sharePiesの合計は100%（1.0）でなければなりません");
    }

    public void validateUpdateFacilityRequest(UpdateFacilityRequest request, Long excludeFacilityId) {
        if (request == null)
            throw new IllegalArgumentException("リクエストがnullです");
        if (request.getSyndicateId() == null)
            throw new IllegalArgumentException("syndicateIdは必須です");
        if (request.getCommitment() == null || request.getCommitment().isZero())
            throw new IllegalArgumentException("commitmentは必須かつ0より大きい必要があります");
        if (request.getCurrency() == null || request.getCurrency().isBlank())
            throw new IllegalArgumentException("currencyは必須です");
        if (request.getStartDate() == null)
            throw new IllegalArgumentException("startDateは必須です");
        if (request.getEndDate() == null)
            throw new IllegalArgumentException("endDateは必須です");
        if (request.getEndDate().isBefore(request.getStartDate()))
            throw new IllegalArgumentException("endDateはstartDate以降でなければなりません");
        if (request.getSharePies() == null || request.getSharePies().isEmpty())
            throw new IllegalArgumentException("sharePiesは1件以上必要です");
        // シェア合計100%チェック
        double totalShare = request.getSharePies().stream()
                .map(sp -> sp.getShare() != null ? sp.getShare().doubleValue() : 0.0)
                .reduce(0.0, Double::sum);
        if (Math.abs(totalShare - 1.0) > 0.0001)
            throw new IllegalArgumentException("sharePiesの合計は100%（1.0）でなければなりません");
        if (request.getVersion() == null)
            throw new IllegalArgumentException("versionは必須です");
    }
}
