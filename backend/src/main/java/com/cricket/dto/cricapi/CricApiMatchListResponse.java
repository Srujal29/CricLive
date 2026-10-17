package com.cricket.dto.cricapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CricApiMatchListResponse {
    private String apikey;
    private String status;
    private List<CricApiMatchData> data;

    public CricApiMatchListResponse() {
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CricApiMatchData> getData() {
        return data;
    }

    public void setData(List<CricApiMatchData> data) {
        this.data = data;
    }
}
