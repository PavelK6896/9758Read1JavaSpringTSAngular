package app.web.pavelk.read1.dto;

import lombok.*;

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
