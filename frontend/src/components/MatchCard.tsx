import React from 'react';
import { ChevronRight, ArrowRight, MapPin } from 'lucide-react';
import type { Match } from '../types/cricket';

interface MatchCardProps {
  match: Match;
  onSelect: (match: Match) => void;
}

export const MatchCard: React.FC<MatchCardProps> = ({ match, onSelect }) => {
  const isLive = match.matchCategory === 'LIVE';
  const isCompleted = match.matchCategory === 'COMPLETED';

  // Get score for team safely
  const getTeamScore = (teamName?: string, teamShort?: string) => {
    if (!match.scores || match.scores.length === 0) return null;
    const tName = (teamName || '').toLowerCase().trim();
    const tShort = (teamShort || '').toLowerCase().trim();

    return match.scores.find((s) => {
      const inningStr = (s.inning || '').toLowerCase().trim();
      return (tName && inningStr.includes(tName)) || (tShort && inningStr.includes(tShort));
    }) || null;
  };

  const team1Score = getTeamScore(match.team1, match.team1Short) || match.scores?.[0];
  const team2Score = getTeamScore(match.team2, match.team2Short) || (match.scores?.[1] !== team1Score ? match.scores?.[1] : null);

  const getTeamInitial = (short?: string, name?: string) => {
    if (short) return short.substring(0, 3).toUpperCase();
    if (name) return name.substring(0, 3).toUpperCase();
    return 'T1';
  };

  return (
    <div className="figma-match-card" onClick={() => onSelect(match)}>
      <div>
        {/* Card Top Row */}
        <div className="card-top">
          <div className="card-tags">
            {isLive ? (
              <span className="tag-live">
                <span className="pulsing-dot" /> LIVE
              </span>
            ) : isCompleted ? (
              <span className="tag-completed">COMPLETED</span>
            ) : (
              <span className="tag-upcoming">UPCOMING</span>
            )}
            <span className="tag-format">{match.matchType?.toUpperCase() || 'T20'}</span>
          </div>
          <ChevronRight size={16} className="card-chevron" />
        </div>

        {/* Series Title & Subtitle */}
        <div className="card-series-name">
          {match.name || 'International Cricket Series'}
        </div>
        <div className="card-match-sub">
          {isLive ? '3rd T20I • Night match' : isCompleted ? 'Final Match' : 'Scheduled Match'}
        </div>

        {/* Teams List */}
        <div className="card-teams-list">
          {/* Team 1 */}
          <div className="card-team-row">
            <div className="team-badge-name">
              <div className="team-badge-circle">
                {getTeamInitial(match.team1Short, match.team1)}
              </div>
              <span className="team-name-text">{match.team1}</span>
            </div>
            {team1Score ? (
              <div className="team-score-num">
                {team1Score.runs}/{team1Score.wickets}
                <span className="team-overs-num">{team1Score.overs}</span>
              </div>
            ) : isCompleted ? (
              <span className="team-overs-num">-</span>
            ) : (
              <span className="team-overs-num">Yet to bat</span>
            )}
          </div>

          {/* Team 2 */}
          <div className="card-team-row">
            <div className="team-badge-name">
              <div className="team-badge-circle">
                {getTeamInitial(match.team2Short, match.team2)}
              </div>
              <span className="team-name-text">{match.team2}</span>
            </div>
            {team2Score ? (
              <div className="team-score-num">
                {team2Score.runs}/{team2Score.wickets}
                <span className="team-overs-num">{team2Score.overs}</span>
              </div>
            ) : isCompleted ? (
              <span className="team-overs-num">-</span>
            ) : (
              <span className="team-overs-num">Yet to bat</span>
            )}
          </div>
        </div>

        {/* Status Strip */}
        <div
          className={`card-status-bar ${
            isLive ? 'live' : isCompleted ? 'completed' : 'upcoming'
          }`}
        >
          {match.status || match.matchResult || 'Match scheduled'}
        </div>
      </div>

      {/* Card Footer */}
      <div className="card-footer">
        <span className="venue-link" title={match.venue}>
          <MapPin size={13} />
          {match.venue || 'Stadium'}
        </span>
        <span className="details-link">
          <span>Details</span>
          <ArrowRight size={13} />
        </span>
      </div>
    </div>
  );
};
