package com.cricket.dto.cricapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CricApiMatchData {
    private String id;
    private String name;
    private String matchType;
    private String status;
    private String venue;
    private String date;
    private String dateTimeGMT;
    private List<String> teams;
    private List<CricApiTeamInfo> teamInfo;
    private List<CricApiScore> score;
    private Boolean matchStarted;
    private Boolean matchEnded;

    public CricApiMatchData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDateTimeGMT() {
        return dateTimeGMT;
    }

    public void setDateTimeGMT(String dateTimeGMT) {
        this.dateTimeGMT = dateTimeGMT;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public List<CricApiTeamInfo> getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(List<CricApiTeamInfo> teamInfo) {
        this.teamInfo = teamInfo;
    }

    public List<CricApiScore> getScore() {
        return score;
    }

    public void setScore(List<CricApiScore> score) {
        this.score = score;
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
}
