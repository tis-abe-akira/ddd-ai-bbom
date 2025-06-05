package com.example.syndicatelending.dto;

import com.example.syndicatelending.entity.Industry;
import com.example.syndicatelending.entity.Country;
import jakarta.validation.constraints.NotBlank;

public class CreateCompanyRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;
    private String registrationNumber;
    private Industry industry;
    private String address;
    private Country country;

    public CreateCompanyRequest() {
    }

    public CreateCompanyRequest(String companyName, String registrationNumber, Industry industry, String address,
            Country country) {
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.industry = industry;
        this.address = address;
        this.country = country;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Industry getIndustry() {
        return industry;
    }

    public String getAddress() {
        return address;
    }

    public Country getCountry() {
        return country;
    }
}
