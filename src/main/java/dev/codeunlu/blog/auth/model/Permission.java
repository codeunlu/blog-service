package dev.codeunlu.blog.auth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

  ANONYMOUS_READ("anonymous:read"),
  ANONYMOUS_UPDATE("anonymous:update"),
  ANONYMOUS_CREATE("anonymous:create"),
  ANONYMOUS_DELETE("anonymous:delete"),

  AUTHOR_READ("author:read"),
  AUTHOR_UPDATE("author:update"),
  AUTHOR_CREATE("author:create"),
  AUTHOR_DELETE("author:delete"),

  MODERATOR_READ("moderator:read"),
  MODERATOR_UPDATE("moderator:update"),
  MODERATOR_CREATE("moderator:create"),
  MODERATOR_DELETE("moderator:delete"),

  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_CREATE("admin:create"),
  ADMIN_DELETE("admin:delete"),

  ;

  private final String permission;
}
