package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class BorrowRequest {
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    private Integer days = 14; // default loan period
}
