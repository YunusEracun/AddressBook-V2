package com.yunusemre.addressbook_api.model;
import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;
// Lombok importları
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entry_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Person.class, name = "person"),
        @JsonSubTypes.Type(value = Company.class, name = "company")
})
public abstract class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @SerializedName("firstName")

    private String name;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
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
