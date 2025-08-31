package com.library.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String isbn;
    
    @Column(nullable = false)
    private String title;
    
    private LocalDate publicationDate;
    private String genre;
    
    @Column(length = 1000)
    private String summary;
    
    private String coverImage;
    private Integer totalCopies;
    private Integer availableCopies;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;
    
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<BorrowRecord> borrowRecords;
    
    public boolean isAvailable() {
        return availableCopies != null && availableCopies > 0;
    }
}
