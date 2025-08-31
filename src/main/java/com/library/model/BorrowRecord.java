package com.library.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.library.model.enums.BorrowStatus;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "borrow_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Column(nullable = false)
    private LocalDate borrowDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    private LocalDate returnDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;
    
    private BigDecimal fineAmount;
    
    @PrePersist
    protected void onCreate() {
        borrowDate = LocalDate.now();
        dueDate = borrowDate.plusDays(14); // 2 weeks borrowing period
        status = BorrowStatus.BORROWED;
    }
    
    public BigDecimal calculateFine() {
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysLate = returnDate.toEpochDay() - dueDate.toEpochDay();
            return BigDecimal.valueOf(daysLate * 0.50); // 0.50 euros per day
        }
        return BigDecimal.ZERO;
    }
}
