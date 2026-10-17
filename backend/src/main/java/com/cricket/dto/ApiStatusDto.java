package com.cricket.dto;

import java.time.LocalDateTime;

public class ApiStatusDto {
    private boolean keyConfigured;
    private String provider;
    private String baseUrl;
    private LocalDateTime lastRefresh;
    private long totalMatchesInDb;
    private long liveMatchesCount;
    private long upcomingMatchesCount;
    private long completedMatchesCount;
    private String message;

    public ApiStatusDto() {
    }

    public boolean isKeyConfigured() {
        return keyConfigured;
    }

    public void setKeyConfigured(boolean keyConfigured) {
        this.keyConfigured = keyConfigured;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public LocalDateTime getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(LocalDateTime lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public long getTotalMatchesInDb() {
        return totalMatchesInDb;
    }

    public void setTotalMatchesInDb(long totalMatchesInDb) {
        this.totalMatchesInDb = totalMatchesInDb;
    }

    public long getLiveMatchesCount() {
        return liveMatchesCount;
    }

    public void setLiveMatchesCount(long liveMatchesCount) {
        this.liveMatchesCount = liveMatchesCount;
    }

    public long getUpcomingMatchesCount() {
        return upcomingMatchesCount;
    }

    public void setUpcomingMatchesCount(long upcomingMatchesCount) {
        this.upcomingMatchesCount = upcomingMatchesCount;
    }

    public long getCompletedMatchesCount() {
        return completedMatchesCount;
    }

    public void setCompletedMatchesCount(long completedMatchesCount) {
        this.completedMatchesCount = completedMatchesCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
