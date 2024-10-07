package dev.codeunlu.blog.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
   @JsonProperty("access_token")
   String accessToken,
   @JsonProperty("refresh_token")
   String refreshToken
) {
  public static AuthenticationResponse of(String accessToken, String refreshToken) {
    return new AuthenticationResponse(accessToken, refreshToken);
  }
}
