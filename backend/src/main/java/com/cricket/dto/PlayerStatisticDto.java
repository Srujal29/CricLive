package com.cricket.dto;

public class PlayerStatisticDto {
    private Long id;
    private String playerName;
    private String teamName;
    private String role;
    private Integer runs;
    private Integer balls;
    private Integer fours;
    private Integer sixes;
    private Double strikeRate;
    private String dismissalInfo;
    private Double oversBowled;
    private Integer maidens;
    private Integer runsConceded;
    private Integer wickets;
    private Double economy;

    public PlayerStatisticDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
