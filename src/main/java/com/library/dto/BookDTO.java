package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private LocalDate publicationDate;
    private String genre;
    private String summary;
    private String coverImage;
    private Integer totalCopies;
    private Integer availableCopies;
    private String authorName;
    private Long authorId;
}
