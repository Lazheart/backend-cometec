package com.demo.DBPBackend.exceptions;

public class InvalidCategoryException extends RuntimeException {
    
    public InvalidCategoryException(String message) {
        super(message);
    }
    
    public InvalidCategoryException(String category, String validCategories) {
        super("Invalid category: '" + category + "'. Valid categories are: " + validCategories);
    }
} 