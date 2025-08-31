package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUser_Id(Long userId);
    List<BorrowRecord> findByBook_Id(Long bookId);
    List<BorrowRecord> findByStatus(BorrowStatus status);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.user.id = :userId AND br.status = :status")
    List<BorrowRecord> findByUserIdAndStatus(Long userId, BorrowStatus status);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :date AND br.status = 'BORROWED'")
    List<BorrowRecord> findOverdueRecords(LocalDate date);
    
    Optional<BorrowRecord> findByUser_IdAndBook_IdAndStatus(Long userId, Long bookId, BorrowStatus status);
    
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.status = 'BORROWED'")
    Long countActiveBorrows();
    
    @Query("SELECT br.book.id, COUNT(br) FROM BorrowRecord br GROUP BY br.book.id ORDER BY COUNT(br) DESC")
    List<Object[]> findMostBorrowedBooks();
}
