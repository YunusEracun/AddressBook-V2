package com.yunusemre.addressbook_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

// Bu anotasyon, Spring'e bu sınıfın TÜM @RestController'ları dinlediğini söyler.
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Service katmanında fırlatılan 'IllegalArgumentException'ları yakalar.
     * (Örn: Mükerrer e-posta, geçersiz telefon)
     * @param e Yakalanan hata
     * @return 400 Bad Request durum kodu ve temiz bir JSON hata mesajı
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Yanıtın durum kodunu 400 yap
    public Map<String, String> handleIllegalArgument(IllegalArgumentException e) {
        // Hata mesajını temiz bir JSON formatında döndür
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("status", "400");
        errorMap.put("error", "Bad Request");
        errorMap.put("message", e.getMessage()); // Service'ten gelen asıl hata mesajı
        return errorMap;
    }

    /**
     * Service katmanında fırlatılan 'NoSuchElementException'ları yakalar.
     * (Örn: ID veya E-posta ile aranan kayıt bulunamadığında)
     * @param e Yakalanan hata
     * @return 404 Not Found durum kodu ve temiz bir JSON hata mesajı
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Yanıtın durum kodunu 404 yap
    public Map<String, String> handleNotFound(NoSuchElementException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("status", "404");
        errorMap.put("error", "Not Found");
        errorMap.put("message", e.getMessage());
        return errorMap;
    }
}