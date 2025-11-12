package com.yunusemre.addressbook_api.model;
// Lombok importları
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("COMPANY")
@Getter @Setter @NoArgsConstructor

public class Company extends Entry {
    private String taxNumber;
    private String address;

    public Company(
            @JsonProperty("firstName") String name,
            @JsonProperty("email") String email,
            @JsonProperty("taxNumber") String taxNumber,
            @JsonProperty("address") String address,
            @JsonProperty("phoneNumber") String phoneNumber) {

        super(name, email, phoneNumber);
        this.taxNumber = taxNumber;
        this.address = address;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    @JsonIgnore
    public String getDetails() {
        return "Tax Number: " + taxNumber + ", Address: " + address;
    }

    @Override
    public String toString() {
        return "Company Name: " + getName() +
                ", Tax Number: " + taxNumber +
                ", Address: " + address +
                ", Email: " + getEmail() +
                ", Phone: " + getPhoneNumber();
    }


}
