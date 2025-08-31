package com.library.service;

import com.library.repository.*;
import com.library.model.Book;
import com.library.model.enums.BorrowStatus;
import com.library.model.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalBooks", bookRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("activeBorrows", borrowRecordRepository.countActiveBorrows(BorrowStatus.BORROWED));
        stats.put("availableBooks", bookRepository.findAvailableBooks().size());
        stats.put("activeMembers", userRepository.findActiveMembers(RoleType.MEMBER).size());
        
        return stats;
    }
    
    public List<Map<String, Object>> getMostBorrowedBooks(int limit) {
        List<Object[]> results = borrowRecordRepository.findMostBorrowedBooks();
        List<Map<String, Object>> books = new ArrayList<>();
        
        for (int i = 0; i < Math.min(limit, results.size()); i++) {
            Object[] result = results.get(i);
            Map<String, Object> bookInfo = new HashMap<>();
            
            Long bookId = (Long) result[0];
            Long borrowCount = (Long) result[1];
            
            bookRepository.findById(bookId).ifPresent(book -> {
                bookInfo.put("id", book.getId());
                bookInfo.put("title", book.getTitle());
                bookInfo.put("author", book.getAuthor() != null ? book.getAuthor().getFullName() : "Unknown");
                bookInfo.put("borrowCount", borrowCount);
            });
            
            if (!bookInfo.isEmpty()) {
                books.add(bookInfo);
            }
        }
        
        return books;
    }
    
    public Map<String, Object> getInventoryStatus() {
        Map<String, Object> inventory = new HashMap<>();
        
        long totalBooks = bookRepository.count();
        List<Book> availableBooks = bookRepository.findAvailableBooks();
        
        inventory.put("totalBooks", totalBooks);
        inventory.put("availableBooks", availableBooks.size());
        inventory.put("borrowedBooks", borrowRecordRepository.findByStatus(BorrowStatus.BORROWED).size());
        inventory.put("overdueBooks", borrowRecordRepository.findByStatus(BorrowStatus.OVERDUE).size());
        
        return inventory;
    }
}
        inventory.put("overdueBooks", borrowRecordRepository.findByStatus(BorrowStatus.OVERDUE).size());
        
        return inventory;
    }
}
