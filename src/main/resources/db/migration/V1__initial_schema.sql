CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- create roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- create authors table
CREATE TABLE authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    biography TEXT,
    birth_date DATE
);

-- create books table
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    publication_date DATE,
    genre VARCHAR(50),
    summary TEXT,
    cover_image VARCHAR(255),
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors(id),
    INDEX idx_title (title),
    INDEX idx_isbn (isbn),
    INDEX idx_genre (genre)
);

-- create borrow_records table
CREATE TABLE borrow_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) NOT NULL,
    fine_amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_due_date (due_date)
);

-- insert default roles
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'System administrator with full access'),
('LIBRARIAN', 'Library staff with book and user management access'),
('MEMBER', 'Library member with borrowing privileges');

-- insert sample admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role_id) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4W', 
        'admin@library.com', 'Admin', 'User', 1);

-- insert sample librarian (password: librarian123)
INSERT INTO users (username, password, email, first_name, last_name, role_id) 
VALUES ('librarian', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4W', 
        'librarian@library.com', 'John', 'Doe', 2);

-- insert sample member (password: member123)
INSERT INTO users (username, password, email, first_name, last_name, role_id) 
VALUES ('member', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4W', 
        'member@library.com', 'Jane', 'Smith', 3);

-- insert sample authors
INSERT INTO authors (first_name, last_name, biography, birth_date) VALUES
('George', 'Orwell', 'English novelist and essayist', '1903-06-25'),
('Jane', 'Austen', 'English novelist known for social commentary', '1775-12-16'),
('Mark', 'Twain', 'American writer and humorist', '1835-11-30'),
('J.K.', 'Rowling', 'British author, best known for Harry Potter series', '1965-07-31'),
('Stephen', 'King', 'American author of horror and supernatural fiction', '1947-09-21');

-- insert sample books
INSERT INTO books (isbn, title, publication_date, genre, summary, total_copies, available_copies, author_id) VALUES
('978-0-452-28423-4', '1984', '1949-06-08', 'Dystopian', 'A dystopian social science fiction novel', 5, 3, 1),
('978-0-14-143951-8', 'Animal Farm', '1945-08-17', 'Political Satire', 'An allegorical novella', 4, 2, 1),
('978-0-14-143972-3', 'Pride and Prejudice', '1813-01-28', 'Romance', 'A romantic novel of manners', 3, 2, 2),
('978-0-486-28061-5', 'The Adventures of Tom Sawyer', '1876-06-01', 'Adventure', 'Story of a young boy growing up along the Mississippi River', 4, 4, 3),
('978-0-7475-3269-9', 'Harry Potter and the Philosopher\'s Stone', '1997-06-26', 'Fantasy', 'First novel in the Harry Potter series', 6, 2, 4),
('978-0-450-04018-4', 'The Shining', '1977-01-28', 'Horror', 'A horror novel about a haunted hotel', 3, 1, 5);
