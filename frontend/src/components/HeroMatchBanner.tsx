import React from 'react';
import { ArrowRight, MapPin } from 'lucide-react';
import type { Match } from '../types/cricket';

interface HeroMatchBannerProps {
  match: Match;
  onViewScorecard: (match: Match) => void;
}

export const HeroMatchBanner: React.FC<HeroMatchBannerProps> = ({ match, onViewScorecard }) => {
  // Helper for team circular badge color
  const getBadgeColor = (shortName?: string, name?: string) => {
    const s = (shortName || name || '').toUpperCase();
    if (s.includes('IND')) return 'blue';
    if (s.includes('AUS')) return 'gold';
    if (s.includes('ENG') || s.includes('GB')) return 'blue';
    if (s.includes('SA') || s.includes('PAK') || s.includes('NZ')) return 'green';
    return 'purple';
  };

  const t1Name = (match.team1 || '').toLowerCase().trim();
  const t1Short = (match.team1Short || '').toLowerCase().trim();
  const t2Name = (match.team2 || '').toLowerCase().trim();
  const t2Short = (match.team2Short || '').toLowerCase().trim();

  const team1Score = match.scores?.find(
    (s) => {
      const inn = (s.inning || '').toLowerCase().trim();
      return (t1Name && inn.includes(t1Name)) || (t1Short && inn.includes(t1Short));
    }
  ) || match.scores?.[0];

  const team2Score = match.scores?.find(
    (s) => {
      const inn = (s.inning || '').toLowerCase().trim();
      return (t2Name && inn.includes(t2Name)) || (t2Short && inn.includes(t2Short));
    }
  ) || (match.scores && match.scores.length > 1 && match.scores[1] !== team1Score ? match.scores[1] : null);

  const hasT1Score = Boolean(team1Score);
  const hasT2Score = Boolean(team2Score);

  return (
    <div className="hero-banner">
      {/* Top Meta Row */}
      <div className="hero-banner-top">
        <div className="badge-row">
          <span className="badge-live-pill">
            <span className="pulsing-dot" /> {match.matchCategory || 'LIVE'}
          </span>
          <span className="badge-meta-pill">
            {match.matchType?.toUpperCase() || 'ODI'} • {match.status?.toUpperCase().includes('OPT TO') ? 'IN PROGRESS' : 'MATCH CENTRE'}
          </span>
        </div>
        <span className="match-id-pill">ID: {match.externalId?.substring(0, 8)}...</span>
      </div>

      {/* Series Title */}
      <div className="series-title">
        {match.name || 'International Cricket Championship'}
      </div>

      {/* Center Layout: Teams vs Situation */}
      <div className="hero-match-layout">
        {/* Teams & Scores */}
        <div className="teams-scores-block">
          {/* Team 1 */}
          <div className="team-score-row">
            <div className="team-left">
              <div className={`circle-badge ${getBadgeColor(match.team1Short, match.team1)}`}>
                {match.team1Short || match.team1?.substring(0, 3).toUpperCase()}
              </div>
              <div>
                <div className="team-name-large">{match.team1}</div>
                <div className="team-sub-status">{hasT1Score ? 'Batting' : 'Yet to bat'}</div>
              </div>
            </div>
            <div className="score-display-large">
              <span>{hasT1Score ? `${team1Score?.runs}/${team1Score?.wickets}` : (match.matchEnded ? '-' : 'Yet to bat')}</span>
              {hasT1Score && <span className="score-overs-large">{team1Score?.overs} overs</span>}
            </div>
          </div>

          {/* Team 2 */}
          <div className="team-score-row">
            <div className="team-left">
              <div className={`circle-badge ${getBadgeColor(match.team2Short, match.team2)}`}>
                {match.team2Short || match.team2?.substring(0, 3).toUpperCase()}
              </div>
              <div>
                <div className="team-name-large">{match.team2}</div>
                <div className="team-sub-status">{hasT2Score ? 'Innings' : 'Yet to bat'}</div>
              </div>
            </div>
            <div className="score-display-large">
              <span>{hasT2Score ? `${team2Score?.runs}/${team2Score?.wickets}` : (match.matchEnded ? '-' : 'Yet to bat')}</span>
              {hasT2Score && <span className="score-overs-large">{team2Score?.overs} overs</span>}
            </div>
          </div>
        </div>

        {/* Current Situation Box */}
        <div className="situation-block">
          <div className="situation-label">Current Situation</div>
          <div className="situation-highlight">
            {match.status || match.matchResult || 'Live match in progress'}
          </div>
          <div className="situation-venue">
            <MapPin size={14} color="#94a3b8" />
            <span>{match.venue || 'International Cricket Ground'}</span>
          </div>
        </div>
      </div>

      {/* Bottom Row */}
      <div className="hero-banner-footer">
        <div className="live-coverage-note">
          <span className="pulsing-dot" />
          <span>Live coverage • Synchronized via Spring Boot</span>
        </div>
        <button className="btn-scorecard-green" onClick={() => onViewScorecard(match)}>
          <span>View scorecard</span>
          <ArrowRight size={16} />
        </button>
      </div>
    </div>
  );
};
