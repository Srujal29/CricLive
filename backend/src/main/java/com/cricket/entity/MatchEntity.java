package com.cricket.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches", indexes = {
    @Index(name = "idx_external_id", columnList = "externalId"),
    @Index(name = "idx_match_category", columnList = "matchCategory")
})
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String externalId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 50)
    private String matchType;

    @Column(length = 255)
    private String status;

    @Column(length = 255)
    private String venue;

    @Column(length = 50)
    private String date;

    @Column(length = 50)
    private String dateTimeGmt;

    @Column(length = 100)
    private String team1;

    @Column(length = 100)
    private String team2;

    @Column(length = 20)
    private String team1Short;

    @Column(length = 20)
    private String team2Short;

    @Column(length = 500)
    private String team1Img;

    @Column(length = 500)
    private String team2Img;

    private Boolean matchStarted = false;

    private Boolean matchEnded = false;

    @Column(length = 50)
    private String matchCategory; // LIVE, UPCOMING, COMPLETED

    @Column(length = 255)
    private String matchResult;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ScoreSnapshot> scoreSnapshots = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlayerStatistic> playerStatistics = new ArrayList<>();

    public MatchEntity() {
    }

    // Helper methods for bidirectional relationships
    public void addScoreSnapshot(ScoreSnapshot snapshot) {
        scoreSnapshots.add(snapshot);
        snapshot.setMatch(this);
    }

    public void addPlayerStatistic(PlayerStatistic player) {
        playerStatistics.add(player);
        player.setMatch(this);
    }

    public void clearScoreSnapshots() {
        scoreSnapshots.clear();
    }

    public void clearPlayerStatistics() {
        playerStatistics.clear();
    }

    // Getters and Setters
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

    public List<ScoreSnapshot> getScoreSnapshots() {
        return scoreSnapshots;
    }

    public void setScoreSnapshots(List<ScoreSnapshot> scoreSnapshots) {
        this.scoreSnapshots = scoreSnapshots;
    }

    public List<PlayerStatistic> getPlayerStatistics() {
        return playerStatistics;
    }

    public void setPlayerStatistics(List<PlayerStatistic> playerStatistics) {
        this.playerStatistics = playerStatistics;
    }
}
