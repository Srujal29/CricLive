import React, { useState, useEffect, useCallback } from 'react';
import { Navbar } from './components/Navbar';
import { HeroMatchBanner } from './components/HeroMatchBanner';
import { MatchCard } from './components/MatchCard';
import { ScorecardDetailView } from './components/ScorecardDetailView';
import { ApiStatusModal } from './components/ApiStatusModal';
import { LoadingSkeleton } from './components/LoadingSkeleton';
import { ErrorMessage } from './components/ErrorMessage';
import type { Match, ApiStatus } from './types/cricket';
import { cricketApi } from './services/cricketApi';
import { Calendar } from 'lucide-react';

export const App: React.FC = () => {
  const [navTab, setNavTab] = useState<'dashboard' | 'live' | 'upcoming' | 'completed'>('dashboard');
  const [filterPill, setFilterPill] = useState<'all' | 'live' | 'upcoming' | 'completed'>('all');
  const [searchQuery, setSearchQuery] = useState<string>('');
  
  const [liveMatches, setLiveMatches] = useState<Match[]>([]);
  const [upcomingMatches, setUpcomingMatches] = useState<Match[]>([]);
  const [completedMatches, setCompletedMatches] = useState<Match[]>([]);
  
  const [loading, setLoading] = useState<boolean>(true);
  const [isRefreshing, setIsRefreshing] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedMatch, setSelectedMatch] = useState<Match | null>(null);
  
  const [apiStatus, setApiStatus] = useState<ApiStatus | null>(null);
  const [isApiModalOpen, setIsApiModalOpen] = useState<boolean>(false);
  const [lastUpdated, setLastUpdated] = useState<Date | null>(new Date());

  // Load matches from Spring Boot backend
  const loadMatches = useCallback(async (showSkeleton = false) => {
    try {
      if (showSkeleton) setLoading(true);
      setError(null);

      const [liveRes, upRes, compRes, statusRes] = await Promise.all([
        cricketApi.getLiveMatches(),
        cricketApi.getUpcomingMatches(),
        cricketApi.getCompletedMatches(),
        cricketApi.getApiStatus().catch(() => null),
      ]);

      setLiveMatches(liveRes || []);
      setUpcomingMatches(upRes || []);
      setCompletedMatches(compRes || []);
      if (statusRes) setApiStatus(statusRes);
      setLastUpdated(new Date());
    } catch (err: any) {
      console.error('Error fetching cricket data:', err);
      setError(
        err.response?.data?.message ||
          'Failed to connect to backend server. Make sure the Spring Boot service is running on port 8080.'
      );
    } finally {
      setLoading(false);
      setIsRefreshing(false);
    }
  }, []);

  // Trigger manual refresh
  const handleManualRefresh = async () => {
    setIsRefreshing(true);
    try {
      await cricketApi.refreshScores().catch((e) => console.warn('Refresh note:', e));
    } catch (e) {
      console.warn('Backend refresh error:', e);
    }
    await loadMatches(false);
  };

  // Initial load
  useEffect(() => {
    loadMatches(true);
  }, [loadMatches]);

  // Periodic auto-refresh every 45s
  useEffect(() => {
    const timer = setInterval(() => {
      loadMatches(false);
    }, 45000);
    return () => clearInterval(timer);
  }, [loadMatches]);

  // Synchronize navTab with filterPill
  const handleNavTabChange = (tab: 'dashboard' | 'live' | 'upcoming' | 'completed') => {
    setNavTab(tab);
    setSelectedMatch(null);
    if (tab === 'dashboard') setFilterPill('all');
    else if (tab === 'live') setFilterPill('live');
    else if (tab === 'upcoming') setFilterPill('upcoming');
    else if (tab === 'completed') setFilterPill('completed');
  };

  // Combined matches list
  const allMatches = [...liveMatches, ...upcomingMatches, ...completedMatches];

  // Featured match for Hero Banner (e.g. first live match, or first available match)
  const featuredMatch = liveMatches.length > 0 ? liveMatches[0] : allMatches[0];

  // Filter based on active filterPill or navTab
  const getFilteredMatches = () => {
    let list: Match[] = [];
    if (filterPill === 'live' || navTab === 'live') {
      list = liveMatches;
    } else if (filterPill === 'upcoming' || navTab === 'upcoming') {
      list = upcomingMatches;
    } else if (filterPill === 'completed' || navTab === 'completed') {
      list = completedMatches;
    } else {
      list = allMatches;
    }

    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase().trim();
      list = list.filter(
        (m) =>
          m.name.toLowerCase().includes(q) ||
          m.team1.toLowerCase().includes(q) ||
          m.team2.toLowerCase().includes(q) ||
          (m.venue && m.venue.toLowerCase().includes(q))
      );
    }

    return list;
  };

  const displayedMatches = getFilteredMatches();

  // Current formatted date for header badge (e.g. "Today Thursday, 14 March" or today's date)
  const todayFormatted = new Intl.DateTimeFormat('en-US', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
  }).format(new Date());

  return (
    <div style={{ minHeight: '100vh', backgroundColor: 'var(--bg-page)' }}>
      {/* Top Navbar matching Figma */}
      <Navbar
        activeNavTab={navTab}
        onNavTabChange={handleNavTabChange}
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
        onRefresh={handleManualRefresh}
        isRefreshing={isRefreshing}
        lastUpdated={lastUpdated}
        apiStatus={apiStatus}
        onOpenApiModal={() => setIsApiModalOpen(true)}
      />

      <main className="page-container">
        {/* Error notification if backend connection fails */}
        {error && (
          <ErrorMessage
            message={error}
            onRetry={() => loadMatches(true)}
          />
        )}

        {/* IF A MATCH IS CLICKED: DISPLAY DETAILED SCORECARD VIEW (FIGMA SCREEN 1) */}
        {selectedMatch ? (
          <ScorecardDetailView
            match={selectedMatch}
            onBack={() => setSelectedMatch(null)}
          />
        ) : (
          /* OTHERWISE DISPLAY MATCH CENTRE & DASHBOARD (FIGMA SCREEN 2, 3, 4) */
          <>
            {/* Page Header */}
            <div className="page-header">
              <div>
                <div className="header-category">Match Centre</div>
                <h1 className="header-title">
                  {navTab === 'live'
                    ? 'Live Matches'
                    : navTab === 'upcoming'
                    ? 'Upcoming Matches'
                    : navTab === 'completed'
                    ? 'Match Results'
                    : 'Live Cricket'}
                </h1>
                <p className="header-subtitle">
                  Scores, schedules and results from around the world.
                </p>
              </div>

              <div className="header-date-badge">
                <Calendar size={18} color="var(--primary-green)" />
                <div>
                  <div className="date-label">Today</div>
                  <div className="date-value">{todayFormatted}</div>
                </div>
              </div>
            </div>

            {/* Featured Hero Match Banner (Figma Screen 2) */}
            {featuredMatch && navTab !== 'upcoming' && navTab !== 'completed' && (
              <HeroMatchBanner
                match={featuredMatch}
                onViewScorecard={(m) => setSelectedMatch(m)}
              />
            )}

            {/* Section Header Row with Pills */}
            <div className="section-header-row">
              <div className="section-title-wrap">
                <h3>
                  {navTab === 'live' ? 'Live matches' : navTab === 'upcoming' ? 'Upcoming fixtures' : navTab === 'completed' ? 'Completed results' : 'Match centre'}
                </h3>
                <p>Follow every fixture and result</p>
              </div>

              {/* Filter Pills */}
              <div className="pills-group">
                <button
                  className={`pill-btn ${filterPill === 'all' && navTab === 'dashboard' ? 'active' : ''}`}
                  onClick={() => {
                    setNavTab('dashboard');
                    setFilterPill('all');
                  }}
                >
                  <span>All matches</span>
                  <span className="pill-count">{allMatches.length}</span>
                </button>

                <button
                  className={`pill-btn ${filterPill === 'live' || navTab === 'live' ? 'active' : ''}`}
                  onClick={() => {
                    setNavTab('live');
                    setFilterPill('live');
                  }}
                >
                  <span>Live</span>
                  <span className="pill-count">{liveMatches.length}</span>
                </button>

                <button
                  className={`pill-btn ${filterPill === 'upcoming' || navTab === 'upcoming' ? 'active' : ''}`}
                  onClick={() => {
                    setNavTab('upcoming');
                    setFilterPill('upcoming');
                  }}
                >
                  <span>Upcoming</span>
                  <span className="pill-count">{upcomingMatches.length}</span>
                </button>

                <button
                  className={`pill-btn ${filterPill === 'completed' || navTab === 'completed' ? 'active' : ''}`}
                  onClick={() => {
                    setNavTab('completed');
                    setFilterPill('completed');
                  }}
                >
                  <span>Completed</span>
                  <span className="pill-count">{completedMatches.length}</span>
                </button>
              </div>
            </div>

            {/* Matches Grid */}
            {loading ? (
              <LoadingSkeleton />
            ) : displayedMatches.length > 0 ? (
              <div className="matches-grid">
                {displayedMatches.map((match) => (
                  <MatchCard
                    key={match.externalId || match.id}
                    match={match}
                    onSelect={(m) => setSelectedMatch(m)}
                  />
                ))}
              </div>
            ) : (
              <div
                style={{
                  background: 'var(--bg-white)',
                  border: '1px solid var(--border-light)',
                  borderRadius: 'var(--radius-lg)',
                  padding: '3.5rem 1.5rem',
                  textAlign: 'center',
                }}
              >
                <div style={{ fontSize: '2.5rem', marginBottom: '0.75rem' }}>🏏</div>
                <h3 style={{ color: '#0f172a', marginBottom: '0.5rem' }}>No matches found</h3>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', maxWidth: '420px', margin: '0 auto 1.5rem auto' }}>
                  No matches match your current query or category filter.
                </p>
                <button
                  className="btn-scorecard-green"
                  style={{ margin: '0 auto' }}
                  onClick={() => {
                    setSearchQuery('');
                    setNavTab('dashboard');
                    setFilterPill('all');
                  }}
                >
                  View All Fixtures
                </button>
              </div>
            )}
          </>
        )}
      </main>

      {/* Database & CricAPI Architecture Modal */}
      {isApiModalOpen && (
        <ApiStatusModal
          status={apiStatus}
          onClose={() => setIsApiModalOpen(false)}
          onDataReset={() => loadMatches(false)}
        />
      )}
    </div>
  );
};

export default App;
