package com.yunusemre.addressbook_api.Controller;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

// 1. Bu sınıfın bir REST API Controller'ı olduğunu belirtir
@RestController
// 2. Tüm metotlar için temel URL yolunu (/api/v1/entries) tanımlar
@RequestMapping("/api/v1/entries")
public class AddressBookController {

    private final AddressBookService service;

    // Bağımlılık Enjeksiyonu: Spring, Service nesnesini buraya otomatik verir
    @Autowired
    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    // --- 1. TÜM KAYITLARI LİSTELEME (Eski Menü 1) ---
    // HTTP Yöntemi: GET
    // Tam URL: GET http://localhost:8080/api/v1/entries
    @GetMapping
    public List<Entry> getAllEntries() {
        // Doğrudan Service katmanındaki findAll metodunu çağırır
        return service.listAllEntries();
    }

    // --- 2. YENİ KAYIT EKLEME (Eski Menü 2) ---
    // HTTP Yöntemi: POST
    // Tam URL: POST http://localhost:8080/api/v1/entries
    // İstenen veri (Entry objesi), HTTP isteğinin gövdesinden (@RequestBody) alınır.
    @PostMapping
    public ResponseEntity<Entry> addEntry(@RequestBody Entry entry) {
        try {
            Entry savedEntry = service.addEntry(entry);
            // Başarılı: 201 Created durum kodu ve kaydedilen objeyi döndür
            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Validasyon hatası (e.g., geçersiz telefon/email formatı)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        // NOT: Email benzersizlik hatası (Duplicate Key) 500 hatası olarak dönebilir.
        // Bunu düzeltmek için daha sonra hata yakalama (Exception Handling) ekleyeceğiz.
    }

    // --- 3. KAYIT SİLME (Eski Menü 3) ---
    // HTTP Yöntemi: DELETE
    // Tam URL: DELETE http://localhost:8080/api/v1/entries/test@example.com
    // Silinecek kaydın e-postası URL yolundan alınır (@PathVariable)
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String email) {
        try {
            service.deleteEntry(email);
            // Başarılı: 204 No Content (içerik yok) durum kodu döndür
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            // Eğer silinecek kayıt bulunamazsa 404 Not Found döndür
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}