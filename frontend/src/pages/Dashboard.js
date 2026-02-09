import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Dashboard = () => {
  const [tenants, setTenants] = useState([]);
  const [policies, setPolicies] = useState([]);
  const [apiKeys, setApiKeys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const [tenantsRes, policiesRes, apiKeysRes] = await Promise.all([
        axios.get('/api/v1/tenants'),
        axios.get('/api/v1/policies'),
        axios.get('/api/v1/api-keys')
      ]);

      setTenants(tenantsRes.data);
      setPolicies(policiesRes.data);
      setApiKeys(apiKeysRes.data);
    } catch (err) {
      setError('Failed to load dashboard data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div>
      <h1>Dashboard</h1>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <h3>Total Tenants</h3>
          <div className="metric">{tenants.length}</div>
        </div>

        <div className="dashboard-card">
          <h3>Total Policies</h3>
          <div className="metric">{policies.length}</div>
        </div>

        <div className="dashboard-card">
          <h3>Total API Keys</h3>
          <div className="metric">{apiKeys.length}</div>
        </div>

        <div className="dashboard-card">
          <h3>Algorithms Used</h3>
          <div className="metric">
            {[...new Set(policies.map(p => p.algorithm))].length}
          </div>
        </div>
      </div>

      <div className="recent-activity">
        <h2>Recent Tenants</h2>
        {tenants.slice(0, 5).map(tenant => (
          <div key={tenant.id} className="activity-item">
            <strong>{tenant.name}</strong> - Status: {tenant.status}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Dashboard;