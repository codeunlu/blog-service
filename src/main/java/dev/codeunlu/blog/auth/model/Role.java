package dev.codeunlu.blog.auth.model;

import static dev.codeunlu.blog.auth.model.Permission.ADMIN_CREATE;
import static dev.codeunlu.blog.auth.model.Permission.ADMIN_DELETE;
import static dev.codeunlu.blog.auth.model.Permission.ADMIN_READ;
import static dev.codeunlu.blog.auth.model.Permission.ADMIN_UPDATE;
import static dev.codeunlu.blog.auth.model.Permission.ANONYMOUS_READ;
import static dev.codeunlu.blog.auth.model.Permission.AUTHOR_CREATE;
import static dev.codeunlu.blog.auth.model.Permission.AUTHOR_READ;
import static dev.codeunlu.blog.auth.model.Permission.AUTHOR_UPDATE;
import static dev.codeunlu.blog.auth.model.Permission.MODERATOR_CREATE;
import static dev.codeunlu.blog.auth.model.Permission.MODERATOR_DELETE;
import static dev.codeunlu.blog.auth.model.Permission.MODERATOR_READ;
import static dev.codeunlu.blog.auth.model.Permission.MODERATOR_UPDATE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
  ANONYMOUS(
      Set.of(
          ANONYMOUS_READ
      )
  ),
  AUTHOR(
      Set.of(
          AUTHOR_READ,
          AUTHOR_CREATE,
          AUTHOR_UPDATE
      )
  ),
  MODERATOR(
      Set.of(
          MODERATOR_READ,
          MODERATOR_CREATE,
          MODERATOR_UPDATE,
          MODERATOR_DELETE
      )
  ),
  ADMIN(
      Set.of(
          ADMIN_READ,
          ADMIN_CREATE,
          ADMIN_UPDATE,
          ADMIN_DELETE
      )
  );

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
    return authorities;
  }
}