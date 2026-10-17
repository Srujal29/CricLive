import React from 'react';

export const LoadingSkeleton: React.FC = () => {
  return (
    <div className="matches-grid">
      {[1, 2, 3, 4, 5, 6].map((idx) => (
        <div key={idx} className="skeleton-card">
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
            <div className="skeleton-shimmer" style={{ width: '60px', height: '22px' }} />
            <div className="skeleton-shimmer" style={{ width: '85px', height: '22px' }} />
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginBottom: '1.5rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                <div className="skeleton-shimmer" style={{ width: '28px', height: '20px' }} />
                <div className="skeleton-shimmer" style={{ width: '110px', height: '20px' }} />
              </div>
              <div className="skeleton-shimmer" style={{ width: '65px', height: '20px' }} />
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                <div className="skeleton-shimmer" style={{ width: '28px', height: '20px' }} />
                <div className="skeleton-shimmer" style={{ width: '120px', height: '20px' }} />
              </div>
              <div className="skeleton-shimmer" style={{ width: '65px', height: '20px' }} />
            </div>
          </div>

          <div className="skeleton-shimmer" style={{ width: '70%', height: '16px', marginBottom: '1.25rem' }} />
          <div className="skeleton-shimmer" style={{ width: '100%', height: '1px', marginBottom: '0.75rem' }} />
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <div className="skeleton-shimmer" style={{ width: '120px', height: '14px' }} />
            <div className="skeleton-shimmer" style={{ width: '80px', height: '14px' }} />
          </div>
        </div>
      ))}
    </div>
  );
};
