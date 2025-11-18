package com.yunusemre.addressbook_api.Controller;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/entries")
@Tag(name = "Adres Defteri Yönetimi", description = "Kişi ekleme, silme, güncelleme ve listeleme işlemleri")
public class AddressBookController {

    private final AddressBookService service;

    @Autowired
    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Tüm kayıtları listele", description = "Sistemdeki kayıtlı tüm kişileri getirir.")
    public List<Entry> getAllEntries() {
        return service.listAllEntries();
    }

    @GetMapping("/{email}")
    @Operation(summary = "Email ile kişi bul", description = "Verilen email adresine sahip kişiyi getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kişi bulundu"),
            @ApiResponse(responseCode = "404", description = "Bu email ile kayıtlı kimse bulunamadı")
    })
    public ResponseEntity<Entry> getEntryByEmail(
            @Parameter(description = "Aranacak kişinin email adresi", example = "ali@example.com")
            @PathVariable String email) {

        Optional<Entry> entry = service.findEntryByEmail(email);
        return entry.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Yeni kişi ekle", description = "Adres defterine yeni bir kayıt oluşturur.")
    @ApiResponse(responseCode = "201", description = "Kayıt başarıyla oluşturuldu")
    public ResponseEntity<Entry> addEntry(@RequestBody Entry entry) {
        Entry savedEntry = service.addEntry(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Kişiyi sil", description = "Email adresi verilen kişiyi sistemden siler.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Silme işlemi başarılı (İçerik yok)"),
            @ApiResponse(responseCode = "404", description = "Silinecek kişi bulunamadı")
    })
    public ResponseEntity<Void> deleteEntry(@PathVariable String email) {
        service.deleteEntry(email); // Buraya service içinde hata fırlatma mekanizması eklenebilir
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kişiyi güncelle", description = "ID'si verilen kişinin bilgilerini günceller.")
    @ApiResponse(responseCode = "200", description = "Güncelleme başarılı")
    public ResponseEntity<Entry> updateEntry(
            @Parameter(description = "Güncellenecek kişinin ID'si") @PathVariable Long id,
            @RequestBody Entry entryDetails) {
        Entry updatedEntry = service.updateEntry(id, entryDetails);
        return ResponseEntity.ok(updatedEntry);
    }
}