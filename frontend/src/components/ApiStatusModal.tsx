import React from 'react';
import { X, Database, CheckCircle2 } from 'lucide-react';
import type { ApiStatus } from '../types/cricket';

interface ApiStatusModalProps {
  status: ApiStatus | null;
  onClose: () => void;
  onDataReset: () => void;
}

export const ApiStatusModal: React.FC<ApiStatusModalProps> = ({ status, onClose }) => {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '620px' }}>
        <div className="modal-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
            <Database size={22} color="var(--primary-green)" />
            <h2 style={{ fontSize: '1.2rem', color: '#0f172a' }}>Real Cricket Data & MySQL Status</h2>
          </div>
          <button className="modal-close-btn" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body" style={{ gap: '1.25rem' }}>
          {/* Status Alert Banner */}
          <div
            style={{
              padding: '1rem',
              borderRadius: 'var(--radius-md)',
              background: 'var(--primary-green-light)',
              border: '1px solid var(--primary-green-border)',
              display: 'flex',
              alignItems: 'center',
              gap: '0.75rem',
            }}
          >
            <CheckCircle2 size={24} color="#16a34a" />
            <div>
              <div style={{ fontWeight: 700, color: '#15803d', fontSize: '0.95rem' }}>
                Live CricAPI Connected & Synchronized
              </div>
              <div style={{ fontSize: '0.82rem', color: 'var(--text-secondary)' }}>
                Real match fixtures are fetched directly from CricAPI and stored in your MySQL database (`cricket_score_db`).
              </div>
            </div>
          </div>

          {/* MySQL Database Status */}
          <div>
            <h4 style={{ color: '#0f172a', marginBottom: '0.6rem', fontSize: '0.9rem', fontWeight: 700 }}>
              MySQL Database Stored Records (`cricket_score_db`)
            </h4>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '0.75rem' }}>
              <div style={{ background: '#f8fafc', border: '1px solid var(--border-light)', padding: '0.85rem', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
                <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#0f172a' }}>
                  {status?.totalMatchesInDb || 0}
                </div>
                <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>Total Fixtures</div>
              </div>
              <div style={{ background: '#f8fafc', border: '1px solid var(--border-light)', padding: '0.85rem', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
                <div style={{ fontSize: '1.4rem', fontWeight: 800, color: 'var(--live-red)' }}>
                  {status?.liveMatchesCount || 0}
                </div>
                <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>Live Matches</div>
              </div>
              <div style={{ background: '#f8fafc', border: '1px solid var(--border-light)', padding: '0.85rem', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
                <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#2563eb' }}>
                  {status?.upcomingMatchesCount || 0}
                </div>
                <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>Upcoming</div>
              </div>
              <div style={{ background: '#f8fafc', border: '1px solid var(--border-light)', padding: '0.85rem', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
                <div style={{ fontSize: '1.4rem', fontWeight: 800, color: 'var(--primary-green)' }}>
                  {status?.completedMatchesCount || 0}
                </div>
                <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>Completed</div>
              </div>
            </div>
          </div>

          {/* Provider details */}
          <div style={{ background: '#f8fafc', border: '1px solid var(--border-light)', padding: '1.15rem', borderRadius: 'var(--radius-md)', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
            <div style={{ fontWeight: 700, color: '#0f172a', marginBottom: '0.35rem' }}>
              Data Provider Architecture
            </div>
            <div>
              External API: <strong>CricketData.org / CricAPI v1</strong><br />
              Endpoint: <code>https://api.cricapi.com/v1/currentMatches</code><br />
              Polling Interval: <strong>Every 45 seconds</strong> (Spring Boot automated sync)<br />
              Persistence: MySQL tables <code>matches</code>, <code>score_snapshots</code>, <code>player_statistics</code>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
