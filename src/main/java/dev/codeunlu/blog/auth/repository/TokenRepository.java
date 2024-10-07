package dev.codeunlu.blog.auth.repository;

import dev.codeunlu.blog.auth.model.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
  @Query("""
        select t from Token t inner join User u on t.user.id = u.id
        where u.id = :userId and (t.expired = false or t.revoke = false)
      """)
  List<Token> findAllValidTokensByUserId(String userId);

  Optional<Token> findByToken(String token);
}
