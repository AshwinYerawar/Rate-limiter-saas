import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ApiKeys = () => {
  const [apiKeys, setApiKeys] = useState([]);
  const [tenants, setTenants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingApiKey, setEditingApiKey] = useState(null);
  const [formData, setFormData] = useState({
    key: '',
    tenantId: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [apiKeysRes, tenantsRes] = await Promise.all([
        axios.get('/api/v1/api-keys'),
        axios.get('/api/v1/tenants')
      ]);

      setApiKeys(apiKeysRes.data);
      setTenants(tenantsRes.data);
    } catch (err) {
      setError('Failed to load data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const generateApiKey = () => {
    return 'ak_' + Math.random().toString(36).substr(2, 16);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const tenant = tenants.find(t => t.id === formData.tenantId);
      const apiKeyData = {
        ...formData,
        tenant: tenant
      };

      if (editingApiKey) {
        await axios.put(`/api/v1/api-keys/${editingApiKey.id}`, apiKeyData);
      } else {
        await axios.post('/api/v1/api-keys', apiKeyData);
      }

      fetchData();
      resetForm();
    } catch (err) {
      setError('Failed to save API key');
      console.error(err);
    }
  };

  const handleEdit = (apiKey) => {
    setEditingApiKey(apiKey);
    setFormData({
      key: apiKey.key,
      tenantId: apiKey.tenant.id,
      status: apiKey.status
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this API key?')) {
      try {
        await axios.delete(`/api/v1/api-keys/${id}`);
        fetchData();
      } catch (err) {
        setError('Failed to delete API key');
        console.error(err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      key: '',
      tenantId: '',
      status: 'ACTIVE'
    });
    setEditingApiKey(null);
    setShowForm(false);
  };

  const handleGenerateKey = () => {
    setFormData(prev => ({
      ...prev,
      key: generateApiKey()
    }));
  };

  if (loading) {
    return <div className="loading">Loading API keys...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>API Keys</h1>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : 'Add API Key'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <div className="form-container">
          <h2>{editingApiKey ? 'Edit API Key' : 'Create New API Key'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="key">API Key:</label>
              <div style={{ display: 'flex', gap: '10px' }}>
                <input
                  type="text"
                  id="key"
                  name="key"
                  value={formData.key}
                  onChange={handleInputChange}
                  placeholder="ak_..."
                  required
                  style={{ flex: 1 }}
                />
                <button
                  type="button"
                  className="btn"
                  onClick={handleGenerateKey}
                  style={{ whiteSpace: 'nowrap' }}
                >
                  Generate
                </button>
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="tenantId">Tenant:</label>
              <select
                id="tenantId"
                name="tenantId"
                value={formData.tenantId}
                onChange={handleInputChange}
                required
              >
                <option value="">Select Tenant</option>
                {tenants.map(tenant => (
                  <option key={tenant.id} value={tenant.id}>
                    {tenant.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="status">Status:</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleInputChange}
              >
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
                <option value="REVOKED">Revoked</option>
              </select>
            </div>

            <div className="form-actions">
              <button type="submit" className="btn btn-primary">
                {editingApiKey ? 'Update API Key' : 'Create API Key'}
              </button>
              <button type="button" className="btn" onClick={resetForm}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="api-keys-list">
        <h2>All API Keys</h2>
        {apiKeys.length === 0 ? (
          <p>No API keys found. Create your first API key!</p>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>API Key</th>
                  <th>Tenant</th>
                  <th>Status</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {apiKeys.map(apiKey => (
                  <tr key={apiKey.id}>
                    <td style={{ fontFamily: 'monospace', fontSize: '0.9em' }}>
                      {apiKey.key}
                    </td>
                    <td>{apiKey.tenant.name}</td>
                    <td>
                      <span className={`status status-${apiKey.status.toLowerCase()}`}>
                        {apiKey.status}
                      </span>
                    </td>
                    <td>{new Date(apiKey.createdAt).toLocaleDateString()}</td>
                    <td>
                      <button
                        className="btn btn-small"
                        onClick={() => handleEdit(apiKey)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-small btn-danger"
                        onClick={() => handleDelete(apiKey.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default ApiKeys;