package com.iut.banque.exceptions;

public class PasswordHashingException extends RuntimeException {
  public PasswordHashingException(String message, Throwable cause) {
    super(message, cause);
  }
}