package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthor_Id(Long authorId);
    List<Book> findByGenre(String genre);
    Optional<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Book> searchBooks(@Param("search") String search);
    
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();
}
