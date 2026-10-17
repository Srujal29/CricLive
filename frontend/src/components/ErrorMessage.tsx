import React from 'react';
import { AlertCircle, RefreshCw } from 'lucide-react';

interface ErrorMessageProps {
  message: string;
  onRetry: () => void;
}

export const ErrorMessage: React.FC<ErrorMessageProps> = ({ message, onRetry }) => {
  return (
    <div className="error-banner">
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.85rem' }}>
        <AlertCircle size={24} color="#ef4444" style={{ flexShrink: 0 }} />
        <div>
          <div style={{ fontWeight: 600, color: '#f87171' }}>Backend Connection Notice</div>
          <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
            {message || 'Unable to connect to Spring Boot backend at http://localhost:8080. Ensure the server is running.'}
          </div>
        </div>
      </div>
      <button className="btn-secondary" onClick={onRetry} style={{ borderColor: 'rgba(239, 68, 68, 0.4)' }}>
        <RefreshCw size={14} />
        <span>Retry</span>
      </button>
    </div>
  );
};
