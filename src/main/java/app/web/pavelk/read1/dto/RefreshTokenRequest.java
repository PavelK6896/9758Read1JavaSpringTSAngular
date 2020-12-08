package app.web.pavelk.read1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
    private String username;
}
