package com.yunusemre.addressbook_api.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
// Lombok importları
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
// JPA/Hibernate importları
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PERSON")
@Getter @Setter @NoArgsConstructor

public class Person extends Entry {
    @SerializedName("lastName")
    private String lastName;

    public Person(
            @JsonProperty("firstName") String name,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("email") String email) {

        super(name, email, phoneNumber); // Sıralamayı kontrol edin (Entry'ye göre)
        this.lastName = lastName;
    }

    public String getLastName() {return lastName;}

    @Override
    public String getDetails() {
        return "Last Name: " + lastName;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                ", Last Name: " + getLastName() +
                ", Email: " + getEmail() +
                ", Phone: " + getPhoneNumber();
    }




}
