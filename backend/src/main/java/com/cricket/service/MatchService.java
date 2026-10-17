package com.cricket.service;

import com.cricket.dto.*;
import com.cricket.dto.cricapi.CricApiMatchData;
import com.cricket.dto.cricapi.CricApiScore;
import com.cricket.dto.cricapi.CricApiTeamInfo;
import com.cricket.entity.MatchEntity;
import com.cricket.entity.PlayerStatistic;
import com.cricket.entity.ScoreSnapshot;
import com.cricket.exception.ResourceNotFoundException;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.PlayerStatisticRepository;
import com.cricket.repository.ScoreSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final ScoreSnapshotRepository scoreSnapshotRepository;
    private final PlayerStatisticRepository playerStatisticRepository;
    private final CricketApiService cricketApiService;

    private LocalDateTime lastRefreshedTime;

    public MatchService(MatchRepository matchRepository,
                        ScoreSnapshotRepository scoreSnapshotRepository,
                        PlayerStatisticRepository playerStatisticRepository,
                        CricketApiService cricketApiService) {
        this.matchRepository = matchRepository;
        this.scoreSnapshotRepository = scoreSnapshotRepository;
        this.playerStatisticRepository = playerStatisticRepository;
        this.cricketApiService = cricketApiService;
    }

    @Transactional(readOnly = true)
    public List<MatchResponseDto> getLiveMatches() {
        List<MatchEntity> matches = matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("LIVE");
        return matches.stream().map(this::mapToMatchResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatchResponseDto> getUpcomingMatches() {
        List<MatchEntity> matches = matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("UPCOMING");
        return matches.stream().map(this::mapToMatchResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatchResponseDto> getCompletedMatches() {
        List<MatchEntity> matches = matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("COMPLETED");
        return matches.stream().map(this::mapToMatchResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatchResponseDto getMatchById(String idOrExternalId) {
        MatchEntity entity = findMatchEntity(idOrExternalId);
        return mapToMatchResponseDto(entity);
    }

    @Transactional
    public List<PlayerStatisticDto> getPlayerStatistics(String idOrExternalId) {
        MatchEntity match = findMatchEntity(idOrExternalId);
        List<PlayerStatistic> stats = match.getPlayerStatistics();

        // If player stats are currently empty and external API is configured, attempt fetch
        if ((stats == null || stats.isEmpty()) && cricketApiService.isApiKeyConfigured()) {
            List<PlayerStatistic> apiStats = cricketApiService.fetchMatchScorecard(match.getExternalId());
            if (apiStats != null && !apiStats.isEmpty()) {
                for (PlayerStatistic p : apiStats) {
                    match.addPlayerStatistic(p);
                }
                matchRepository.save(match);
                stats = match.getPlayerStatistics();
            }
        }

        return stats != null ? stats.stream().map(this::mapToPlayerStatisticDto).collect(Collectors.toList()) : List.of();
    }

    @Transactional(readOnly = true)
    public MatchSummaryDto getMatchSummary(String idOrExternalId) {
        MatchEntity match = findMatchEntity(idOrExternalId);
        MatchResponseDto matchDto = mapToMatchResponseDto(match);

        MatchSummaryDto summary = new MatchSummaryDto();
        summary.setMatch(matchDto);
        summary.setInningsScores(matchDto.getScores());

        List<PlayerStatistic> playerStats = match.getPlayerStatistics();
        if (playerStats != null && !playerStats.isEmpty()) {
            List<PlayerStatisticDto> batsmen = playerStats.stream()
                    .filter(p -> "BATSMAN".equalsIgnoreCase(p.getRole()))
                    .sorted((a, b) -> Integer.compare(b.getRuns() != null ? b.getRuns() : 0, a.getRuns() != null ? a.getRuns() : 0))
                    .limit(5)
                    .map(this::mapToPlayerStatisticDto)
                    .collect(Collectors.toList());
            summary.setTopBatsmen(batsmen);

            List<PlayerStatisticDto> bowlers = playerStats.stream()
                    .filter(p -> "BOWLER".equalsIgnoreCase(p.getRole()))
                    .sorted((a, b) -> Integer.compare(b.getWickets() != null ? b.getWickets() : 0, a.getWickets() != null ? a.getWickets() : 0))
                    .limit(5)
                    .map(this::mapToPlayerStatisticDto)
                    .collect(Collectors.toList());
            summary.setTopBowlers(bowlers);
        } else {
            summary.setTopBatsmen(List.of());
            summary.setTopBowlers(List.of());
        }

        summary.setSummaryText(match.getStatus() != null ? match.getStatus() : match.getMatchResult());
        summary.setTossInfo("Toss details available in match scorecard");
        summary.setManOfTheMatch(match.getMatchEnded() ? "Declared at match conclusion" : "To be decided");

        return summary;
    }

    @Transactional(readOnly = true)
    public ApiStatusDto getApiStatus() {
        ApiStatusDto dto = new ApiStatusDto();
        dto.setKeyConfigured(cricketApiService.isApiKeyConfigured());
        dto.setProvider("CricAPI / CricketData.org");
        dto.setBaseUrl(cricketApiService.getBaseUrl());
        dto.setLastRefresh(lastRefreshedTime != null ? lastRefreshedTime : LocalDateTime.now());
        dto.setTotalMatchesInDb(matchRepository.count());
        dto.setLiveMatchesCount(matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("LIVE").size());
        dto.setUpcomingMatchesCount(matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("UPCOMING").size());
        dto.setCompletedMatchesCount(matchRepository.findByMatchCategoryOrderByLastUpdatedDesc("COMPLETED").size());
        dto.setMessage(cricketApiService.isApiKeyConfigured()
                ? "Active external CricAPI key connected! Real live fixtures are synchronized."
                : "Using offline baseline store. Paste your free CricAPI key to connect and fetch 100% real matches.");
        return dto;
    }

    /**
     * Dynamic API key update & live sync
     */
    @Transactional
    public RefreshResponseDto updateApiKeyAndSync(String newKey) {
        if (newKey == null || newKey.trim().length() < 10) {
            return new RefreshResponseDto(
                    false,
                    "Invalid API key format. CricAPI keys are GUID format strings from https://cricketdata.org",
                    0,
                    0,
                    LocalDateTime.now(),
                    "KEY_FORMAT_ERROR"
            );
        }

        cricketApiService.setApiKey(newKey.trim());

        // Test connection by fetching current matches
        List<CricApiMatchData> apiMatches = cricketApiService.fetchCurrentMatches();
        List<CricApiMatchData> upcomingMatches = cricketApiService.fetchUpcomingMatches();

        if (apiMatches.isEmpty() && upcomingMatches.isEmpty()) {
            return new RefreshResponseDto(
                    false,
                    "Failed to fetch real matches from CricAPI. Please verify that your key is active and has remaining credits at cricketdata.org.",
                    0,
                    0,
                    LocalDateTime.now(),
                    "CRICAPI_VERIFY_FAILED"
            );
        }

        // Successfully verified! Remove sample mock matches so DB has only true real data
        try {
            matchRepository.deleteByExternalIdStartingWith("cric-");
        } catch (Exception e) {
            log.warn("Note deleting baseline fixtures: {}", e.getMessage());
        }

        int updated = 0;
        int inserted = 0;

        for (CricApiMatchData m : apiMatches) {
            if (saveApiMatchToDb(m)) inserted++; else updated++;
        }
        for (CricApiMatchData m : upcomingMatches) {
            if (saveApiMatchToDb(m)) inserted++; else updated++;
        }

        this.lastRefreshedTime = LocalDateTime.now();
        return new RefreshResponseDto(
                true,
                String.format("Successfully connected! Fetched %d real matches from CricAPI.", inserted + updated),
                updated,
                inserted,
                lastRefreshedTime,
                "REAL_CRICAPI_CONNECTED"
        );
    }

    /**
     * Scheduled automated refresh every 45-60 seconds
     */
    @Scheduled(fixedDelayString = "${cricket.api.refresh-interval-seconds:45}000")
    public void scheduledAutoRefresh() {
        if (cricketApiService.isApiKeyConfigured()) {
            log.info("Executing scheduled cricket scores refresh...");
            try {
                refreshMatchesFromApi();
            } catch (Exception e) {
                log.warn("Scheduled refresh encountered an error: {}", e.getMessage());
            }
        }
    }

    /**
     * Main refresh mechanism: syncs with external API or updates timestamps
     */
    @Transactional
    public RefreshResponseDto refreshMatchesFromApi() {
        this.lastRefreshedTime = LocalDateTime.now();

        if (!cricketApiService.isApiKeyConfigured()) {
            return new RefreshResponseDto(
                    true,
                    "Database is current. External API key is not configured; serving stored MySQL snapshot.",
                    0,
                    0,
                    lastRefreshedTime,
                    "OFFLINE_CACHE"
            );
        }

        int updatedCount = 0;
        int insertedCount = 0;

        try {
            List<CricApiMatchData> currentMatches = cricketApiService.fetchCurrentMatches();
            List<CricApiMatchData> upcomingMatches = cricketApiService.fetchUpcomingMatches();

            for (CricApiMatchData apiMatch : currentMatches) {
                if (saveApiMatchToDb(apiMatch)) insertedCount++; else updatedCount++;
            }
            for (CricApiMatchData apiMatch : upcomingMatches) {
                if (saveApiMatchToDb(apiMatch)) insertedCount++; else updatedCount++;
            }

            return new RefreshResponseDto(
                    true,
                    String.format("Successfully synced with CricAPI: %d updated, %d new matches added.", updatedCount, insertedCount),
                    updatedCount,
                    insertedCount,
                    lastRefreshedTime,
                    "CRICAPI_LIVE"
            );

        } catch (Exception ex) {
            log.error("Failed to refresh live cricket data from API: {}", ex.getMessage());
            return new RefreshResponseDto(
                    false,
                    "Error connecting to external API (" + ex.getMessage() + "). Serving cached MySQL data.",
                    0,
                    0,
                    lastRefreshedTime,
                    "API_ERROR_FALLBACK"
            );
        }
    }

    private boolean saveApiMatchToDb(CricApiMatchData apiMatch) {
        if (apiMatch.getId() == null || apiMatch.getId().isBlank()) return false;

        Optional<MatchEntity> existingOpt = matchRepository.findByExternalId(apiMatch.getId());
        MatchEntity match;
        boolean isNew = false;

        if (existingOpt.isPresent()) {
            match = existingOpt.get();
        } else {
            match = new MatchEntity();
            match.setExternalId(apiMatch.getId());
            isNew = true;
        }

        populateEntityFromApiData(match, apiMatch);
        matchRepository.save(match);
        return isNew;
    }

    private void populateEntityFromApiData(MatchEntity entity, CricApiMatchData apiData) {
        entity.setName(apiData.getName() != null ? apiData.getName() : "Cricket Match");
        entity.setMatchType(apiData.getMatchType() != null ? apiData.getMatchType() : "t20");
        entity.setStatus(apiData.getStatus() != null ? apiData.getStatus() : "Live");
        entity.setVenue(apiData.getVenue() != null ? apiData.getVenue() : "Cricket Stadium");
        entity.setDate(apiData.getDate() != null ? apiData.getDate() : "");
        entity.setDateTimeGmt(apiData.getDateTimeGMT() != null ? apiData.getDateTimeGMT() : "");

        boolean started = Boolean.TRUE.equals(apiData.getMatchStarted());
        boolean ended = Boolean.TRUE.equals(apiData.getMatchEnded());

        entity.setMatchStarted(started);
        entity.setMatchEnded(ended);

        if (ended) {
            entity.setMatchCategory("COMPLETED");
            entity.setMatchResult(apiData.getStatus());
        } else if (started) {
            entity.setMatchCategory("LIVE");
            entity.setMatchResult("In progress");
        } else {
            entity.setMatchCategory("UPCOMING");
            entity.setMatchResult("Scheduled");
        }

        // Teams & Logos
        if (apiData.getTeamInfo() != null && !apiData.getTeamInfo().isEmpty()) {
            CricApiTeamInfo t1 = apiData.getTeamInfo().get(0);
            entity.setTeam1(t1.getName());
            entity.setTeam1Short(t1.getShortname());
            entity.setTeam1Img(t1.getImg());

            if (apiData.getTeamInfo().size() > 1) {
                CricApiTeamInfo t2 = apiData.getTeamInfo().get(1);
                entity.setTeam2(t2.getName());
                entity.setTeam2Short(t2.getShortname());
                entity.setTeam2Img(t2.getImg());
            }
        } else if (apiData.getTeams() != null && !apiData.getTeams().isEmpty()) {
            entity.setTeam1(apiData.getTeams().get(0));
            if (apiData.getTeams().size() > 1) {
                entity.setTeam2(apiData.getTeams().get(1));
            }
        }

        entity.setLastUpdated(LocalDateTime.now());

        // Process score snapshots (deduplicate against existing innings snapshots)
        if (apiData.getScore() != null) {
            for (CricApiScore s : apiData.getScore()) {
                String inningName = s.getInning() != null ? s.getInning() : "Innings";
                Optional<ScoreSnapshot> existingSnapshot = entity.getScoreSnapshots().stream()
                        .filter(ss -> inningName.equalsIgnoreCase(ss.getInning()))
                        .findFirst();

                if (existingSnapshot.isPresent()) {
                    ScoreSnapshot snap = existingSnapshot.get();
                    snap.setRuns(s.getR() != null ? s.getR() : 0);
                    snap.setWickets(s.getW() != null ? s.getW() : 0);
                    snap.setOvers(s.getO() != null ? s.getO() : 0.0);
                    snap.setTimestamp(LocalDateTime.now());
                } else {
                    ScoreSnapshot newSnap = new ScoreSnapshot(
                            inningName,
                            s.getR() != null ? s.getR() : 0,
                            s.getW() != null ? s.getW() : 0,
                            s.getO() != null ? s.getO() : 0.0,
                            LocalDateTime.now()
                    );
                    entity.addScoreSnapshot(newSnap);
                }
            }
        }
    }

    private MatchEntity findMatchEntity(String idOrExternalId) {
        if (idOrExternalId == null || idOrExternalId.isBlank()) {
            throw new ResourceNotFoundException("Match ID must not be empty");
        }

        // Try by externalId first
        Optional<MatchEntity> byExternal = matchRepository.findByExternalId(idOrExternalId);
        if (byExternal.isPresent()) {
            return byExternal.get();
        }

        // Try numeric ID
        try {
            Long numericId = Long.parseLong(idOrExternalId);
            return matchRepository.findById(numericId)
                    .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + idOrExternalId));
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Match not found with external id: " + idOrExternalId);
        }
    }

    private MatchResponseDto mapToMatchResponseDto(MatchEntity entity) {
        MatchResponseDto dto = new MatchResponseDto();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setName(entity.getName());
        dto.setMatchType(entity.getMatchType());
        dto.setStatus(entity.getStatus());
        dto.setVenue(entity.getVenue());
        dto.setDate(entity.getDate());
        dto.setDateTimeGmt(entity.getDateTimeGmt());
        dto.setTeam1(entity.getTeam1());
        dto.setTeam2(entity.getTeam2());
        dto.setTeam1Short(entity.getTeam1Short());
        dto.setTeam2Short(entity.getTeam2Short());
        dto.setTeam1Img(entity.getTeam1Img());
        dto.setTeam2Img(entity.getTeam2Img());
        dto.setMatchStarted(entity.getMatchStarted());
        dto.setMatchEnded(entity.getMatchEnded());
        dto.setMatchCategory(entity.getMatchCategory());
        dto.setMatchResult(entity.getMatchResult());
        dto.setLastUpdated(entity.getLastUpdated());

        List<ScoreSnapshotDto> scoreDtos = entity.getScoreSnapshots().stream()
                .map(s -> new ScoreSnapshotDto(s.getId(), s.getInning(), s.getRuns(), s.getWickets(), s.getOvers(), s.getTimestamp()))
                .collect(Collectors.toList());
        dto.setScores(scoreDtos);

        return dto;
    }

    private PlayerStatisticDto mapToPlayerStatisticDto(PlayerStatistic p) {
        PlayerStatisticDto dto = new PlayerStatisticDto();
        dto.setId(p.getId());
        dto.setPlayerName(p.getPlayerName());
        dto.setTeamName(p.getTeamName());
        dto.setRole(p.getRole());
        dto.setRuns(p.getRuns());
        dto.setBalls(p.getBalls());
        dto.setFours(p.getFours());
        dto.setSixes(p.getSixes());
        dto.setStrikeRate(p.getStrikeRate());
        dto.setDismissalInfo(p.getDismissalInfo());
        dto.setOversBowled(p.getOversBowled());
        dto.setMaidens(p.getMaidens());
        dto.setRunsConceded(p.getRunsConceded());
        dto.setWickets(p.getWickets());
        dto.setEconomy(p.getEconomy());
        return dto;
    }
}
