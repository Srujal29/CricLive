package com.cricket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_snapshots", indexes = {
    @Index(name = "idx_snapshot_match", columnList = "match_id")
})
public class ScoreSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @JsonIgnore
    private MatchEntity match;

    @Column(nullable = false, length = 150)
    private String inning;

    private Integer runs = 0;

    private Integer wickets = 0;

    private Double overs = 0.0;

    private LocalDateTime timestamp;

    public ScoreSnapshot() {
    }

    public ScoreSnapshot(String inning, Integer runs, Integer wickets, Double overs, LocalDateTime timestamp) {
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

    public MatchEntity getMatch() {
        return match;
    }

    public void setMatch(MatchEntity match) {
        this.match = match;
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
