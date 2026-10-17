package com.cricket.dto;

import java.util.List;

public class MatchSummaryDto {
    private MatchResponseDto match;
    private List<ScoreSnapshotDto> inningsScores;
    private List<PlayerStatisticDto> topBatsmen;
    private List<PlayerStatisticDto> topBowlers;
    private String summaryText;
    private String tossInfo;
    private String manOfTheMatch;

    public MatchSummaryDto() {
    }

    public MatchResponseDto getMatch() {
        return match;
    }

    public void setMatch(MatchResponseDto match) {
        this.match = match;
    }

    public List<ScoreSnapshotDto> getInningsScores() {
        return inningsScores;
    }

    public void setInningsScores(List<ScoreSnapshotDto> inningsScores) {
        this.inningsScores = inningsScores;
    }

    public List<PlayerStatisticDto> getTopBatsmen() {
        return topBatsmen;
    }

    public void setTopBatsmen(List<PlayerStatisticDto> topBatsmen) {
        this.topBatsmen = topBatsmen;
    }

    public List<PlayerStatisticDto> getTopBowlers() {
        return topBowlers;
    }

    public void setTopBowlers(List<PlayerStatisticDto> topBowlers) {
        this.topBowlers = topBowlers;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public String getTossInfo() {
        return tossInfo;
    }

    public void setTossInfo(String tossInfo) {
        this.tossInfo = tossInfo;
    }

    public String getManOfTheMatch() {
        return manOfTheMatch;
    }

    public void setManOfTheMatch(String manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }
}
