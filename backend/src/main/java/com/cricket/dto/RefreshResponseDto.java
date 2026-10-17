package com.cricket.dto;

import java.time.LocalDateTime;

public class RefreshResponseDto {
    private boolean success;
    private String message;
    private int matchesUpdated;
    private int matchesInserted;
    private LocalDateTime refreshedAt;
    private String providerStatus;

    public RefreshResponseDto() {
    }

    public RefreshResponseDto(boolean success, String message, int matchesUpdated, int matchesInserted,
                              LocalDateTime refreshedAt, String providerStatus) {
        this.success = success;
        this.message = message;
        this.matchesUpdated = matchesUpdated;
        this.matchesInserted = matchesInserted;
        this.refreshedAt = refreshedAt;
        this.providerStatus = providerStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMatchesUpdated() {
        return matchesUpdated;
    }

    public void setMatchesUpdated(int matchesUpdated) {
        this.matchesUpdated = matchesUpdated;
    }

    public int getMatchesInserted() {
        return matchesInserted;
    }

    public void setMatchesInserted(int matchesInserted) {
        this.matchesInserted = matchesInserted;
    }

    public LocalDateTime getRefreshedAt() {
        return refreshedAt;
    }

    public void setRefreshedAt(LocalDateTime refreshedAt) {
        this.refreshedAt = refreshedAt;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }
}
