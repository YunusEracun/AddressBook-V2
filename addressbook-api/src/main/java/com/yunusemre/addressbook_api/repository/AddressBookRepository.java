package com.yunusemre.addressbook_api.repository;

import com.yunusemre.addressbook_api.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Entry varlığını ve Primary Key tipini (Long) belirtir
public interface AddressBookRepository extends JpaRepository<Entry, Long> {

    // E-posta üzerinden O(1) hızlı arama için Spring Data JPA metodu:
    Optional<Entry> findByEmail(String email);

    // Spring, bu metotları otomatik olarak uygulayacaktır.
}