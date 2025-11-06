package com.yunusemre.addressbook_api.service;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.model.Person;
import com.yunusemre.addressbook_api.model.Company;
import com.yunusemre.addressbook_api.repository.AddressBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // İşlemleri yönetmek için
import java.util.*;
import java.util.stream.Collectors;

// Bu sınıfın Spring tarafından yönetilen bir servis olduğunu belirtir
@Service
public class AddressBookService {

    // Spring, bu repository arayüzünün somut bir implementasyonunu otomatik olarak sağlar.
    private final AddressBookRepository repository;

    // Eski Set<String> kullanilanTelefonlar yapısı artık veritabanına taşınabilir,
    // ancak şimdilik telefon numarasını elle kontrol edelim (Veritabanı benzersizlik kuralına ek olarak).

    // Constructor tabanlı Bağımlılık Enjeksiyonu (Spring'in önerdiği en temiz yol)
    @Autowired
    public AddressBookService(AddressBookRepository repository) {
        this.repository = repository;
    }

    // --- TEMEL CRUD İŞLEMLERİ ---

    // Eski 'addEntry' metodunuzun yeni hali
    @Transactional
    public Entry addEntry(Entry newEntry) {

        // 1. Manuel Telefon Benzersizlik Kontrolü (Veritabanı kontrolüne ek)
        // Eğer veritabanında telefon numarası benzersiz (unique) olarak ayarlanmadıysa bu kontrol önemlidir.
        // Spring Data JPA'ya özel bir sorgu yazarak kontrol edilebilir:
        // if (repository.existsByPhoneNumber(newEntry.getPhoneNumber())) { ... }

        // Şimdilik sadece email benzersizliğini veritabanına bırakıyoruz.
        // Email benzersizliği (unique=true), veritabanı seviyesinde kontrol edilecektir.

        // 2. Alan Validasyonu: Model Constructor'da yapılır (throw new IllegalArgumentException)

        // 3. Veritabanına kaydetme (Eski Map.put() yerine)
        return repository.save(newEntry);
    }

    // Eski 'listAllEntries' metodunuzun yeni hali
    public List<Entry> listAllEntries() {
        // Repository'den tüm kayıtları çek (Eski Map.values() yerine)
        return repository.findAll();
    }

    // Eski 'deleteEntry' metodunuzun yeni hali
    @Transactional
    public void deleteEntry(String email) {
        // E-posta ile kaydı bul (O(1) arama mantığı artık SQL sorgusuna dönüşüyor)
        Entry entry = repository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Entry with email " + email + " not found."));

        // Veritabanından sil
        repository.delete(entry);
    }

    // --- ÖZEL MANTIK VE ARAMA İŞLEMLERİ ---

    // Eski 'findEntryByEmail'
    public Optional<Entry> findEntryByEmail(String email) {
        // Repository'de tanımladığımız metot kullanılır
        return repository.findByEmail(email);
    }

    // Eski 'searchEntries' (Polimorfik Arama)
    public List<Entry> searchEntries(String entryType, String searchField, String searchValue) {
        // NOT: Spring Data JPA'da bu kadar karmaşık bir aramayı Repository'ye taşımak için
        // Query metotları veya @Query anotasyonları gerekir. Şimdilik Stream ile yapalım:

        return repository.findAll().stream() // Tüm veriyi çek (Verimli DEĞİL, ama basit)
                .filter(entry -> {
                    // ... (Önceki searchEntries mantığı: switch-case ve instanceof) ...
                    // Bu mantık, repository.findAll()'dan dönen List<Entry> üzerinde çalışır.
                    return false; // Geçici olarak false döndürülüyor.
                })
                .collect(Collectors.toList());
    }

    // Eski 'findDuplicateNames'
    public Collection<Entry> findDuplicateNames() {
        // Mükerrer bulma mantığı da burada Stream API ile (eski mantıkla) devam eder.
        return new ArrayList<>(); // Geçici olarak boş döndürülüyor.
    }

    // --- KALICILIK İŞLEMLERİ (Artık JPA'nın İşi) ---
    // loadData ve saveData metotlarına artık ihtiyacımız yok, JPA her şeyi otomatik yönetir.
}