export interface ScoreSnapshot {
  id?: number;
  inning: string;
  runs: number;
  wickets: number;
  overs: number;
  timestamp?: string;
}

export interface PlayerStatistic {
  id?: number;
  playerName: string;
  teamName?: string;
  role: 'BATSMAN' | 'BOWLER' | string;
  runs?: number;
  balls?: number;
  fours?: number;
  sixes?: number;
  strikeRate?: number;
  dismissalInfo?: string;
  oversBowled?: number;
  maidens?: number;
  runsConceded?: number;
  wickets?: number;
  economy?: number;
}

export interface Match {
  id: number;
  externalId: string;
  name: string;
  matchType: string;
  status: string;
  venue: string;
  date: string;
  dateTimeGmt?: string;
  team1: string;
  team2: string;
  team1Short?: string;
  team2Short?: string;
  team1Img?: string;
  team2Img?: string;
  matchStarted: boolean;
  matchEnded: boolean;
  matchCategory: 'LIVE' | 'UPCOMING' | 'COMPLETED' | string;
  matchResult?: string;
  lastUpdated: string;
  scores: ScoreSnapshot[];
}

export interface MatchSummary {
  match: Match;
  inningsScores: ScoreSnapshot[];
  topBatsmen: PlayerStatistic[];
  topBowlers: PlayerStatistic[];
  summaryText: string;
  tossInfo?: string;
  manOfTheMatch?: string;
}

export interface RefreshResponse {
  success: boolean;
  message: string;
  matchesUpdated: number;
  matchesInserted: number;
  refreshedAt: string;
  providerStatus: string;
}

export interface ApiStatus {
  keyConfigured: boolean;
  provider: string;
  baseUrl: string;
  lastRefresh: string;
  totalMatchesInDb: number;
  liveMatchesCount: number;
  upcomingMatchesCount: number;
  completedMatchesCount: number;
  message: string;
}
