package com.library.controller;

import com.library.dto.BorrowRequest;
import com.library.model.BorrowRecord;
import com.library.service.BorrowService;
import com.library.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BorrowControllerFixed {
    private final BorrowService borrowService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRecord> borrowBook(
            Authentication authentication,
            @Valid @RequestBody BorrowRequest request) {
        Long userId = getUserIdFromAuth(authentication);
        BorrowRecord record = borrowService.borrowBook(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }
    
    @PostMapping("/return/{recordId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long recordId) {
        BorrowRecord record = borrowService.returnBook(recordId);
        return ResponseEntity.ok(record);
    }
    
    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BorrowRecord>> getBorrowHistory(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(borrowService.getUserBorrowHistory(userId));
    }
    
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BorrowRecord>> getActiveBorrows(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(borrowService.getActiveBorrows(userId));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowRecord>> getOverdueRecords() {
        return ResponseEntity.ok(borrowService.getOverdueRecords());
    }
    
    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new RuntimeException("Unable to extract user ID from authentication");
    }
}
