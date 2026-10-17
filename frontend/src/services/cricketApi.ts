import axios from 'axios';
import type { Match, MatchSummary, PlayerStatistic, RefreshResponse, ApiStatus } from '../types/cricket';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/matches';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 12000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const cricketApi = {
  // Fetch live ongoing matches
  async getLiveMatches(): Promise<Match[]> {
    const response = await apiClient.get<Match[]>('/live');
    return response.data;
  },

  // Fetch upcoming scheduled matches
  async getUpcomingMatches(): Promise<Match[]> {
    const response = await apiClient.get<Match[]>('/upcoming');
    return response.data;
  },

  // Fetch completed matches and final results
  async getCompletedMatches(): Promise<Match[]> {
    const response = await apiClient.get<Match[]>('/completed');
    return response.data;
  },

  // Fetch single match by ID or external ID
  async getMatchById(id: string | number): Promise<Match> {
    const response = await apiClient.get<Match>(`/${id}`);
    return response.data;
  },

  // Fetch player statistics for a match
  async getPlayerStatistics(id: string | number): Promise<PlayerStatistic[]> {
    const response = await apiClient.get<PlayerStatistic[]>(`/${id}/players`);
    return response.data;
  },

  // Fetch comprehensive match summary with innings breakdown & top performers
  async getMatchSummary(id: string | number): Promise<MatchSummary> {
    const response = await apiClient.get<MatchSummary>(`/${id}/summary`);
    return response.data;
  },

  // Trigger manual refresh on backend
  async refreshScores(): Promise<RefreshResponse> {
    const response = await apiClient.post<RefreshResponse>('/refresh');
    return response.data;
  },

  // Get external API status and MySQL db records count
  async getApiStatus(): Promise<ApiStatus> {
    const response = await apiClient.get<ApiStatus>('/status');
    return response.data;
  },

  // Configure real CricAPI key and synchronize live data immediately
  async configureApiKey(apiKey: string): Promise<RefreshResponse> {
    const response = await apiClient.post<RefreshResponse>('/config/api-key', { apiKey });
    return response.data;
  },

  // Seed baseline matches
  async seedBaselineData(): Promise<string> {
    const response = await apiClient.post<string>('/seed-sample-data');
    return response.data;
  },
};
