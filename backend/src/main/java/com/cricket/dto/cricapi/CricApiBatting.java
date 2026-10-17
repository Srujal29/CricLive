package com.cricket.dto.cricapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CricApiBatting {
    private String batsman;

    @JsonProperty("dismissal-info")
    private String dismissalInfo;

    @JsonProperty("R")
    private String runs;

    @JsonProperty("B")
    private String balls;

    @JsonProperty("4s")
    private String fours;

    @JsonProperty("6s")
    private String sixes;

    @JsonProperty("SR")
    private String strikeRate;

    public CricApiBatting() {
    }

    public String getBatsman() {
        return batsman;
    }

    public void setBatsman(String batsman) {
        this.batsman = batsman;
    }

    public String getDismissalInfo() {
        return dismissalInfo;
    }

    public void setDismissalInfo(String dismissalInfo) {
        this.dismissalInfo = dismissalInfo;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getBalls() {
        return balls;
    }

    public void setBalls(String balls) {
        this.balls = balls;
    }

    public String getFours() {
        return fours;
    }

    public void setFours(String fours) {
        this.fours = fours;
    }

    public String getSixes() {
        return sixes;
    }

    public void setSixes(String sixes) {
        this.sixes = sixes;
    }

    public String getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(String strikeRate) {
        this.strikeRate = strikeRate;
    }
}
