package com.yunusemre.addressbook_api.service;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.model.Person;
import com.yunusemre.addressbook_api.model.Company;
import com.yunusemre.addressbook_api.repository.AddressBookRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    /**
     * Mevcut bir kaydı ID'sine göre bulur ve günceller.
     * @param id Güncellenecek kaydın veritabanı ID'si (URL'den gelir)
     * @param entryDetails Postman'den gelen yeni verileri içeren nesne (JSON Body'den gelir)
     * @return Veritabanına kaydedilen güncellenmiş nesne
     * @throws NoSuchElementException Kayıt bulunamazsa fırlatılır
     * @throws IllegalArgumentException E-posta zaten kullanımdaysa fırlatılır
     */
    @Transactional
    public Entry updateEntry(Long id, Entry entryDetails) {

        Entry existingEntry = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entry not found with id: " + id));

        if (!existingEntry.getEmail().equals(entryDetails.getEmail())) {
            if (repository.findByEmail(entryDetails.getEmail()).isPresent()) {
                throw new IllegalArgumentException("This email address is already in use by another entry.");
            }
        }
        if (!existingEntry.getPhoneNumber().equals(entryDetails.getPhoneNumber())) {
            if (repository.findByPhoneNumber(entryDetails.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("This phone number is already in use by another entry.");
            }
        }
        entryDetails.setId(id);
        return repository.save(entryDetails);
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