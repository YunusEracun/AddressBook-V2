package com.yunusemre.addressbook_api.service;
import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;
// Lombok importları
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity // Bu bir veritabanı tablosudur
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entry_type")

public abstract class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @SerializedName("firstName")

    private String name;
    private String email;
    private String phoneNumber;

    public Entry(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(email, entry.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email + ", Phone Number: " + phoneNumber;
    }

    public abstract String getDetails();

}
