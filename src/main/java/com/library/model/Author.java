package com.library.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(length = 1000)
    private String biography;
    
    private LocalDate birthDate;
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Book> books;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
