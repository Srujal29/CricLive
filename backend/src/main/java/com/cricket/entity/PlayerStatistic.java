package com.cricket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "player_statistics", indexes = {
    @Index(name = "idx_player_match", columnList = "match_id")
})
public class PlayerStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @JsonIgnore
    private MatchEntity match;

    @Column(nullable = false, length = 100)
    private String playerName;

    @Column(length = 100)
    private String teamName;

    @Column(length = 20)
    private String role; // BATSMAN, BOWLER

    // Batting stats
    private Integer runs = 0;
    private Integer balls = 0;
    private Integer fours = 0;
    private Integer sixes = 0;
    private Double strikeRate = 0.0;

    @Column(length = 200)
    private String dismissalInfo;

    // Bowling stats
    private Double oversBowled = 0.0;
    private Integer maidens = 0;
    private Integer runsConceded = 0;
    private Integer wickets = 0;
    private Double economy = 0.0;

    public PlayerStatistic() {
    }

    // Helper static constructors
    public static PlayerStatistic createBatsman(String playerName, String teamName, Integer runs, Integer balls,
                                                Integer fours, Integer sixes, Double strikeRate, String dismissalInfo) {
        PlayerStatistic p = new PlayerStatistic();
        p.setPlayerName(playerName);
        p.setTeamName(teamName);
        p.setRole("BATSMAN");
        p.setRuns(runs != null ? runs : 0);
        p.setBalls(balls != null ? balls : 0);
        p.setFours(fours != null ? fours : 0);
        p.setSixes(sixes != null ? sixes : 0);
        p.setStrikeRate(strikeRate != null ? strikeRate : 0.0);
        p.setDismissalInfo(dismissalInfo != null ? dismissalInfo : "not out");
        return p;
    }

    public static PlayerStatistic createBowler(String playerName, String teamName, Double oversBowled,
                                               Integer maidens, Integer runsConceded, Integer wickets, Double economy) {
        PlayerStatistic p = new PlayerStatistic();
        p.setPlayerName(playerName);
        p.setTeamName(teamName);
        p.setRole("BOWLER");
        p.setOversBowled(oversBowled != null ? oversBowled : 0.0);
        p.setMaidens(maidens != null ? maidens : 0);
        p.setRunsConceded(runsConceded != null ? runsConceded : 0);
        p.setWickets(wickets != null ? wickets : 0);
        p.setEconomy(economy != null ? economy : 0.0);
        return p;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getRuns() {
        return runs;
    }

    public void setRuns(Integer runs) {
        this.runs = runs;
    }

    public Integer getBalls() {
        return balls;
    }

    public void setBalls(Integer balls) {
        this.balls = balls;
    }

    public Integer getFours() {
        return fours;
    }

    public void setFours(Integer fours) {
        this.fours = fours;
    }

    public Integer getSixes() {
        return sixes;
    }

    public void setSixes(Integer sixes) {
        this.sixes = sixes;
    }

    public Double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(Double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public String getDismissalInfo() {
        return dismissalInfo;
    }

    public void setDismissalInfo(String dismissalInfo) {
        this.dismissalInfo = dismissalInfo;
    }

    public Double getOversBowled() {
        return oversBowled;
    }

    public void setOversBowled(Double oversBowled) {
        this.oversBowled = oversBowled;
    }

    public Integer getMaidens() {
        return maidens;
    }

    public void setMaidens(Integer maidens) {
        this.maidens = maidens;
    }

    public Integer getRunsConceded() {
        return runsConceded;
    }

    public void setRunsConceded(Integer runsConceded) {
        this.runsConceded = runsConceded;
    }

    public Integer getWickets() {
        return wickets;
    }

    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }

    public Double getEconomy() {
        return economy;
    }

    public void setEconomy(Double economy) {
        this.economy = economy;
    }
}
