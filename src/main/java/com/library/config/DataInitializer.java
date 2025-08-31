package com.library.config;

import com.library.model.Role;
import com.library.model.User;
import com.library.model.Author;
import com.library.model.Book;
import com.library.model.enums.RoleType;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            initializeRoles();
        }
        
        // initialize default users if they don't exist
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        
        // initialize sample authors and books if they don't exist
        if (authorRepository.count() == 0) {
            initializeAuthorsAndBooks();
        }
    }
    
    private void initializeRoles() {
        Role adminRole = new Role();
        adminRole.setName(RoleType.ADMIN);
        adminRole.setDescription("System administrator with full access");
        roleRepository.save(adminRole);
        
        Role librarianRole = new Role();
        librarianRole.setName(RoleType.LIBRARIAN);
        librarianRole.setDescription("Library staff with book and user management access");
        roleRepository.save(librarianRole);
        
        Role memberRole = new Role();
        memberRole.setName(RoleType.MEMBER);
        memberRole.setDescription("Library member with borrowing privileges");
        roleRepository.save(memberRole);
    }
    
    private void initializeUsers() {
        // create user admin
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@library.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(roleRepository.findByName(RoleType.ADMIN).orElseThrow());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setActive(true);
        userRepository.save(admin);
        
        // create user librarian
        User librarian = new User();
        librarian.setUsername("librarian");
        librarian.setPassword(passwordEncoder.encode("librarian123"));
        librarian.setEmail("librarian@library.com");
        librarian.setFirstName("John");
        librarian.setLastName("Doe");
        librarian.setRole(roleRepository.findByName(RoleType.LIBRARIAN).orElseThrow());
        librarian.setCreatedAt(LocalDateTime.now());
        librarian.setActive(true);
        userRepository.save(librarian);
        
        // create user member
        User member = new User();
        member.setUsername("member");
        member.setPassword(passwordEncoder.encode("member123"));
        member.setEmail("member@library.com");
        member.setFirstName("Jane");
        member.setLastName("Smith");
        member.setRole(roleRepository.findByName(RoleType.MEMBER).orElseThrow());
        member.setCreatedAt(LocalDateTime.now());
        member.setActive(true);
        userRepository.save(member);
    }
    
    private void initializeAuthorsAndBooks() {
        // create authors
        Author orwell = new Author();
        orwell.setFirstName("George");
        orwell.setLastName("Orwell");
        orwell.setBiography("English novelist and essayist");
        orwell.setBirthDate(LocalDate.of(1903, 6, 25));
        authorRepository.save(orwell);
        
        Author austen = new Author();
        austen.setFirstName("Jane");
        austen.setLastName("Austen");
        austen.setBiography("English novelist known for social commentary");
        austen.setBirthDate(LocalDate.of(1775, 12, 16));
        authorRepository.save(austen);
        
        Author twain = new Author();
        twain.setFirstName("Mark");
        twain.setLastName("Twain");
        twain.setBiography("American writer and humorist");
        twain.setBirthDate(LocalDate.of(1835, 11, 30));
        authorRepository.save(twain);
        
        // create books
        Book book1984 = new Book();
        book1984.setIsbn("978-0-452-28423-4");
        book1984.setTitle("1984");
        book1984.setPublicationDate(LocalDate.of(1949, 6, 8));
        book1984.setGenre("Dystopian");
        book1984.setSummary("A dystopian social science fiction novel");
        book1984.setTotalCopies(5);
        book1984.setAvailableCopies(3);
        book1984.setAuthor(orwell);
        bookRepository.save(book1984);
        
        Book animalFarm = new Book();
        animalFarm.setIsbn("978-0-14-143951-8");
        animalFarm.setTitle("Animal Farm");
        animalFarm.setPublicationDate(LocalDate.of(1945, 8, 17));
        animalFarm.setGenre("Political Satire");
        animalFarm.setSummary("An allegorical novella");
        animalFarm.setTotalCopies(4);
        animalFarm.setAvailableCopies(2);
        animalFarm.setAuthor(orwell);
        bookRepository.save(animalFarm);
        
        Book pride = new Book();
        pride.setIsbn("978-0-14-143972-3");
        pride.setTitle("Pride and Prejudice");
        pride.setPublicationDate(LocalDate.of(1813, 1, 28));
        pride.setGenre("Romance");
        pride.setSummary("A romantic novel of manners");
        pride.setTotalCopies(3);
        pride.setAvailableCopies(2);
        pride.setAuthor(austen);
        bookRepository.save(pride);
        
        Book tomSawyer = new Book();
        tomSawyer.setIsbn("978-0-486-28061-5");
        tomSawyer.setTitle("The Adventures of Tom Sawyer");
        tomSawyer.setPublicationDate(LocalDate.of(1876, 6, 1));
        tomSawyer.setGenre("Adventure");
        tomSawyer.setSummary("Story of a young boy growing up along the Mississippi River");
        tomSawyer.setTotalCopies(4);
        tomSawyer.setAvailableCopies(4);
        tomSawyer.setAuthor(twain);
        bookRepository.save(tomSawyer);
    }
}
 
