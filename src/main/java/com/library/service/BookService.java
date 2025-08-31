package com.library.service;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.model.Author;
import com.library.repository.BookRepository;
import com.library.repository.AuthorRepository;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    
    public BookDTO createBook(BookDTO bookDTO) {
        if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
            throw new BadRequestException("Book with this ISBN already exists");
        }
        
        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setGenre(bookDTO.getGenre());
        book.setSummary(bookDTO.getSummary());
        book.setCoverImage(bookDTO.getCoverImage());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getTotalCopies());
        
        if (bookDTO.getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            book.setAuthor(author);
        }
        
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }
    
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return convertToDTO(book);
    }
    
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::convertToDTO);
    }
    
    public List<BookDTO> searchBooks(String searchTerm) {
        return bookRepository.searchBooks(searchTerm).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findAvailableBooks().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        
        book.setTitle(bookDTO.getTitle());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setGenre(bookDTO.getGenre());
        book.setSummary(bookDTO.getSummary());
        book.setCoverImage(bookDTO.getCoverImage());
        book.setTotalCopies(bookDTO.getTotalCopies());
        
        if (bookDTO.getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            book.setAuthor(author);
        }
        
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }
    
    public void updateBookStock(Long bookId, int change) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        
        int newAvailable = book.getAvailableCopies() + change;
        if (newAvailable < 0) {
            throw new BadRequestException("Insufficient copies available");
        }
        
        book.setAvailableCopies(newAvailable);
        bookRepository.save(book);
    }
    
    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setGenre(book.getGenre());
        dto.setSummary(book.getSummary());
        dto.setCoverImage(book.getCoverImage());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        
        if (book.getAuthor() != null) {
            dto.setAuthorId(book.getAuthor().getId());
            dto.setAuthorName(book.getAuthor().getFullName());
        }
        
        return dto;
    }
}
