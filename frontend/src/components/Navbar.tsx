import React from 'react';
import { Search, RefreshCw, Menu } from 'lucide-react';
import type { ApiStatus } from '../types/cricket';

interface NavbarProps {
  activeNavTab: 'dashboard' | 'live' | 'upcoming' | 'completed';
  onNavTabChange: (tab: 'dashboard' | 'live' | 'upcoming' | 'completed') => void;
  searchQuery: string;
  onSearchChange: (q: string) => void;
  onRefresh: () => void;
  isRefreshing: boolean;
  lastUpdated: Date | null;
  apiStatus: ApiStatus | null;
  onOpenApiModal: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({
  activeNavTab,
  onNavTabChange,
  searchQuery,
  onSearchChange,
  onRefresh,
  isRefreshing,
  lastUpdated,
  onOpenApiModal,
}) => {
  return (
    <header className="top-navbar">
      {/* Upper Main Nav */}
      <div className="nav-inner">
        {/* Brand Logo */}
        <div className="logo-wrapper" onClick={() => onNavTabChange('dashboard')}>
          <div className="logo-icon" title="CricketLive">
            🏏
          </div>
          <div className="logo-text">
            Cricket<span>Live</span>
          </div>
        </div>

        {/* Search Bar */}
        <div className="nav-search">
          <Search size={16} className="nav-search-icon" />
          <input
            type="text"
            placeholder="Search matches by team, tournament or venue..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
          />
        </div>

        {/* Right Controls */}
        <div className="nav-actions">
          <div className="last-updated-badge">
            <div className="last-updated-label">Last Updated</div>
            <div className="last-updated-time">
              {lastUpdated ? lastUpdated.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }) : 'Just now'}
            </div>
          </div>

          <button
            className="icon-btn"
            onClick={onRefresh}
            disabled={isRefreshing}
            title="Refresh Live Scores"
          >
            <RefreshCw size={17} className={isRefreshing ? 'spin-animation' : ''} />
          </button>

          <button
            className="icon-btn"
            onClick={onOpenApiModal}
            title="System & Architecture Info"
          >
            <Menu size={18} />
          </button>
        </div>
      </div>

      {/* Secondary Horizontal Nav Links */}
      <nav className="sub-navbar">
        <div className="sub-nav-inner">
          <div
            className={`sub-nav-link ${activeNavTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => onNavTabChange('dashboard')}
          >
            Dashboard
          </div>
          <div
            className={`sub-nav-link ${activeNavTab === 'live' ? 'active' : ''}`}
            onClick={() => onNavTabChange('live')}
          >
            Live Matches
          </div>
          <div
            className={`sub-nav-link ${activeNavTab === 'upcoming' ? 'active' : ''}`}
            onClick={() => onNavTabChange('upcoming')}
          >
            Upcoming
          </div>
          <div
            className={`sub-nav-link ${activeNavTab === 'completed' ? 'active' : ''}`}
            onClick={() => onNavTabChange('completed')}
          >
            Results
          </div>
        </div>
      </nav>
    </header>
  );
};
