package com.yunusemre.addressbook_api.service;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.model.Person;
import com.yunusemre.addressbook_api.model.Company;
import com.yunusemre.addressbook_api.repository.AddressBookRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // İşlemleri yönetmek için
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AddressBookService {

    private final AddressBookRepository repository;
    @Autowired
    public AddressBookService(AddressBookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Entry addEntry(Entry newEntry) {
        return repository.save(newEntry);
    }

    public List<Entry> listAllEntries() {
        return repository.findAll();
    }

    @Transactional
    public void deleteEntry(String email) {
        Entry entry = repository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Entry with email " + email + " not found."));

        // Veritabanından sil
        repository.delete(entry);
    }


    public Optional<Entry> findEntryByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Entry> searchEntries(String entryType, String searchField, String searchValue) {

        // 1. (YENİ YOL) Dinamik sorguyu oluştur
        Specification<Entry> spec = EntrySpecification.findByCriteria(entryType, searchField, searchValue);
        return repository.findAll(spec);

    }

    public Collection<Entry> findDuplicateNames() {
        return new ArrayList<>();
    }

}