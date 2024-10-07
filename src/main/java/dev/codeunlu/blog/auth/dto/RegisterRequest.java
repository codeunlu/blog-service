package dev.codeunlu.blog.auth.dto;

public record RegisterRequest(
    String name,
    String surname,
    String email,
    String password
) {

  public static RegisterRequest of(String name, String surname, String email, String password) {
    return new RegisterRequest(name, surname, email, password);
  }
}
