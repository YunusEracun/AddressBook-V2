package com.yunusemre.addressbook_api.repository;

import com.yunusemre.addressbook_api.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface AddressBookRepository extends JpaRepository<Entry, Long>, JpaSpecificationExecutor<Entry> {

    Optional<Entry> findByEmail(String email);
    Optional<Entry> findByPhoneNumber(String phoneNumber);
}