package com.yunusemre.addressbook_api.Controller;

import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/entries")
public class AddressBookController {

    private final AddressBookService service;

    @Autowired
    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Entry> getAllEntries() {
        return service.listAllEntries();
    }
    @GetMapping("/{email}")
    public ResponseEntity<Entry> getEntryByEmail(@PathVariable String email) {

        Optional<Entry> entry = service.findEntryByEmail(email);
        return entry.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<Entry> addEntry(@RequestBody Entry entry) {
        try {
            Entry savedEntry = service.addEntry(entry);
            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String email) {
        try {
            service.deleteEntry(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}