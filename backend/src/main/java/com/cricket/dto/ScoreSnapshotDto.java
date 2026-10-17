package com.cricket.dto;

import java.time.LocalDateTime;

public class ScoreSnapshotDto {
    private Long id;
    private String inning;
    private Integer runs;
    private Integer wickets;
    private Double overs;
    private LocalDateTime timestamp;

    public ScoreSnapshotDto() {
    }

    public ScoreSnapshotDto(Long id, String inning, Integer runs, Integer wickets, Double overs, LocalDateTime timestamp) {
        this.id = id;
        this.inning = inning;
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overs;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInning() {
        return inning;
    }

    public void setInning(String inning) {
        this.inning = inning;
    }

    public Integer getRuns() {
        return runs;
    }

    public void setRuns(Integer runs) {
        this.runs = runs;
    }

    public Integer getWickets() {
        return wickets;
    }

    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }

    public Double getOvers() {
        return overs;
    }

    public void setOvers(Double overs) {
        this.overs = overs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
