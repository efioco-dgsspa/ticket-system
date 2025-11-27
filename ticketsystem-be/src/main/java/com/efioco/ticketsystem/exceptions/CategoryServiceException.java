package com.efioco.ticketsystem.exceptions;

public class CategoryServiceException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CategoryServiceException(String message) {
        super(message);
    }
}
