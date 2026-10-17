package com.cricket.dto.cricapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CricApiBowling {
    private String bowler;

    @JsonProperty("O")
    private String overs;

    @JsonProperty("M")
    private String maidens;

    @JsonProperty("R")
    private String runs;

    @JsonProperty("W")
    private String wickets;

    @JsonProperty("ECON")
    private String economy;

    public CricApiBowling() {
    }

    public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }

    public String getOvers() {
        return overs;
    }

    public void setOvers(String overs) {
        this.overs = overs;
    }

    public String getMaidens() {
        return maidens;
    }

    public void setMaidens(String maidens) {
        this.maidens = maidens;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getWickets() {
        return wickets;
    }

    public void setWickets(String wickets) {
        this.wickets = wickets;
    }

    public String getEconomy() {
        return economy;
    }

    public void setEconomy(String economy) {
        this.economy = economy;
    }
}
