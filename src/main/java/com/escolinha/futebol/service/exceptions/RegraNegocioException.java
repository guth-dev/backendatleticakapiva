package com.escolinha.futebol.service.exceptions;

// Usamos RuntimeException para não forçar quem chama o método
// a usar "try-catch" (unchecked exception)
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
        super(message);
    }
}