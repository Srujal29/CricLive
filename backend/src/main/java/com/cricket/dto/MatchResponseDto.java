package com.cricket.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MatchResponseDto {
    private Long id;
    private String externalId;
    private String name;
    private String matchType;
    private String status;
    private String venue;
    private String date;
    private String dateTimeGmt;
    private String team1;
    private String team2;
    private String team1Short;
    private String team2Short;
    private String team1Img;
    private String team2Img;
    private Boolean matchStarted;
    private Boolean matchEnded;
    private String matchCategory; // LIVE, UPCOMING, COMPLETED
    private String matchResult;
    private LocalDateTime lastUpdated;
    private List<ScoreSnapshotDto> scores;

    public MatchResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTimeGmt() {
        return dateTimeGmt;
    }

    public void setDateTimeGmt(String dateTimeGmt) {
        this.dateTimeGmt = dateTimeGmt;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1Short() {
        return team1Short;
    }

    public void setTeam1Short(String team1Short) {
        this.team1Short = team1Short;
    }

    public String getTeam2Short() {
        return team2Short;
    }

    public void setTeam2Short(String team2Short) {
        this.team2Short = team2Short;
    }

    public String getTeam1Img() {
        return team1Img;
    }

    public void setTeam1Img(String team1Img) {
        this.team1Img = team1Img;
    }

    public String getTeam2Img() {
        return team2Img;
    }

    public void setTeam2Img(String team2Img) {
        this.team2Img = team2Img;
    }

    public Boolean getMatchStarted() {
        return matchStarted;
    }

    public void setMatchStarted(Boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    public Boolean getMatchEnded() {
        return matchEnded;
    }

    public void setMatchEnded(Boolean matchEnded) {
        this.matchEnded = matchEnded;
    }

    public String getMatchCategory() {
        return matchCategory;
    }

    public void setMatchCategory(String matchCategory) {
        this.matchCategory = matchCategory;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<ScoreSnapshotDto> getScores() {
        return scores;
    }

    public void setScores(List<ScoreSnapshotDto> scores) {
        this.scores = scores;
    }
}
