package com.cricket.controller;

import com.cricket.dto.*;
import com.cricket.service.DataSeedService;
import com.cricket.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;
    private final DataSeedService dataSeedService;

    public MatchController(MatchService matchService, DataSeedService dataSeedService) {
        this.matchService = matchService;
        this.dataSeedService = dataSeedService;
    }

    /**
     * GET /api/matches/live - Retrieve all currently ongoing matches
     */
    @GetMapping("/live")
    public ResponseEntity<List<MatchResponseDto>> getLiveMatches() {
        return ResponseEntity.ok(matchService.getLiveMatches());
    }

    /**
     * GET /api/matches/upcoming - Retrieve scheduled matches
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<MatchResponseDto>> getUpcomingMatches() {
        return ResponseEntity.ok(matchService.getUpcomingMatches());
    }

    /**
     * GET /api/matches/completed - Retrieve completed matches and results
     */
    @GetMapping("/completed")
    public ResponseEntity<List<MatchResponseDto>> getCompletedMatches() {
        return ResponseEntity.ok(matchService.getCompletedMatches());
    }

    /**
     * GET /api/matches/{id} - Retrieve single match by internal ID or external ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDto> getMatchById(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    /**
     * GET /api/matches/{id}/players - Retrieve player statistics for a match
     */
    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerStatisticDto>> getPlayerStatistics(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getPlayerStatistics(id));
    }

    /**
     * GET /api/matches/{id}/summary - Retrieve match summary, innings details, top performers
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<MatchSummaryDto> getMatchSummary(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatchSummary(id));
    }

    /**
     * POST /api/matches/refresh - Manually triggers live score refresh from external API
     */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refreshMatches() {
        return ResponseEntity.ok(matchService.refreshMatchesFromApi());
    }

    /**
     * GET /api/matches/status - API connectivity, hit limits, and database status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiStatusDto> getApiStatus() {
        return ResponseEntity.ok(matchService.getApiStatus());
    }

    /**
     * POST /api/matches/config/api-key - Configure real CricAPI key and synchronize live data
     */
    @PostMapping("/config/api-key")
    public ResponseEntity<RefreshResponseDto> configureApiKey(@RequestBody Map<String, String> body) {
        String key = body.get("apiKey");
        return ResponseEntity.ok(matchService.updateApiKeyAndSync(key));
    }

    /**
     * POST /api/matches/seed-sample-data - Reset/seed baseline demonstration matches
     */
    @PostMapping("/seed-sample-data")
    public ResponseEntity<String> seedSampleData() {
        dataSeedService.seedSampleMatches();
        return ResponseEntity.ok("Sample baseline matches successfully seeded into MySQL.");
    }
}
