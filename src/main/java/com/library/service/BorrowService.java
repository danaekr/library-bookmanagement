package com.library.service;

import com.library.dto.BorrowRequest;
import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.model.User;
import com.library.model.enums.BorrowStatus;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookService bookService;
    
    public BorrowRecord borrowBook(Long userId, BorrowRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        
        // check if the book is available
        if (!book.isAvailable()) {
            throw new BadRequestException("Book is not available for borrowing");
        }
        
        // check if user already has this book borrowed
        borrowRecordRepository.findByUser_IdAndBook_IdAndStatus(userId, request.getBookId(), BorrowStatus.BORROWED)
            .ifPresent(record -> {
                throw new BadRequestException("User already has this book borrowed");
            });
        
        // Create borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(request.getDays()));
        borrowRecord.setStatus(BorrowStatus.BORROWED);
        
        // update book availability
        bookService.updateBookStock(book.getId(), -1);
        
        return borrowRecordRepository.save(borrowRecord);
    }
    
    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
            .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));
        
        if (record.getStatus() != BorrowStatus.BORROWED) {
            throw new BadRequestException("Book is not currently borrowed");
        }
        
        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);
        record.setFineAmount(record.calculateFine());
        
        // update book availability
        bookService.updateBookStock(record.getBook().getId(), 1);
        
        return borrowRecordRepository.save(record);
    }
    
    public List<BorrowRecord> getUserBorrowHistory(Long userId) {
        return borrowRecordRepository.findByUser_Id(userId);
    }
    
    public List<BorrowRecord> getActiveBorrows(Long userId) {
        return borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.BORROWED);
    }
    
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordRepository.findOverdueRecords(LocalDate.now(), BorrowStatus.BORROWED);
    }
    
    public void updateOverdueStatus() {
        List<BorrowRecord> overdueRecords = getOverdueRecords();
        overdueRecords.forEach(record -> {
            record.setStatus(BorrowStatus.OVERDUE);
            borrowRecordRepository.save(record);
        });
    }
}
