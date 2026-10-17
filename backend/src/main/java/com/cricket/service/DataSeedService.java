package com.cricket.service;

import com.cricket.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataSeedService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeedService.class);

    private final MatchRepository matchRepository;
    private final CricketApiService cricketApiService;
    private final MatchService matchService;

    public DataSeedService(MatchRepository matchRepository,
                           CricketApiService cricketApiService,
                           MatchService matchService) {
        this.matchRepository = matchRepository;
        this.cricketApiService = cricketApiService;
        this.matchService = matchService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Clear old baseline demo matches (IDs starting with cric-)
        try {
            matchRepository.deleteByExternalIdStartingWith("cric-");
            log.info("Cleared any demo baseline matches.");
        } catch (Exception e) {
            log.warn("Notice during cleanup: {}", e.getMessage());
        }

        if (cricketApiService.isApiKeyConfigured()) {
            log.info("CricAPI key is active! Fetching real live matches on startup...");
            try {
                matchService.refreshMatchesFromApi();
            } catch (Exception e) {
                log.error("Error during initial CricAPI live sync: {}", e.getMessage());
            }
        }
    }
}
