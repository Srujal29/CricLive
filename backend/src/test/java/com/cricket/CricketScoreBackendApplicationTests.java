package com.cricket;

import com.cricket.dto.MatchResponseDto;
import com.cricket.service.MatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CricketScoreBackendApplicationTests {

	@Autowired
	private MatchService matchService;

	@Test
	@DisplayName("Application Context Loads Successfully")
	void contextLoads() {
		assertNotNull(matchService, "MatchService should be injected into application context");
	}

	@Test
	@DisplayName("Live matches are fetched from database or seed baseline")
	void testGetLiveMatches() {
		List<MatchResponseDto> liveMatches = matchService.getLiveMatches();
		assertNotNull(liveMatches, "Live matches list should not be null");
		assertFalse(liveMatches.isEmpty(), "Baseline live matches should be populated");
	}

	@Test
	@DisplayName("Completed matches are fetched from database")
	void testGetCompletedMatches() {
		List<MatchResponseDto> completed = matchService.getCompletedMatches();
		assertNotNull(completed, "Completed matches list should not be null");
		assertFalse(completed.isEmpty(), "Baseline completed matches should be populated");
	}
}
