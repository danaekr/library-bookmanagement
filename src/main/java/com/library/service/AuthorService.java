package com.library.service;

import com.library.dto.AuthorDTO;
import com.library.model.Author;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setBiography(authorDTO.getBiography());
        author.setBirthDate(authorDTO.getBirthDate());
        
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }
    
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        return convertToDTO(author);
    }
    
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<AuthorDTO> searchAuthors(String searchTerm) {
        return authorRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setBiography(authorDTO.getBiography());
        author.setBirthDate(authorDTO.getBirthDate());
        
        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }
    
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        
        // Check if author has books
        if (!bookRepository.findByAuthor_Id(id).isEmpty()) {
            throw new BadRequestException("Cannot delete author with associated books");
        }
        
        authorRepository.delete(author);
    }
    
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBiography(author.getBiography());
        dto.setBirthDate(author.getBirthDate());
        dto.setBookCount(bookRepository.findByAuthor_Id(author.getId()).size());
        return dto;
    }
}
