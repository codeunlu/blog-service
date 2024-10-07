package dev.codeunlu.blog.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codeunlu.blog.auth.dto.AuthenticationRequest;
import dev.codeunlu.blog.auth.dto.AuthenticationResponse;
import dev.codeunlu.blog.auth.dto.RegisterRequest;
import dev.codeunlu.blog.auth.model.Role;
import dev.codeunlu.blog.auth.model.Token;
import dev.codeunlu.blog.auth.model.TokenType;
import dev.codeunlu.blog.auth.model.User;
import dev.codeunlu.blog.auth.repository.TokenRepository;
import dev.codeunlu.blog.auth.repository.UserRepository;
import dev.codeunlu.blog.auth.security.JwtTokenService;
import dev.codeunlu.blog.common.exception.CustomException;
import dev.codeunlu.blog.common.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtTokenService jwtTokenService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User
        .builder()
        .name(request.name())
        .surname(request.surname())
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .role(Role.AUTHOR)
        .build();

    var registeredUser = userRepository.save(user);
    var jwtToken = jwtTokenService.generateToken(user);
    saveUserToken(registeredUser, jwtToken);
    var refreshToken = jwtTokenService.generateRefreshToken(user);

    return AuthenticationResponse.of(jwtToken, refreshToken);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()
        )
    );

    var user = userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(ErrorMessage
        .NOT_FOUND_USER.getMessage()));
    var jwtToken = jwtTokenService.generateToken(user);
    revokeAllUserToken(user);
    saveUserToken(user, jwtToken);
    var refreshToken = jwtTokenService.generateRefreshToken(user);

    return AuthenticationResponse.of(jwtToken, refreshToken);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    refreshToken = authHeader.substring(7);
    userEmail = jwtTokenService.extractUsername(refreshToken);

    if (userEmail != null) {
      var user = this.userRepository
          .findByEmail(userEmail)
          .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_USER.getMessage()));

      if (jwtTokenService.validateToken(refreshToken, user)) {
        var accessToken = jwtTokenService.generateToken(user);
        revokeAllUserToken(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.of(accessToken, refreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  private void revokeAllUserToken(User user) {
    var validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }

    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoke(true);
    });

    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(User registeredUser, String jwtToken) {
    var token = Token.builder()
        .user(registeredUser)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoke(false)
        .build();
    tokenRepository.save(token);
  }
}
