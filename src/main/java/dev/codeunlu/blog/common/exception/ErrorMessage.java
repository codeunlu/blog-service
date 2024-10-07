package dev.codeunlu.blog.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
  NOT_FOUND_USER("WPS-01", "Kullanıcı sistemde kayıtlı değildir!");

  private final String code;
  private final String message;
}
