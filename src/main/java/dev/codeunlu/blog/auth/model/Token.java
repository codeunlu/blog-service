package dev.codeunlu.blog.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  private boolean expired;

  private boolean revoke;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
