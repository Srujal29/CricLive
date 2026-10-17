package com.cricket.service;

import com.cricket.dto.cricapi.CricApiMatchData;
import com.cricket.dto.cricapi.CricApiScore;
import com.cricket.dto.cricapi.CricApiTeamInfo;
import com.cricket.entity.PlayerStatistic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CricketApiService {

    private static final Logger log = LoggerFactory.getLogger(CricketApiService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${cricket.api.base-url:https://api.cricapi.com/v1}")
    private String baseUrl;

    @Value("${cricket.api.key:test-api-key}")
    private String apiKey;

    public CricketApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean isApiKeyConfigured() {
        return apiKey != null &&
               !apiKey.isBlank() &&
               !apiKey.equalsIgnoreCase("test-api-key") &&
               !apiKey.equalsIgnoreCase("your_free_cricapi_key_here") &&
               apiKey.length() > 10;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
    }

    /**
     * Fetch live/current matches from CricAPI
     */
    public List<CricApiMatchData> fetchCurrentMatches() {
        if (!isApiKeyConfigured()) {
            log.info("CricAPI key is not configured or using placeholder. Skipping live API call.");
            return List.of();
        }

        String url = String.format("%s/currentMatches?apikey=%s&offset=0", baseUrl, apiKey);
        return executeMatchListQuery(url, "current matches");
    }

    /**
     * Fetch scheduled / upcoming matches from CricAPI
     */
    public List<CricApiMatchData> fetchUpcomingMatches() {
        if (!isApiKeyConfigured()) {
            return List.of();
        }

        String url = String.format("%s/matches?apikey=%s&offset=0", baseUrl, apiKey);
        return executeMatchListQuery(url, "scheduled matches");
    }

    /**
     * Fetch scorecard and player statistics for a match if supported
     */
    public List<PlayerStatistic> fetchMatchScorecard(String externalMatchId) {
        if (!isApiKeyConfigured() || externalMatchId == null || externalMatchId.isBlank()) {
            return List.of();
        }

        String url = String.format("%s/match_scorecard?apikey=%s&id=%s", baseUrl, apiKey, externalMatchId);
        List<PlayerStatistic> stats = new ArrayList<>();

        try {
            log.info("Fetching scorecard from CricAPI for match: {}", externalMatchId);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getBody() == null) return stats;

            JsonNode root = objectMapper.readTree(response.getBody());
            if (!"success".equalsIgnoreCase(root.path("status").asText())) {
                log.warn("CricAPI scorecard returned non-success: {}", root.path("status").asText());
                return stats;
            }

            JsonNode dataNode = root.path("data");

            // Parse batting
            JsonNode battingArray = dataNode.path("batting");
            if (battingArray.isArray()) {
                for (JsonNode inningsObj : battingArray) {
                    String title = inningsObj.path("title").asText("");
                    JsonNode scoresNode = inningsObj.path("scores");
                    if (scoresNode.isArray()) {
                        for (JsonNode item : scoresNode) {
                            if (item.isArray()) {
                                for (JsonNode b : item) {
                                    parseBatsman(b, title, stats);
                                }
                            } else {
                                parseBatsman(item, title, stats);
                            }
                        }
                    }
                }
            }

            // Parse bowling
            JsonNode bowlingArray = dataNode.path("bowling");
            if (bowlingArray.isArray()) {
                for (JsonNode bowlObj : bowlingArray) {
                    String title = bowlObj.path("title").asText("");
                    JsonNode scoresNode = bowlObj.path("scores");
                    if (scoresNode.isArray()) {
                        for (JsonNode item : scoresNode) {
                            if (item.isArray()) {
                                for (JsonNode b : item) {
                                    parseBowler(b, title, stats);
                                }
                            } else {
                                parseBowler(item, title, stats);
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            log.warn("Unable to fetch/parse scorecard from external API: {}", ex.getMessage());
        }

        return stats;
    }

    private void parseBatsman(JsonNode b, String inningTitle, List<PlayerStatistic> stats) {
        String batsman = b.path("batsman").asText("");
        if (batsman.isBlank()) batsman = b.path("name").asText("");
        if (batsman.isBlank()) return;

        int runs = parseIntSafe(b.path("R").asText("0"));
        int balls = parseIntSafe(b.path("B").asText("0"));
        int fours = parseIntSafe(b.path("4s").asText("0"));
        int sixes = parseIntSafe(b.path("6s").asText("0"));
        double sr = parseDoubleSafe(b.path("SR").asText("0.0"));
        String dismissal = b.path("dismissal-info").asText("not out");

        stats.add(PlayerStatistic.createBatsman(batsman, inningTitle, runs, balls, fours, sixes, sr, dismissal));
    }

    private void parseBowler(JsonNode b, String inningTitle, List<PlayerStatistic> stats) {
        String bowler = b.path("bowler").asText("");
        if (bowler.isBlank()) bowler = b.path("name").asText("");
        if (bowler.isBlank()) return;

        double overs = parseDoubleSafe(b.path("O").asText("0.0"));
        int maidens = parseIntSafe(b.path("M").asText("0"));
        int runs = parseIntSafe(b.path("R").asText("0"));
        int wickets = parseIntSafe(b.path("W").asText("0"));
        double econ = parseDoubleSafe(b.path("ECON").asText("0.0"));

        stats.add(PlayerStatistic.createBowler(bowler, inningTitle, overs, maidens, runs, wickets, econ));
    }

    private List<CricApiMatchData> executeMatchListQuery(String url, String requestName) {
        List<CricApiMatchData> matches = new ArrayList<>();
        try {
            log.info("Requesting CricAPI for {}", requestName);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getBody() == null) {
                return matches;
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String status = root.path("status").asText("");
            if (!"success".equalsIgnoreCase(status)) {
                String reason = root.path("reason").asText("");
                log.warn("CricAPI {} response status not success: {} ({})", requestName, status, reason);
                return matches;
            }

            JsonNode dataArray = root.path("data");
            if (dataArray.isArray()) {
                for (JsonNode item : dataArray) {
                    CricApiMatchData match = new CricApiMatchData();
                    match.setId(item.path("id").asText(""));
                    match.setName(item.path("name").asText(""));
                    match.setMatchType(item.path("matchType").asText("t20"));
                    match.setStatus(item.path("status").asText(""));
                    match.setVenue(item.path("venue").asText(""));
                    match.setDate(item.path("date").asText(""));
                    match.setDateTimeGMT(item.path("dateTimeGMT").asText(""));
                    match.setMatchStarted(item.path("matchStarted").asBoolean(false));
                    match.setMatchEnded(item.path("matchEnded").asBoolean(false));

                    // Teams
                    List<String> teamList = new ArrayList<>();
                    JsonNode teamsNode = item.path("teams");
                    if (teamsNode.isArray()) {
                        for (JsonNode t : teamsNode) {
                            teamList.add(t.asText());
                        }
                    }
                    match.setTeams(teamList);

                    // Team info
                    List<CricApiTeamInfo> infoList = new ArrayList<>();
                    JsonNode teamInfoNode = item.path("teamInfo");
                    if (teamInfoNode.isArray()) {
                        for (JsonNode ti : teamInfoNode) {
                            CricApiTeamInfo teamInfo = new CricApiTeamInfo();
                            teamInfo.setName(ti.path("name").asText(""));
                            teamInfo.setShortname(ti.path("shortname").asText(""));
                            teamInfo.setImg(ti.path("img").asText(""));
                            infoList.add(teamInfo);
                        }
                    }
                    match.setTeamInfo(infoList);

                    // Scores
                    List<CricApiScore> scoreList = new ArrayList<>();
                    JsonNode scoreNode = item.path("score");
                    if (scoreNode.isArray()) {
                        for (JsonNode s : scoreNode) {
                            CricApiScore score = new CricApiScore();
                            score.setR(s.path("r").asInt(0));
                            score.setW(s.path("w").asInt(0));
                            score.setO(s.path("o").asDouble(0.0));
                            score.setInning(s.path("inning").asText(""));
                            scoreList.add(score);
                        }
                    }
                    match.setScore(scoreList);

                    matches.add(match);
                }
            }

            log.info("Successfully fetched {} matches from CricAPI for {}", matches.size(), requestName);
        } catch (RestClientException ex) {
            log.warn("Network error calling CricAPI for {}: {}", requestName, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error processing CricAPI response for {}: {}", requestName, ex.getMessage(), ex);
        }

        return matches;
    }

    private int parseIntSafe(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String val) {
        try {
            return Double.parseDouble(val.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
