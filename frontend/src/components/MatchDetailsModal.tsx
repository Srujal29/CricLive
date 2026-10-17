import React, { useEffect, useState } from 'react';
import { X, MapPin, Calendar, Clock, Trophy, RefreshCw, UserCheck } from 'lucide-react';
import type { Match, MatchSummary, PlayerStatistic } from '../types/cricket';
import { cricketApi } from '../services/cricketApi';

interface MatchDetailsModalProps {
  match: Match;
  onClose: () => void;
}

export const MatchDetailsModal: React.FC<MatchDetailsModalProps> = ({ match, onClose }) => {
  const [summary, setSummary] = useState<MatchSummary | null>(null);
  const [playerStats, setPlayerStats] = useState<PlayerStatistic[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [activeTab, setActiveTab] = useState<'scorecard' | 'players' | 'info'>('scorecard');

  useEffect(() => {
    let isMounted = true;

    const loadDetails = async () => {
      try {
        setLoading(true);
        const [sumRes, playersRes] = await Promise.all([
          cricketApi.getMatchSummary(match.externalId).catch(() => null),
          cricketApi.getPlayerStatistics(match.externalId).catch(() => []),
        ]);

        if (isMounted) {
          setSummary(sumRes);
          setPlayerStats(playersRes);
        }
      } catch (err) {
        console.error('Failed to load match details:', err);
      } finally {
        if (isMounted) setLoading(false);
      }
    };

    loadDetails();

    // Close on Escape key
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => {
      isMounted = false;
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [match.externalId, onClose]);

  const batsmen = playerStats.filter((p) => p.role === 'BATSMAN');
  const bowlers = playerStats.filter((p) => p.role === 'BOWLER');

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Modal Header */}
        <div className="modal-header">
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '0.35rem' }}>
              <span className="match-type-pill">{match.matchType}</span>
              <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                ID: {match.externalId}
              </span>
            </div>
            <h2 style={{ fontSize: '1.25rem', color: 'white' }}>{match.name}</h2>
          </div>
          <button className="modal-close-btn" onClick={onClose} title="Close modal">
            <X size={20} />
          </button>
        </div>

        {/* Modal Tabs */}
        <div style={{ display: 'flex', gap: '1rem', borderBottom: '1px solid var(--border-subtle)', padding: '0.75rem 1.75rem 0 1.75rem' }}>
          <button
            onClick={() => setActiveTab('scorecard')}
            style={{
              background: 'transparent',
              color: activeTab === 'scorecard' ? 'var(--accent-emerald)' : 'var(--text-secondary)',
              borderBottom: activeTab === 'scorecard' ? '2px solid var(--accent-emerald)' : '2px solid transparent',
              paddingBottom: '0.6rem',
              fontWeight: 600,
              fontSize: '0.9rem',
            }}
          >
            Innings Scorecard
          </button>
          <button
            onClick={() => setActiveTab('players')}
            style={{
              background: 'transparent',
              color: activeTab === 'players' ? 'var(--accent-emerald)' : 'var(--text-secondary)',
              borderBottom: activeTab === 'players' ? '2px solid var(--accent-emerald)' : '2px solid transparent',
              paddingBottom: '0.6rem',
              fontWeight: 600,
              fontSize: '0.9rem',
            }}
          >
            Player Statistics ({playerStats.length})
          </button>
          <button
            onClick={() => setActiveTab('info')}
            style={{
              background: 'transparent',
              color: activeTab === 'info' ? 'var(--accent-emerald)' : 'var(--text-secondary)',
              borderBottom: activeTab === 'info' ? '2px solid var(--accent-emerald)' : '2px solid transparent',
              paddingBottom: '0.6rem',
              fontWeight: 600,
              fontSize: '0.9rem',
            }}
          >
            Match Info
          </button>
        </div>

        {/* Modal Body */}
        <div className="modal-body">
          {/* Status banner */}
          <div
            style={{
              background: 'var(--bg-elevated)',
              border: '1px solid var(--border-subtle)',
              borderRadius: 'var(--radius-sm)',
              padding: '1rem 1.25rem',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              flexWrap: 'wrap',
              gap: '0.5rem',
            }}
          >
            <div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>
                Current Status
              </div>
              <div style={{ fontSize: '1.05rem', fontWeight: 700, color: '#38bdf8' }}>
                {match.status || match.matchResult}
              </div>
              {summary?.summaryText && summary.summaryText !== match.status && (
                <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginTop: '0.25rem' }}>
                  {summary.summaryText}
                </div>
              )}
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Trophy size={18} color="#f59e0b" />
              <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                {match.matchEnded ? (summary?.manOfTheMatch || 'Result Finalized') : 'Live Match In Progress'}
              </span>
            </div>
          </div>

          {loading ? (
            <div style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>
              <RefreshCw size={24} className="spin-animation" style={{ margin: '0 auto 0.75rem auto' }} />
              <div>Loading match breakdown and player stats...</div>
            </div>
          ) : (
            <>
              {/* TAB 1: INNINGS SCORECARD */}
              {activeTab === 'scorecard' && (
                <div>
                  <h3 style={{ fontSize: '1rem', color: 'white', marginBottom: '0.75rem' }}>
                    Innings Breakdown
                  </h3>
                  {match.scores && match.scores.length > 0 ? (
                    <div style={{ overflowX: 'auto' }}>
                      <table className="score-table">
                        <thead>
                          <tr>
                            <th>Innings</th>
                            <th>Runs</th>
                            <th>Wickets</th>
                            <th>Overs</th>
                            <th>Run Rate</th>
                          </tr>
                        </thead>
                        <tbody>
                          {match.scores.map((s, idx) => {
                            const runRate =
                              s.overs > 0 ? (s.runs / s.overs).toFixed(2) : '-';
                            return (
                              <tr key={idx}>
                                <td style={{ fontWeight: 600, color: 'white' }}>{s.inning}</td>
                                <td style={{ fontWeight: 700, color: 'var(--accent-emerald)' }}>
                                  {s.runs}
                                </td>
                                <td>{s.wickets}</td>
                                <td>{s.overs}</td>
                                <td>{runRate}</td>
                              </tr>
                            );
                          })}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <div style={{ color: 'var(--text-muted)', padding: '1.5rem', background: 'var(--bg-elevated)', borderRadius: 'var(--radius-sm)', textAlign: 'center' }}>
                      Innings score data has not commenced or is pending from provider.
                    </div>
                  )}
                </div>
              )}

              {/* TAB 2: PLAYER STATS */}
              {activeTab === 'players' && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                  {/* Batsmen */}
                  <div>
                    <h3 style={{ fontSize: '1rem', color: 'white', marginBottom: '0.75rem' }}>
                      Batting Performers
                    </h3>
                    {batsmen.length > 0 ? (
                      <div style={{ overflowX: 'auto' }}>
                        <table className="score-table">
                          <thead>
                            <tr>
                              <th>Batsman</th>
                              <th>Team / Innings</th>
                              <th>Dismissal</th>
                              <th>R</th>
                              <th>B</th>
                              <th>4s</th>
                              <th>6s</th>
                              <th>SR</th>
                            </tr>
                          </thead>
                          <tbody>
                            {batsmen.map((b, i) => (
                              <tr key={i}>
                                <td style={{ fontWeight: 600, color: 'white' }}>
                                  {b.playerName}
                                </td>
                                <td style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>
                                  {b.teamName || '-'}
                                </td>
                                <td style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>
                                  {b.dismissalInfo || 'not out'}
                                </td>
                                <td style={{ fontWeight: 700, color: '#f8fafc' }}>{b.runs}</td>
                                <td>{b.balls}</td>
                                <td>{b.fours}</td>
                                <td>{b.sixes}</td>
                                <td style={{ color: 'var(--accent-emerald)' }}>{b.strikeRate?.toFixed(1)}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    ) : (
                      <div style={{ color: 'var(--text-muted)', padding: '1rem', background: 'var(--bg-elevated)', borderRadius: 'var(--radius-sm)', fontSize: '0.85rem' }}>
                        Detailed batting player statistics are currently not available for this fixture from the external API free tier.
                      </div>
                    )}
                  </div>

                  {/* Bowlers */}
                  <div>
                    <h3 style={{ fontSize: '1rem', color: 'white', marginBottom: '0.75rem' }}>
                      Bowling Performers
                    </h3>
                    {bowlers.length > 0 ? (
                      <div style={{ overflowX: 'auto' }}>
                        <table className="score-table">
                          <thead>
                            <tr>
                              <th>Bowler</th>
                              <th>Team / Innings</th>
                              <th>Overs</th>
                              <th>Maidens</th>
                              <th>Runs</th>
                              <th>Wickets</th>
                              <th>Economy</th>
                            </tr>
                          </thead>
                          <tbody>
                            {bowlers.map((b, i) => (
                              <tr key={i}>
                                <td style={{ fontWeight: 600, color: 'white' }}>
                                  {b.playerName}
                                </td>
                                <td style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>
                                  {b.teamName || '-'}
                                </td>
                                <td>{b.oversBowled}</td>
                                <td>{b.maidens}</td>
                                <td>{b.runsConceded}</td>
                                <td style={{ fontWeight: 700, color: 'var(--accent-ruby)' }}>
                                  {b.wickets}
                                </td>
                                <td style={{ color: 'var(--accent-emerald)' }}>{b.economy?.toFixed(2)}</td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    ) : (
                      <div style={{ color: 'var(--text-muted)', padding: '1rem', background: 'var(--bg-elevated)', borderRadius: 'var(--radius-sm)', fontSize: '0.85rem' }}>
                        Detailed bowling player statistics are not available for this fixture.
                      </div>
                    )}
                  </div>
                </div>
              )}

              {/* TAB 3: MATCH INFO */}
              {activeTab === 'info' && (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '1rem' }}>
                  <div style={{ background: 'var(--bg-elevated)', padding: '1.25rem', borderRadius: 'var(--radius-sm)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontSize: '0.8rem' }}>
                      <MapPin size={16} /> Venue
                    </div>
                    <div style={{ color: 'white', fontWeight: 600 }}>{match.venue || 'Stadium'}</div>
                  </div>

                  <div style={{ background: 'var(--bg-elevated)', padding: '1.25rem', borderRadius: 'var(--radius-sm)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontSize: '0.8rem' }}>
                      <Calendar size={16} /> Date
                    </div>
                    <div style={{ color: 'white', fontWeight: 600 }}>{match.date || 'TBD'}</div>
                  </div>

                  <div style={{ background: 'var(--bg-elevated)', padding: '1.25rem', borderRadius: 'var(--radius-sm)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontSize: '0.8rem' }}>
                      <Clock size={16} /> Start Time (GMT)
                    </div>
                    <div style={{ color: 'white', fontWeight: 600 }}>{match.dateTimeGmt || 'Not specified'}</div>
                  </div>

                  <div style={{ background: 'var(--bg-elevated)', padding: '1.25rem', borderRadius: 'var(--radius-sm)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-muted)', marginBottom: '0.4rem', fontSize: '0.8rem' }}>
                      <UserCheck size={16} /> External API Match ID
                    </div>
                    <div style={{ color: '#38bdf8', fontWeight: 500, wordBreak: 'break-all', fontSize: '0.85rem' }}>
                      {match.externalId}
                    </div>
                  </div>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};
