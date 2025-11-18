package com.efioco.ticketsystem.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Body per richiedere un nuovo access token con refresh token")
public class TokenRefreshRequest {

	@Schema(description = "Refresh token salvato a DB", example = "abc123def456...")
	private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
