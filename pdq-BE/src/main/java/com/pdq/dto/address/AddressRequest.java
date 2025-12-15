package com.pdq.dto.address;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequest {
    
    @NotBlank(message = "Recipient name is required")
    @JsonAlias({
        "fullName", "name", "recipient", "recipient_name", "recipientName",
        "full_name", "FullName", "RecipientName"
    })
    private String recipientName;

    @NotBlank(message = "Phone is required")
    @JsonAlias({
        "phoneNumber", "phone_number", "recipientPhone", "mobile", "phoneNo", "phone_no"
    })
    private String phone;

    @NotBlank(message = "Address is required")
    @JsonAlias({
        "address", "address_line", "street", "addressLine", "addressDetail",
        "address_detail", "detailAddress", "detail_address"
    })
    private String addressLine;

    @JsonAlias({"wardName", "ward_name"})
    private String ward;
    @JsonAlias({"districtName", "district_name"})
    private String district;
    @JsonAlias({"cityName", "city_name", "province"})
    private String city;
    @JsonAlias({"default", "is_default", "defaultAddress", "default_address"})
    private Boolean isDefault;
    private String note;

    public AddressRequest() {}

    // Getters and Setters
    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
